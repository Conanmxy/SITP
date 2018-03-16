package com.example.s1.test;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<View> mViewList;
    private List<String>mTitleList;
    public MyPagerAdapter(List<View>mViewList,List<String>mTitleList){
        this.mViewList=mViewList;
        this.mTitleList=mTitleList;
    }

    @Override
    public int getCount() {
        return mViewList.size();//页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
