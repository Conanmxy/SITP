package com.example.s1.adapter;

/**
 * Created by Administrator on 2018/3/29.
 */

import android.icu.text.LocaleDisplayNames;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.s1.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<ArrayList<String>> scoreList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView scoreView;
        TextView name;//课程名称
        TextView grade;//成绩
        TextView credit;//学分
        TextView gradePoint;//绩点

        public ViewHolder(View view){
            super(view);
            scoreView=(CardView)view;
            name=(TextView)view.findViewById(R.id.name);
            grade=(TextView)view.findViewById(R.id.grade);
            credit=(TextView)view.findViewById(R.id.credit);
            gradePoint=(TextView)view.findViewById(R.id.gradePoint);
           // Log.d("scoreAdapter","msg2");

        }

    }

    public ScoreAdapter(List<ArrayList<String>>scoreList){
        this.scoreList=scoreList;
        //Log.d("scoreAdapter","msg");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater
                .from(parent.getContext()).inflate(R.layout.score_item,parent,false);
        //Log.d("scoreAdapter","msg1");

        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ArrayList<String>currentScore=scoreList.get(position);
        if(currentScore.size()>1)
        {
            holder.name.setText(currentScore.get(1));
            holder.grade.setText(currentScore.get(2));
            holder.credit.setText(currentScore.get(3));
            holder.gradePoint.setText(currentScore.get(4));

        }
        else
        {
           holder.name.setText(currentScore.get(0));
            holder.grade.setText("");
            holder.credit.setText("");
            holder.gradePoint.setText("");
        }

    }

    @Override
    public int getItemCount(){

        return scoreList.size();
    }
}
