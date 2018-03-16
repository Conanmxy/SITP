package com.example.s1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.Utils.ImageUtils;
import com.example.s1.Utils.ScreenUtils;
import com.example.s1.entity.DiaryText;
import com.example.s1.rxjava.RxBus2;

import org.litepal.crud.DataSupport;


import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WriteDiaryActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO = 2;
    private EditText editText;
    private Button left;
    private Button right;
    private TextView titleText;
    private ImageView addPic;
    private ImageView hideSoftInput;
    private ArrayList<String> uris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        uris=new ArrayList<>();
        initialView();
        initialEvent();
    }

    private void initialView() {
        editText = (EditText) findViewById(R.id.diary_edit);
        left = (Button) findViewById(R.id.title_left);
        right = (Button) findViewById(R.id.title_right);
        titleText = (TextView) findViewById(R.id.title_text);
        addPic = (ImageView) findViewById(R.id.diary_edit_add_pic_ib);
        hideSoftInput=(ImageView)findViewById(R.id.diary_edit_hideSoftInput_ib);

        Intent intent = getIntent();
        String exist_diary = intent.getStringExtra("existed_diary");
        uris=intent.getStringArrayListExtra("uri");
        if(uris==null)
            uris=new ArrayList<>();
        if(exist_diary!=null)
            showExistDiary(exist_diary);

        editText.setSelection(editText.getText().toString().length());
        String title_text = intent.getStringExtra("current_title");
        titleText.setText(title_text);
        if (title_text.equals("查看"))
            left.setText("删除");
        else left.setText("取消");
        right.setText("完成");
    }

    private void showExistDiary(String input)
    {
        //input 是获取将被解析的字符串
        //将图片那一串字符串解析出来，即<img src="xxx"/>
        Pattern p=Pattern.compile("\\<img src=\".*?\"\\/>");
        Matcher m=p.matcher(input);

        SpannableString spannable=new SpannableString(input);
        while(m.find())
        {
            Log.d("rgex",m.group());
            //这里s保存的是整个式子，即
            //<img src="xxx"/>，start和end保存下标
            String s=m.group();
            int start=m.start();
            int end=m.end();
            //path是去<img rsc=/>的中间的图片路径
            String path=s.replaceAll("\\<img src=\"|\"\\/>","").trim();
            Log.d("eliminte",path);

            //利用spannableString 和 imageSpan来替换掉这些图片
            int width=ScreenUtils.getScreenWidth(WriteDiaryActivity.this);
            int height=ScreenUtils.getScreenHeight(WriteDiaryActivity.this);

            Bitmap bitmap= ImageUtils.getSmallBitmap(path,width,480);
            ImageSpan imageSpan=new ImageSpan(this,bitmap);
            spannable.setSpan(imageSpan,start,end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        editText.setText(spannable);
        editText.append("\n");
        Log.d("last",editText.getText().toString());
    }

    private void initialEvent() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //目前是删除的功能
                if (left.getText().toString().equals("删除")) {
                    int currentId = getIntent().getIntExtra("current_id", 0);
                    DataSupport.deleteAll(DiaryText.class, "id=?", String.valueOf(currentId));
                    editText.setText("");
                    DiaryText diaryText = new DiaryText();
                    RxBus2.getDefault().post(diaryText);
                    finish();
                } else finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText.getText().toString().equals("新建")) {
                    //此时插入数据库
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("diary", editText.getText().toString());
                    editor.apply();
                    // SimpleDateFormat formatter =  new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    DiaryText diaryText1 = new DiaryText();
                    String diaryText=editText.getText().toString();
                    diaryText1.setDiaryText(diaryText);
                    diaryText1.setDate(date.toString());
                    Log.d("Diary_Text....:",diaryText);
                    diaryText1.save();
                    RxBus2.getDefault().post(diaryText1);
                    finish();
                }
                else if (titleText.getText().toString().equals("编辑"))
                {//取消了修改的功能
                    DiaryText diaryText = new DiaryText();
                    int currentId = getIntent().getIntExtra("current_id", 0);

                    diaryText.setDiaryText(editText.getText().toString());
                    diaryText.updateAll("id=?", String.valueOf(currentId));
                    RxBus2.getDefault().post(diaryText);
                    finish();
                }


            }
        });



        //插入图片部分
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(WriteDiaryActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WriteDiaryActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


        //隐藏键盘部分
        hideSoftInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hideinput","hide");
                InputMethodManager inputMethodManager=(InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
            }
        });

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document 类型的uri 则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的URI，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的URI，则直接或路径
                imagePath = uri.getPath();
            }
            //displayImage(imagePath);//根据图片路径显示图片
            Log.d("路径1",imagePath);
            insertImg(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
        Log.d("路径2",imagePath);
        insertImg(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过URI和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void insertImg(String path)
    {

        //为图片路径加上<img>标签
        String tagPath="<img src=\""+path+"\"/>";
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        if(bitmap!=null)
        {
            SpannableString ss=getBitmapMime2(path,tagPath);
            insertPhotoToEditText(ss);
            editText.append("\n");
            Log.d("YYPT",editText.getText().toString());
        }

    }

    private SpannableString getBitmapMime2(String path,String tagPath)
    {
        SpannableString ss=new SpannableString(tagPath);

        int width=ScreenUtils.getScreenWidth(WriteDiaryActivity.this);
        int height= ScreenUtils.getScreenHeight(WriteDiaryActivity.this);

        Bitmap bitmap= ImageUtils.getSmallBitmap(path,width,480);
        ImageSpan imageSpan=new ImageSpan(this,bitmap);
        ss.setSpan(imageSpan,0,tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void insertPhotoToEditText(SpannableString ss)
    {
        Editable et=editText.getText();
        int start=editText.getSelectionStart();
        et.insert(start,ss);
        editText.setText(et);
        //  editText.setText(Html.fromHtml(et,imageGetter,null));
        editText.setSelection(start+ss.length());
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
    }

    //图片缩放
    public Bitmap resizeImage(Bitmap bitmaporg,int widthNew,int heightNew)
    {
        int widthOld=bitmaporg.getWidth();
        int heightOld=bitmaporg.getHeight();
        float scaleWidht=(float)widthNew/widthOld;
        float scaleHeight=(float)heightNew/heightOld;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidht,scaleHeight);
        Bitmap bitmapNew=Bitmap.createBitmap(bitmaporg,0,0,widthOld,heightOld,matrix,true);
        return bitmapNew;
    }
}
