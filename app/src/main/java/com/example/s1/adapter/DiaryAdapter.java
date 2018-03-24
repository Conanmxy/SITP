package com.example.s1.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s1.DiaryActivity.DiaryDetailActivity;
import com.example.s1.R;
import com.example.s1.entity.DiaryText;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private List<DiaryText> mDiaryList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View diaryView;
        TextView diaryItem;
        TextView diaryDate;
        public ViewHolder(View view){
            super(view);
            diaryView=view;
            diaryItem=(TextView)view.findViewById(R.id.diary_item);
            diaryDate=(TextView)view.findViewById(R.id.diary_date);
        }

    }

    public DiaryAdapter(List<DiaryText>diaryList){
        mDiaryList=diaryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater
                .from(parent.getContext()).inflate(R.layout.diary_tiem,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.diaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                DiaryText diaryText=mDiaryList.get(position);
                String selectDiaryText=diaryText.getDiaryText();
                Intent intent=new Intent(v.getContext(), DiaryDetailActivity.class);
                intent.putExtra("existed_text",selectDiaryText);
                intent.putExtra("current_title","日记详情");
                intent.putExtra("current_id",diaryText.getId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        DiaryText diaryText=mDiaryList.get(position);
        holder.diaryItem.setText(diaryText.getDiaryText());

        holder.diaryDate.setText(diaryText.getDate());
    }

    @Override
    public int getItemCount(){
        return mDiaryList.size();
    }
}
