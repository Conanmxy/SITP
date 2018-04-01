package com.example.s1.adapter;

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
 * Created by Maxiuyu on 2018/3/31.
 */
public class aBookAdapter extends RecyclerView.Adapter<aBookAdapter.ViewHolder> {

private List<ArrayList<String>> aBookList;

static class ViewHolder extends RecyclerView.ViewHolder{

    CardView scoreView;
    TextView searchNo;//
    TextView storedPlace;//
    TextView bookState;//
    TextView barcode;//
    TextView year;


    public ViewHolder(View view){
        super(view);
        scoreView=(CardView)view;
        searchNo=(TextView)view.findViewById(R.id.search_no);
        storedPlace=(TextView)view.findViewById(R.id.stored_place);
        bookState=(TextView)view.findViewById(R.id.book_state);
        barcode=(TextView)view.findViewById(R.id.barcode);
        year=(TextView)view.findViewById(R.id.year);
        Log.d("scoreAdapter","msg2");

    }

}

    public aBookAdapter(List<ArrayList<String>> aBookList){
        this.aBookList=aBookList;
        Log.d("scoreAdapter","msg");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater
                .from(parent.getContext()).inflate(R.layout.abook_info_item,parent,false);
        Log.d("scoreAdapter","msg1");

        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ArrayList<String>current=aBookList.get(position);
            holder.searchNo.setText(current.get(0));
            holder.storedPlace.setText(current.get(3));
            holder.bookState.setText(current.get(4));
            holder.barcode.setText(current.get(2));
            holder.year.setText(current.get(1));
    }

    @Override
    public int getItemCount(){

        return aBookList.size();
    }
}

