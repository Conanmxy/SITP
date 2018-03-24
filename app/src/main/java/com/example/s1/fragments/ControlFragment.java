package com.example.s1.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.controlActivity.ControlDetailActivity;
import com.example.s1.entity.WheelView;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/10/17.
 */
public class ControlFragment extends Fragment implements View.OnClickListener{



    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG = "abcdefg";
    // private static final String[] HOUR = new String[]{"24","23","22","21","20","19","18","17","15","14","13","12","11","10","9","8","7","6","5","4","3","2","1","0"};
    private static final String[] MINUTE = new String[]{"59","58","57","56","55","54","53","52","51","50","49","48","47","46","45","44","43","42","41","40","39","38","37","36","35","34","33","32","31","30",
            "29","28","27","26","25","24","23","22","21","20","19","18","17","16","15","14","13","12","11","10","9","8","7","6","5","4","3","2","1","0"};
    /* private static final String[] SECOND = new String[]{"59","58","57","56","55","54","53","52","51","50","49","48","47","46","45","44","43","42","41","40","39","38","37","36","35","34","33","32","31","30",
                                                             "29","28","27","26","25","24","23","22","21","20","19","18","17","16","15","14","13","12","11","10","9","8","7","6","5","4","3","2","1"};
 */
    Button btn,btn1,btn2,btn3,btn4;
    Calendar c = Calendar.getInstance();
    TextView edit;
    int flag_btn1=0,flag_btn2=0,flag_btn3=0,flag_btn4=0,time=59;
    WheelView wvaminute;
    String b;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.control_layout,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);




        wvaminute = (WheelView) getActivity().findViewById(R.id.main_wvminuute);

        wvaminute.setOffset(1);
        wvaminute.setItems(Arrays.asList(MINUTE));
        wvaminute.setOnWheelViewListener(new WheelView.OnWheelViewListener()
        {
            @Override
            public void onSelected(int selectedIndex, String item) {
                //  获取滚轮选中的时间
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                time = Integer.parseInt(item);
            }
        });

        btn=(Button)getActivity().findViewById(R.id.main_btn);
        btn1=(Button)getActivity().findViewById(R.id.btn1);
        btn2=(Button)getActivity().findViewById(R.id.btn2);
        btn3=(Button)getActivity().findViewById(R.id.btn3);
        btn4=(Button)getActivity().findViewById(R.id.btn4);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    /*public  void start(View v)
    {
        TextView edit=(TextView) findViewById(R.id.edt);
        //String str = edit.getText().toString();
        edit.setText("成功");
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn:
                /*if(flag_btn1==0&&flag_btn2==0&&flag_btn3==0&&flag_btn4==0) {
                    edit.setText(time);
                }*/
                if(flag_btn1==1)
                    //edit.setText("按钮一");
                    time = 10;
                if(flag_btn2==1)
                    //edit.setText("按钮二");
                    time = 30;
                if(flag_btn3==1)
                    //edit.setText("按钮三");
                    time = 40;
                if(flag_btn4==1)
                    //edit.setText("按钮四");
                    time = 60;
                //edit.setText(Integer.toString(time));
                Intent it = new Intent(getActivity(),ControlDetailActivity.class);
                it.putExtra("time",time);
                startActivity(it);
                break;
            case  R.id.btn1:
                if(flag_btn1==0){
                    btn1.setTextColor(Color.rgb(65,105,225));
                    btn2.setTextColor(Color.rgb(169,169,169));
                    btn3.setTextColor(Color.rgb(169,169,169));
                    btn4.setTextColor(Color.rgb(169,169,169));
                    flag_btn1=1;
                    flag_btn2=0;
                    flag_btn3=0;
                    flag_btn4=0;
                }
                else {
                    flag_btn1 = 0;
                    btn1.setTextColor(Color.rgb(169,169,169));
                }
                break;
            case  R.id.btn2:
                if(flag_btn2==0){
                    btn2.setTextColor(Color.rgb(65,105,225));
                    btn1.setTextColor(Color.rgb(169,169,169));
                    btn3.setTextColor(Color.rgb(169,169,169));
                    btn4.setTextColor(Color.rgb(169,169,169));
                    flag_btn2=1;
                    flag_btn1=0;
                    flag_btn3=0;
                    flag_btn4=0;
                }
                else {
                    flag_btn2 = 0;
                    btn2.setTextColor(Color.rgb(169,169,169));
                }
                break;
            case  R.id.btn3:
                if(flag_btn3==0){
                    btn3.setTextColor(Color.rgb(65,105,225));
                    btn1.setTextColor(Color.rgb(169,169,169));
                    btn2.setTextColor(Color.rgb(169,169,169));
                    btn4.setTextColor(Color.rgb(169,169,169));
                    flag_btn3=1;
                    flag_btn1=0;
                    flag_btn2=0;
                    flag_btn4=0;
                }
                else {
                    flag_btn3 = 0;
                    btn3.setTextColor(Color.rgb(169,169,169));
                }
                break;
            case  R.id.btn4:
                if(flag_btn4==0){
                    btn4.setTextColor(Color.rgb(65,105,225));
                    btn1.setTextColor(Color.rgb(169,169,169));
                    btn2.setTextColor(Color.rgb(169,169,169));
                    btn3.setTextColor(Color.rgb(169,169,169));
                    flag_btn4=1;
                    flag_btn1=0;
                    flag_btn2=0;
                    flag_btn3=0;
                }
                else {
                    flag_btn4 = 0;
                    btn4.setTextColor(Color.rgb(169,169,169));
                }
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {








   // }
}
