package com.example.s1.newsActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.s1.R;

import java.util.ArrayList;

public class NewsFirstRunActivity extends AppCompatActivity
implements CompoundButton.OnCheckedChangeListener{

    private ArrayList<String> interests;
    CheckBox cbPolitics;
    CheckBox cbWar;
    CheckBox cbFinance;
    CheckBox cbFun;
    CheckBox cbTechnology;
    CheckBox cbSport;
    Button finished;

    @Override
    public void onCheckedChanged(CompoundButton arg0,boolean arg1)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_first_run);
        initialView();
        initialEvent();

    }

    public void initialView()
    {

        cbPolitics=(CheckBox) findViewById(R.id.politics);
        cbWar=(CheckBox) findViewById(R.id.war);
        cbFinance=(CheckBox) findViewById(R.id.finance);
        cbFun=(CheckBox) findViewById(R.id.fun);
        cbTechnology=(CheckBox) findViewById(R.id.technology);
        cbSport=(CheckBox) findViewById(R.id.sport);
        finished=(Button)findViewById(R.id.finish);
    }
    public void initialEvent()
    {
        cbPolitics.setOnCheckedChangeListener(this);
        cbWar.setOnCheckedChangeListener(this);
        cbFinance.setOnCheckedChangeListener(this);
        cbFun.setOnCheckedChangeListener(this);
        cbTechnology.setOnCheckedChangeListener(this);
        cbSport.setOnCheckedChangeListener(this);
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interests=new ArrayList<String>();
                if(cbPolitics.isChecked())
                    interests.add("政治");
                if(cbWar.isChecked())
                    interests.add("军事");
                if(cbFinance.isChecked())
                 interests.add("财经");
                if(cbFun.isChecked())
                    interests.add("娱乐");
                if(cbTechnology.isChecked())
                    interests.add("科技");
                if(cbSport.isChecked())
                    interests.add("运动");
                Toast.makeText(NewsFirstRunActivity.this,interests.get(0),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
