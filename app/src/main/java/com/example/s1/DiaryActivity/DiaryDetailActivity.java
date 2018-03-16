package com.example.s1.DiaryActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.Utils.ImageUtils;
import com.example.s1.Utils.ScreenUtils;
import com.example.s1.WriteDiaryActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaryDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button left;
    Button right;
    TextView title;
    EditText diary_edit;
    String input;//要显示的字符串
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        initialView();
        initialEvent();
        show(input);
    }
    //region
    private void initialView()
    {
        left=(Button)findViewById(R.id.title_left);
        right=(Button)findViewById(R.id.title_right);
        title=(TextView)findViewById(R.id.title_text);
        diary_edit=(EditText)findViewById(R.id.diary_content);
        title.setText("日记详情");
        left.setText("返回");
        right.setText("编辑");
        input=getIntent().getStringExtra("existed_text");
        id=getIntent().getIntExtra("current_id",0);
    }

    private void initialEvent()
    {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DiaryDetailActivity.this, WriteDiaryActivity.class);
                intent.putExtra("existed_diary",diary_edit.getText().toString());
                intent.putExtra("current_title","编辑");
                intent.putExtra("current_id",id);
                Log.d("start","dddd");
                startActivity(intent);
                finish();
               // intent.putExtra("exist")
            }
        });
    }
    private void show(String input)
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
             int width=ScreenUtils.getScreenWidth(DiaryDetailActivity.this);
             int height=ScreenUtils.getScreenHeight(DiaryDetailActivity.this);

             Bitmap bitmap= ImageUtils.getSmallBitmap(path,width,480);
             ImageSpan imageSpan=new ImageSpan(this,bitmap);
             spannable.setSpan(imageSpan,start,end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
         }
         diary_edit.setText(spannable);
         diary_edit.append("\n");
         Log.d("last",diary_edit.getText().toString());
     }

    Html.ImageGetter imageGetter=new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            int width= ScreenUtils.getScreenWidth(DiaryDetailActivity.this);
            int height=ScreenUtils.getScreenHeight(DiaryDetailActivity.this);
            Bitmap bitmap= ImageUtils.getSmallBitmap(source,width,200);
            Drawable drawable=new BitmapDrawable(bitmap);
            drawable.setBounds(0,0,width,height);
            return drawable;
        }
    };


    //endregion
}
