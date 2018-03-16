package com.example.s1.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.s1.R;
import com.example.s1.test.TestMainActivity;

/**
 * Created by Administrator on 2017/10/17.
 */
public class ScheduleFragment extends Fragment {
    Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.schedule_layout,container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        button=(Button)getActivity().findViewById(R.id.schedule_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TestMainActivity.class);
                startActivity(intent);

            }
        });
    }
}
