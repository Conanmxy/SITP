package com.example.s1.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s1.LoginActivity;
import com.example.s1.R;
import com.example.s1.scheduleActivity.MyTaskActivity;
import com.example.s1.scheduleActivity.QueryBookActivity;
import com.example.s1.scheduleActivity.QueryScoreActivity;
import com.example.s1.scheduleActivity.ScoreActivity;
import com.example.s1.scheduleActivity.SyllabusActivity;
import com.example.s1.test.TestMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences pref=getActivity().getSharedPreferences("TJuser", Context.MODE_PRIVATE);
        final boolean isIn=pref.getBoolean("isIn",false);
        //查询绩点
        CardView queryScore=(CardView)getActivity().findViewById(R.id.query_score);
        queryScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isIn)
                {
                    Intent intent=new Intent(getActivity(), ScoreActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

      //查询课表
        CardView querySyllabus=(CardView)getActivity().findViewById(R.id.query_syllabus);
        querySyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIn)
                {
                    Intent intent=new Intent(getActivity(), SyllabusActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //查询图书
        CardView queryBook=(CardView)getActivity().findViewById(R.id.query_book);
        queryBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), QueryBookActivity.class);
                //Intent intent=new Intent(getActivity(), TestMainActivity.class);
                startActivity(intent);
            }
        });

        //查询我的近期任务
        CardView queryTask=(CardView)getActivity().findViewById(R.id.query_task);
        queryTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), MyTaskActivity.class);
                startActivity(intent);
            }
        });

    }
}
