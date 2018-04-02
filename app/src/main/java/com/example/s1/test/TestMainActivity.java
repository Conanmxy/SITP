package com.example.s1.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s1.R;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

public class TestMainActivity extends Activity {

    private LinearLayout mLayout;
    private TextView mTextView;
    private RelativeLayout mLayout2;
    private TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//// 创建一个线性布局
//        mLayout = new LinearLayout(this);
//// 接着创建一个TextView
//        mTextView = new TextView(this);
//
//// 第一个参数为宽的设置，第二个参数为高的设置。
//        mTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT));
//// 设置mTextView的文字
//        mTextView.setText("这是TextView");
//// 设置字体大小
//        mTextView.setTextSize(20);
//// 设置背景
//        mTextView.setBackgroundColor(Color.BLUE);
//// 设置字体颜色
//        mTextView.setTextColor(Color.RED);
////设置居中
//        mTextView.setGravity(Gravity.CENTER);
////
//        mTextView.setPadding(1, 0, 0, 0);//left, top, right, bottom
//
//// 将TextView添加到Linearlayout中去
//        mLayout.addView(mTextView);
//




// 创建一个线性布局
        mLayout2 = new RelativeLayout(this);
// 接着创建一个TextView
        mTextView2 = new TextView(this);

// 第一个参数为宽的设置，第二个参数为高的设置。
        mTextView2.setLayoutParams(new RelativeLayout.LayoutParams(200,
                50));
// 设置mTextView的文字
        mTextView2.setText("这是我的TextView2");
// 设置字体大小
        mTextView2.setTextSize(20);
// 设置背景
        mTextView2.setBackgroundColor(Color.BLUE);
// 设置字体颜色
        mTextView2.setTextColor(Color.RED);
// 设置居中
        mTextView2.setGravity(Gravity.CENTER);
//相对位置
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mTextView2.getLayoutParams();
        params.setMargins(200, 400, 0, 0);// 通过自定义坐标来放置你的控件left, top, right, bottom
        mTextView2 .setLayoutParams(params);//
// 将TextView添加到RelativeLayout中去
        mLayout2.addView(mTextView2);
// 展现这个线性布局
       // setContentView(mLayout);
        setContentView(mLayout2);

    }
}