package com.example.s1.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.rxjava.RxBus2;

import org.w3c.dom.Text;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/24.
 */
public class PopWindow extends PopupWindow {
    private View conentView;
    private TextView t_sport;
    public PopWindow(final Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_window, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 4 + 20);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

       // this.setAnimationStyle(R.anim.animation_slide_right);
        t_sport=(TextView)context.findViewById(R.id.m_sport);
        conentView.findViewById(R.id.m_pol).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
               // removeView();
                RxBus2.getDefault().post("政治");
              //  PopWindow.this.dismiss();
            }
        });

        conentView.findViewById(R.id.m_war).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something before signing out
                context.finish();
                PopWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.m_sport).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something you need here

                PopWindow.this.dismiss();
            }
        });


        //register();


    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 3, 5);
        } else {
            this.dismiss();
        }
    }

    public void removeView(int j,int i)
    {
        LinearLayout linearLayout=(LinearLayout)(conentView.findViewById(R.id.pop_lin));
        int count= linearLayout.getChildCount();
        Log.d("count1  ", Integer.toString(count));
        linearLayout.removeViewAt(2*j-i);
        count= linearLayout.getChildCount();
        Log.d("count2  ", Integer.toString(count));

    }

    //注册RxBus
    public void register(){
        //rxbus
        RxBus2.getDefault().toObservable(ArrayList.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<ArrayList>() {
                    @Override
                    public void call(ArrayList newsKind) {
                        ArrayList<Integer>a=(ArrayList<Integer>) newsKind;

                        Log.d("arraylist news:",Integer.toString(a.size()));
//                        for(int i=0;i<a.size();i++)
//                        {
//                            removeView(a.get(i),i);
//                        }
                        removeView(0,0);
                    }
                });
    }

}