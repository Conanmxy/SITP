package com.example.s1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s1.R;

import com.example.s1.entity.ChildModel;
import com.example.s1.calendarviewdemo.DateEnum;

import java.util.List;

/**
 */
public class ContactsInfoAdapter extends BaseExpandableListAdapter {
    private List<String> group;           //组列表
    private List<List<ChildModel>> child;     //子列表
    private Context context;
    private LayoutInflater mInflater;
    private int nowWeek;   //当前星期几
    private String[] nowDate;    //存储 年 月 日，0代表年，1代表月，2代表日

    public ContactsInfoAdapter(List<String> group, List<List<ChildModel>> child, Context context,String[] nowDate, int nowWeek) {
        this.group = group;
        this.child = child;
        this.context = context;
        this.nowWeek = nowWeek;
        this.nowDate = nowDate;
    }

    public ContactsInfoAdapter(List<List<ChildModel>> child){
        child=child;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    class GroupViewHolder{
        TextView data;
        TextView week;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        String string = group.get(groupPosition);
        GroupViewHolder holder ;
        if(view == null){
            holder = new GroupViewHolder();
            mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.group_item_layout, null);
            holder.data = (TextView)view.findViewById(R.id.data);
            holder.week = (TextView)view.findViewById(R.id.week);
            view.setTag(holder);
        }else {
            holder = (GroupViewHolder)view.getTag();
        }
//        if((groupPosition + 1) == Integer.valueOf(nowDate[2])){
//           holder.week.setText("今天，" + "星期" + transitionWeek(nowWeek));
//        }else if((groupPosition + 2) == Integer.valueOf(nowDate[2])){
//           holder.week.setText("昨天，" + "星期" + transitionWeek(nowWeek - 1));
//        }else if((groupPosition) == Integer.valueOf(nowDate[2])){
//           holder.week.setText("明天，" + "星期" + transitionWeek(nowWeek + 1));
//        }else {
//            holder.week.setText("");
//        }
        holder.data.setText(nowDate[1] + "月" +string + "日");
        view.setClickable(false);    //设置不可闭合


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder ;
        if(view == null){
            holder = new ChildViewHolder();
            mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.child_item_layout, null);
            holder.head = (ImageView)view.findViewById(R.id.child_head_image);
            holder.time = (TextView)view.findViewById(R.id.time);
            holder.content = (TextView)view.findViewById(R.id.content);
            view.setTag(holder);
        }else {
            holder = (ChildViewHolder)view.getTag();
        }
        List<ChildModel> list = child.get(groupPosition);
//        if(list.size() > 0){
        ChildModel model = list.get(childPosition);
        holder.time.setText("");
        holder.content.setText(model.getContent());



//        }
        return view;
    }
    class ChildViewHolder{
        ImageView head;
        TextView time;
        TextView content;
    }

    private String  transitionWeek(int nowWeek){
        String week = "";
        switch (DateEnum.fromInteger(nowWeek)){
            case MON:
                week = DateEnum.MON.getValue();
                break;
            case TUE:
                week = DateEnum.TUE.getValue();
                break;
            case WED:
                week = DateEnum.WED.getValue();
                break;
            case THU:
                week = DateEnum.THU.getValue();
                break;
            case FRI:
                week = DateEnum.FRI.getValue();
                break;
            case SAT:
                week = DateEnum.SAT.getValue();
                break;
            case SUN:
                week = DateEnum.SUN.getValue();
                break;
        }
        return week;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
