package com.example.s1.fragments;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;


import com.example.s1.R;
import com.example.s1.adapter.DiaryAdapter;
import com.example.s1.entity.DiaryText;
import com.example.s1.rxjava.RxBus2;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/17.
 */
public class DiaryFragment extends Fragment {

    private  List<DiaryText>diaryTextList=new ArrayList<>();
    private DiaryAdapter adapter;
    private RecyclerView recyclerView;
    private int resId;
    private LayoutAnimationController animationController;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.diary_layout,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDiaryText();//初始化日记数据
        initialFragmentView();
        refresh();
        register();
    }


    public void initialFragmentView()
    {
        recyclerView=(RecyclerView)getActivity().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DiaryAdapter(diaryTextList);
        recyclerView.setAdapter(adapter);
        resId=R.anim.animation_slide_right;
        animationController= AnimationUtils.loadLayoutAnimation(getActivity(),resId);
        recyclerView.setLayoutAnimation(animationController);
    }
    private void initDiaryText()
    {
        LitePal.getDatabase();
    }

    //刷新日记显示界面
    public void refresh()
    {
        diaryTextList= DataSupport.order("id desc").find(DiaryText.class);
        adapter=new DiaryAdapter(diaryTextList);
        recyclerView.setLayoutAnimation(animationController);
        recyclerView.setAdapter(adapter);
    }

    //注册RxBus
    public void register(){
        //rxbus
        RxBus2.getDefault().toObservable(DiaryText.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<DiaryText>() {
                    @Override
                    public void call(DiaryText diaryText) {
                        refresh();

                    }
                });
    }

}
