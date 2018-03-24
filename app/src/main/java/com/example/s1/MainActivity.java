package com.example.s1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.s1.DiaryActivity.WriteDiaryActivity;
import com.example.s1.Utils.PopWindow;
import com.example.s1.adapter.MyPagerStateAdapter;
import com.example.s1.fragments.ControlFragment;
import com.example.s1.fragments.DiaryFragment;
import com.example.s1.fragments.NewsFragment;
import com.example.s1.fragments.ScheduleFragment;
import com.example.s1.newsActivity.NewsFirstRunActivity;
import com.example.s1.rxjava.RxBus2;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private TabLayout.Tab diary;
    private TabLayout.Tab schedule;
    private TabLayout.Tab control;
    private TabLayout.Tab news;
    DrawerLayout mDrawerLayout;
    DiaryFragment diaryFragment;
    ScheduleFragment scheduleFragment;
    NewsFragment newsFragment;
    ControlFragment controlFragment;
    MyPagerStateAdapter myPagerStateAdapter;
    List<Fragment> fragmentList;
    NavigationView navView;
    private PopWindow popWindow;
    ArrayList<Integer>delMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fragmentList=new ArrayList<>();
        fragmentList.add(scheduleFragment=new ScheduleFragment());
        fragmentList.add(newsFragment=new NewsFragment());
        fragmentList.add(diaryFragment=new DiaryFragment());
        fragmentList.add(controlFragment=new ControlFragment());
        initViews();
        initEvents();
        register();
    }

    private void initEvents(){
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab==mTablayout.getTabAt(0)){
                    schedule.setIcon(R.mipmap.ic_menu_anniversary);
                }else if(tab==mTablayout.getTabAt(1)){
                    news.setIcon(R.mipmap.ic_menu_news_48);
                }else if(tab==mTablayout.getTabAt(2)){
                    diary.setIcon(R.mipmap.ic_menu_diary);
                }else if(tab==mTablayout.getTabAt(3)){
                    control.setIcon(R.mipmap.ic_menu_welfare_black);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if(tab==mTablayout.getTabAt(0)){
                    schedule.setIcon(R.mipmap.ic_menu_anniversary_dark);
                    mViewPager.setCurrentItem(0);
                }else if(tab==mTablayout.getTabAt(1)){
                    news.setIcon(R.mipmap.news_48_light_green);
                    mViewPager.setCurrentItem(1);
                }else if(tab==mTablayout.getTabAt(2)){
                    diary.setIcon(R.mipmap.ic_menu_diary_dark);
                    mViewPager.setCurrentItem(2);
                }else if(tab==mTablayout.getTabAt(3)){
                    control.setIcon(R.mipmap.ic_menu_welfare_dark);
                    mViewPager.setCurrentItem(3);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.setting)
                {
                    Intent intent=new Intent(MainActivity.this, NewsFirstRunActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }

    private void initViews(){
        mTablayout=(TabLayout)findViewById(R.id.tabLayout);
        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        myPagerStateAdapter=new MyPagerStateAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(myPagerStateAdapter);
        navView=(NavigationView)findViewById(R.id.nav_view);
        delMenu=new ArrayList<Integer>();

        //处理toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu_64px);
        }
        navView.setCheckedItem(R.id.nav_call);

        mTablayout.setupWithViewPager(mViewPager);

        schedule=mTablayout.getTabAt(0);
        news=mTablayout.getTabAt(1);
        diary=mTablayout.getTabAt(2);
        control=mTablayout.getTabAt(3);

        schedule.setIcon(R.mipmap.ic_menu_anniversary_dark);
        news.setIcon(R.mipmap.ic_menu_welfare_dark);
        diary.setIcon(R.mipmap.ic_menu_diary_dark);
        control.setIcon(R.mipmap.ic_menu_welfare_dark);

    }

    //顶部菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){//有待处理
            case R.id.add:
                if(mViewPager.getCurrentItem()==1)
                {
                    Log.d("before","before");
                    popWindow=new PopWindow(this);
                    for(int i=0;i<delMenu.size();i++)
                    {
                        popWindow.removeView(delMenu.get(i),i);
                    }
                    Log.d("later","later");

                  //  popWindow.register();
                    popWindow.showPopupWindow(findViewById(R.id.add));
                }
                else
                {
                    Intent intent=new Intent(MainActivity.this,WriteDiaryActivity.class);
                    intent.putExtra("current_title","新建");
                    startActivity(intent);
                }

                break;

            case android.R.id.home:
                Log.d("open","打开");
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;


            default:
                break;
        }
        return true;
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
                        delMenu=(ArrayList<Integer>) newsKind;

                        Log.d("arraylist news:",Integer.toString(delMenu.size()));

                       // removeView(0,0);
                    }
                });
    }

}
