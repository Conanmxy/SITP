package com.example.s1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.s1.fragments.DiaryFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */
public class MyPagerStateAdapter extends FragmentStatePagerAdapter {
    private String[]mTitles=new String[]{"任务","新闻","日记","休息"};
    private List<Fragment>fragmentList;
    private Fragment mCurrentFragment;
    private DiaryFragment diaryFragment;

    public MyPagerStateAdapter(FragmentManager fragmentManager,List<Fragment>list){
        super(fragmentManager);
        fragmentList=list;
    }
    @Override
    public Fragment getItem(int position) {
       return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


    @Override
    public CharSequence getPageTitle(int position){
        return mTitles[position];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
