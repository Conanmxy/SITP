package com.example.s1.scheduleActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.adapter.ScoreAdapter;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ArrayList<ArrayList<String> >scoreList;
    ScoreAdapter adapter;
    RecyclerView recyclerView;
    TextView totalScore;
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

        final String userName=getIntent().getStringExtra("sUserName");
        final String password=getIntent().getStringExtra("sPassword");
        Log.d("username",userName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("scoreActi","msg2");

                scoreList=MyOkHttp.loginXuanke(userName,password);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String temp=scoreList.get(1).get(0);
                        String[]strs=temp.split("实修");
                        totalScore.setText(strs[0]+'\n'+strs[1]);
                        scoreList.remove(0);
                        scoreList.remove(0);
                        adapter=new ScoreAdapter(scoreList);
                        recyclerView.setAdapter(adapter);
                        Log.d("scoreActi","msg4");
                    }
                });

            }

        }).start();
        Log.d("scoreActi","msg1");
    }
}
