package com.example.s1.scheduleActivity;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.adapter.ScoreAdapter;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ArrayList<ArrayList<String> >scoreList;
    ScoreAdapter adapter;
    RecyclerView recyclerView;
    TextView totalScore;
    SwipeRefreshLayout scoreSwipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.score_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ScoreActivity.this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        totalScore=(TextView)findViewById(R.id.total_score);
        scoreSwipe=(SwipeRefreshLayout)findViewById(R.id.score_swipe);

        //获取缓存的用户信息
        SharedPreferences userPrefer=getSharedPreferences("TJuser",MODE_PRIVATE);
        final String userName=userPrefer.getString("username","");
        final String password=userPrefer.getString("password","");

        SharedPreferences prefs1=getSharedPreferences("scoreList", MODE_PRIVATE);
        String suserName=prefs1.getString("susername","s");
        if(suserName.equals(userName))//加载缓存
        {
            ArrayList<ArrayList<String> >localScore=new ArrayList<>();

            int i=prefs1.getInt("scoreListSizei",0);

            for(int m=0;m<i;m++)
            {
                ArrayList<String>localt=new ArrayList<>();
                int j=prefs1.getInt("scoreJofI"+m,9);
                for(int n=0;n<j;n++)
                {
                    localt.add(prefs1.getString(("scoreList"+m+n),""));
                    System.out.println("local"+prefs1.getString(("scoreList"+m+n),""));
                }
                localScore.add(localt);
            }


            String temp=localScore.get(1).get(0);
            String[]strs=temp.split("实修");
            totalScore.setText(strs[0]+'\n'+strs[1]);
            localScore.remove(0);
            localScore.remove(0);
            adapter=new ScoreAdapter(localScore);
            recyclerView.setAdapter(adapter);

        }
        else
        {
            showScoreOnlie(userName,password);
        }


//        scoreSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                showScoreOnlie(userName,password);
//            }
//        });

    }

    public void showScoreOnlie(final String userName,final String password)
    {
        Toast.makeText(ScoreActivity.this,"稍等片刻...",Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d("scoreActi","msg2");

                scoreList=MyOkHttp.loginXuanke(userName,password);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(scoreList==null)
                        {
                            Toast.makeText(ScoreActivity.this, "网络开小差了..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //尝试使用缓存
                            SharedPreferences prefs=getSharedPreferences("scoreList",MODE_PRIVATE);
                            SharedPreferences.Editor editors=prefs.edit();
                            for(int i=0;i<scoreList.size();i++)
                            {
                                for(int j=0;j<scoreList.get(i).size();j++)
                                {
                                    editors.putString("scoreList"+i+j,scoreList.get(i).get(j));
                                }
                                editors.putInt("scoreJofI"+i,scoreList.get(i).size());
                            }
                            editors.putInt("scoreListSizei",scoreList.size());
//                            editors.putInt("scoreListSizej",scoreList.get(5).size());
//                            System.out.println("ssjin"+scoreList.get(5).size());
                            editors.putString("susername",userName);
                            editors.apply();



                            String temp=scoreList.get(1).get(0);
                            String[]strs=temp.split("实修");
                            totalScore.setText(strs[0]+'\n'+strs[1]);
                            scoreList.remove(0);
                            scoreList.remove(0);
                            adapter=new ScoreAdapter(scoreList);
                            recyclerView.setAdapter(adapter);
                            // Log.d("scoreActi","msg4");
                        }

                    }
                });

            }

        }).start();
        //Log.d("scoreActi","msg1");



    }
}
