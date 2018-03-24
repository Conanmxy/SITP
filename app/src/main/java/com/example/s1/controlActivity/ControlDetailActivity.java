package com.example.s1.controlActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.R;

public class ControlDetailActivity extends AppCompatActivity implements View.OnClickListener,DialogInterface.OnClickListener {

    private TextView timeNum;//显示时间数目
    private Button clickBtn;//点击放弃按钮
    private Handler mHandler = new Handler();//全局handler
    int i = 0;//倒计时的整个时间数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_detail);

        Intent it=getIntent();
        i = it.getIntExtra("time",0) * 60;

        timeNum = (TextView)findViewById(R.id.time_num);//显示剩余时间数
        clickBtn = (Button)findViewById(R.id.click_btn);
        clickBtn.setOnClickListener(this);
        new Thread(new ClassCut()).start();//开启倒计时
    }
   /* 0private class TimeOnclisten implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new ClassCut()). //开启倒计时
        }
    }*/

    @Override
    public void onClick(View v){
        new AlertDialog.Builder(this).setMessage("你确定放弃吗？")     // 设置显示信息
                .setCancelable(false)   // 禁用返回键关闭对话框
                .setIcon(android.R.drawable.ic_menu_edit)   //采用内建的图标
                .setPositiveButton("确定",this)   // 加入肯定按钮并监听事件
                .setNegativeButton("取消",this).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("你确定放弃吗？")     // 设置显示信息
                .setCancelable(false)   // 禁用返回键关闭对话框
                .setIcon(android.R.drawable.ic_menu_edit)   //采用内建的图标
                .setPositiveButton("确定",this)   // 加入肯定按钮并监听事件
                .setNegativeButton("取消",this).show();
    }
    @Override
    public void onClick(DialogInterface dialog,int which){
        if(which==DialogInterface.BUTTON_POSITIVE) {
            Toast.makeText(ControlDetailActivity.this, "很遗憾，挑战失败！下次加油吧。", Toast.LENGTH_SHORT).show();//提示中途退出
            finish();
        }
    }

    class ClassCut implements Runnable{//倒计时逻辑子线程
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(i>0){//整个倒计时执行的循环
                i--;
                mHandler.post(new Runnable() {//通过它在UI主线程中修改显示的剩余时间
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(i/60<10&&i%60>10)
                            timeNum.setText("0"+i/60+"  ："+i%60);//显示剩余时间
                        else if(i/60<10&&i%60<10)
                            timeNum.setText("0"+i/60+"  ：0"+i%60);//显示剩余时间
                        else if(i/60>10&&i%60<10)
                            timeNum.setText(i/60+"  ：0"+i%60);//显示剩余时间
                        else
                            timeNum.setText(i/60+"  ："+i%60);//显示剩余时间
                    }
                });
                try {
                    Thread.sleep(1000);//线程休眠一秒钟     这个就是倒计时的间隔时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //下面是倒计时结束逻辑
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //timeNum.setText("0");//一轮倒计时结束  修改剩余时间为一分钟
                    Toast.makeText(ControlDetailActivity.this, "恭喜你完成挑战！", Toast.LENGTH_LONG).show();//提示倒计时完成
                    finish();
                }
            });
            //i = 60;//修改倒计时剩余时间变量为60秒
        }
    }

}
