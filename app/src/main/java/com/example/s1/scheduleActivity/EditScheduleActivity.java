package com.example.s1.scheduleActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.entity.ChildModel;
import com.example.s1.calendarviewdemo.MyDatabaseHelper;
import com.example.s1.rxjava.RxBus2;


import java.sql.Date;

public class EditScheduleActivity extends AppCompatActivity {

    private EditText editText1;
    private ImageButton left1;
    private ImageButton right1;
    private TextView titleText1;
    private MyDatabaseHelper dbHelper1;
    SQLiteDatabase db_schedule1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_add);
        initialView1();
        initialEvent1();
    }
    private void initialView1()
    {
        editText1=(EditText) findViewById(R.id.schedule_edit);
        left1=(ImageButton) findViewById(R.id.title1_left);
        right1=(ImageButton) findViewById(R.id.title1_right);
        titleText1=(TextView) findViewById(R.id.title1_text);
        Intent intent=getIntent();
        String exist_diary= intent.getStringExtra("existed_text");
        editText1.setText(exist_diary);
        editText1.setSelection(editText1.getText().toString().length());
        String title_text=intent.getStringExtra("current_title");
        titleText1.setText(title_text);
        if(title_text.equals("编辑"))
            left1.setImageResource(R.mipmap.delete_white);
        else left1.setImageResource(R.mipmap.back_white);
        right1.setImageResource(R.mipmap.finished_white);
    }
    private void initialEvent1()
    {
        left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //目前是删除的功能
                if(titleText1.getText().toString().equals("编辑"))
                {
                    dbHelper1 = new MyDatabaseHelper(getApplicationContext(),"schedule.db",null,1);
                    db_schedule1=dbHelper1.getWritableDatabase();
                    int currentId=getIntent().getIntExtra("current_id",0);
                    db_schedule1.delete("schedule","id=?",new String[] {String.valueOf(currentId)});
                    editText1.setText("");
                    ChildModel diaryText=new ChildModel();
                    RxBus2.getDefault().post(diaryText);
                    db_schedule1.close();
                    finish();
                }else finish();
            }
        });

        right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleText1.getText().toString().equals("新建")){//此时插入数据库
                    dbHelper1 = new MyDatabaseHelper(getApplicationContext(),"schedule.db",null,1);
                    db_schedule1=dbHelper1.getWritableDatabase();
                    String date1=getIntent().getStringExtra("date");
                    Date time=new Date(System.currentTimeMillis());
                    String time1=time.toString();
                    String content1=editText1.getText().toString();
                    ContentValues cv=new ContentValues(3);
                    cv.put("date",date1);
                    cv.put("time",time1);
                    cv.put("content",content1);
                    db_schedule1.insert("schedule",null,cv);
                    ChildModel diaryText1=new ChildModel();
                    diaryText1.setContent(editText1.getText().toString());
                    diaryText1.setDate(date1);
                    diaryText1.setTime(time1);
                    diaryText1.save();
                    RxBus2.getDefault().post(diaryText1);
                    db_schedule1.close();
                    finish();
                }else
                if(titleText1.getText().toString().equals("编辑")){//此时要更新数据库
                    dbHelper1 = new MyDatabaseHelper(getApplicationContext(),"schedule.db",null,1);
                    db_schedule1=dbHelper1.getWritableDatabase();
                    ChildModel diaryText=new ChildModel();
                    int currentId=getIntent().getIntExtra("current_id",0);
                    String date1=getIntent().getStringExtra("date");
                    diaryText.setContent(editText1.getText().toString());
                    diaryText.setDate(date1);
                    diaryText.setTime(date1);
                    diaryText.setId(currentId);
                    diaryText.save();
                    ContentValues cv=new ContentValues(1);
                    cv.put("date",date1);
                    cv.put("time",date1);
                    cv.put("content",editText1.getText().toString());
                    db_schedule1.update("schedule",cv,"id = ?",new String[] {String.valueOf(currentId)});
                    RxBus2.getDefault().post(diaryText);
                    db_schedule1.close();
                    finish();
                }


            }
        });
    }

}
