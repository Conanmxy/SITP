package com.example.s1.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s1.R;
import com.example.s1.scheduleActivity.QueryScoreActivity;
import com.example.s1.scheduleActivity.ScoreActivity;
import com.example.s1.scheduleActivity.SyllabusActivity;

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

        CardView queryScore=(CardView)getActivity().findViewById(R.id.query_score);
        queryScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), QueryScoreActivity.class);
                startActivity(intent);
            }
        });


        CardView querySyllabus=(CardView)getActivity().findViewById(R.id.query_syllabus);
        querySyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), SyllabusActivity.class);
                startActivity(intent);
            }
        });

    }
}
