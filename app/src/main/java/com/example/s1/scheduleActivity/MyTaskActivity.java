package com.example.s1.scheduleActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.s1.scheduleActivity.EditScheduleActivity;
import com.example.s1.R;
import com.example.s1.calendarviewdemo.CalendarView;
import com.example.s1.entity.ChildModel;
import com.example.s1.adapter.ContactsInfoAdapter;
import com.example.s1.calendarviewdemo.MyDatabaseHelper;
import com.example.s1.rxjava.RxBus2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MyTaskActivity extends AppCompatActivity {

    private CalendarView calendar;
    private ImageButton calendarLeft;
    private TextView calendarCenter;
    private ImageButton calendarRight;
    private SimpleDateFormat format;
    private ExpandableListView mListView;
    private List<String> group;           //组列表
    private List<List<ChildModel>> child;     //子列表
    private ContactsInfoAdapter adapter;  //数据适配器
    private List<Integer> spotList;

    private int groupPosition2;
    private static final int THRESHOLD_Y_LIST_VIEW = 20;
    private boolean isSvToBottom = false;
    private float mLastY;

    private MyDatabaseHelper dbHelper;
    SQLiteDatabase db_schedule;
    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);
        init();
        setCalendar();
        initializeData();
        register();
    }


    private void initializeData() {
        dbHelper = new MyDatabaseHelper(this,"schedule.db",null,1);
        db_schedule=dbHelper.getWritableDatabase();
        group.clear();
        child.clear();
        List<ChildModel> childitem = getChildModels(groupPosition2);
        group.add(groupPosition2 + "");    //日期
        child.add(childitem);
        int maxnum= calendar.getMaxDayNum();
        int groupPositiona=groupPosition2+1;
        if(groupPositiona<=maxnum) {
            List<ChildModel> childitem1 = getChildModels(groupPositiona);
            group.add(groupPositiona + "");    //日期
            child.add(childitem1);
        }
        int groupPositionb=groupPosition2+2;
        if(groupPositionb<=maxnum) {
            List<ChildModel> childitem2 = getChildModels(groupPositionb);
            group.add(groupPositionb + "");    //日期
            child.add(childitem2);
        }


        //设置默认全部打开
        for (int i = 0; i < group.size(); i++) {
            mListView.expandGroup(i);
        }
        db_schedule.close();



        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                if(child.get(groupPosition).size() == childPosition + 1){
                    int position1=Integer.valueOf(group.get(groupPosition));
                    List<ChildModel> list = child.get(groupPosition);
                    ChildModel model = list.get(childPosition);
                    String content1=model.getContent();
                    String time1=model.getDate();
                    Intent intent=new Intent(view.getContext(), EditScheduleActivity.class);
                    intent.putExtra("date",time1);
                    intent.putExtra("current_title","新建");
                    view.getContext().startActivity(intent);
                    spotList.add(Integer.valueOf(group.get(groupPosition)) - 1 + calendar.getcurStartIndex());
                    calendar.change(spotList);
                    //Toast.makeText(getActivity(), "添加了一个日程" , Toast.LENGTH_SHORT).show();
                }
                else {
                    int position1=Integer.valueOf(group.get(groupPosition));
                    List<ChildModel> list = child.get(groupPosition);
                    ChildModel model = list.get(childPosition);
                    String content1=model.getContent();
                    String time1=model.getTime();
                    int Id=model.getId();
                    Intent intent1=new Intent(view.getContext(), EditScheduleActivity.class);
                    intent1.putExtra("existed_text",content1);
                    intent1.putExtra("date",time1);
                    intent1.putExtra("current_title","编辑");
                    intent1.putExtra("current_id",Id);
                    view.getContext().startActivity(intent1);
                }
                return false;
            }

        });
        adapter.notifyDataSetChanged();
        mListView.post(new Runnable() { //ListView数据没有更改之前，setselection()方法调用效果一切正常；而填充数据更改之后，同样的代码片段却莫名其妙无效了。
            @Override
            public void run() {
                mListView.setSelectedGroup(calendar.getNowDate() - 1);  //设置初始listview的显示位置，为本日
            }
        });
    }


    /**
     * 构造child里面的数据
     *
     * @param i
     * @return
     */
    private List<ChildModel> getChildModels(int i) {
        List<ChildModel> childitem = new ArrayList<ChildModel>();
        ChildModel model;
        String date2;
        String str;
        int n;
        Date datea= new Date();
        String[] yaa = getStrings(format.format(datea));
        if(i<10)
            date2=yaa[0]+"-"+yaa[1]+"-0"+String.valueOf(i);
        else
            date2=yaa[0]+"-"+yaa[1]+"-"+String.valueOf(i);
        Cursor cur=db_schedule.rawQuery("select * from schedule where date = ?",new String[] {date2});
        if(cur.moveToFirst()){
            do {
                model = new ChildModel();
                str="";
                n=cur.getInt(0);
                str +=cur.getString(2);
                str +=cur.getString(3);
                str +="\n";
                i=i+1;
                model.setId(n);
                model.setDate(cur.getString(1));
                model.setTime(cur.getString(2));
                model.setContent(cur.getString(3));
                childitem.add(model);
            }while (cur.moveToNext());
        }

        //在末尾加入新建日程条目
        //if(groupPosition2!=i) {
        model = new ChildModel();
        model.setContent("新建日程");
        model.setDate(date2);
        childitem.add(model);
        // }
        return childitem;
    }

    private void init() {
        spotList = new ArrayList<Integer>();
        group = new ArrayList<String>();
        child = new ArrayList<List<ChildModel>>();
        format = new SimpleDateFormat("yyyy-MM-dd");
        //获取日历控件对象
        calendar = (CalendarView) findViewById(R.id.calendar);
        mListView = (ExpandableListView) findViewById(R.id.list);
        //mScrollView = (ScrollView) getActivity().findViewById(R.id.scroll);
        calendar.setSelectMore(false); //单选

        calendarLeft = (ImageButton) findViewById(R.id.calendarLeft);
        calendarCenter = (TextView) findViewById(R.id.calendarCenter);
        calendarRight = (ImageButton) findViewById(R.id.calendarRight);
        adapter = new ContactsInfoAdapter(group, child, this, getStrings(calendar.getYearAndmonthAndDate()), calendar.getNowWeek());
        mListView.setAdapter(adapter);
    }
    private void setCalendar() {
        try {
            //设置日历日期
            Date date = format.parse("2015-01-01");
            calendar.setCalendarData(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        calendar.change(num, true);

        Date date1= new Date();
        setHeadDate(format.format(date1));
        calendarLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //点击上一月 同样返回年月
                String leftYearAndmonth = calendar.clickLeftMonth();
                clickCalendar(leftYearAndmonth);
            }
        });

        calendarRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //点击下一月
                String rightYearAndmonth = calendar.clickRightMonth();
                clickCalendar(rightYearAndmonth);
            }
        });

        //设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {

            @Override
            public void OnItemClick(Date selectedStartDate,
                                    Date selectedEndDate, Date downDate) {
                setHeadDate(format.format(downDate));   //头上的日期显示更换
                String[] ya = getStrings(format.format(downDate));  //获取按下的日期，目的是获取“日”，与mListView的下标对比，得到要显示的位置
                String[] nowDate = getStrings(calendar.getYearAndmonthAndDate());
                if(ya[1].equals(nowDate[1])){   //用于限制本页显示灰色的上一月和下一月，点击后不执行以下代码
                    mListView.setSelectedGroup(Integer.valueOf(ya[2]) - 1); //日期比下标大1，因此这里减1，可定位到正确位置
                }
            }
        });
    }

    private void clickCalendar(String leftYearAndmonth) {
        initializeData();
        setHeadDate(leftYearAndmonth);
        spotList.clear();
        calendar.change(spotList);
    }

    //获取日历中年月 ya[0]为年，ya[1]为月,ya[1]为日
    private void setHeadDate(String date) {
        String[] ya = getStrings(date);
        calendarCenter.setText(ya[0] + "-" + ya[1] + "-" + ya[2]);
        //getChildModels(1);
        groupPosition2=Integer.valueOf(ya[2]);
        initializeData();
    }

    private String[] getStrings(String date) {
        return date.split("-");
    }

    //注册RxBus
    public void register(){
        //rxbus
        RxBus2.getDefault().toObservable(ChildModel.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<ChildModel>() {
                    @Override
                    public void call(ChildModel diaryText) {
                        //init();
                        //setCalendar();
                        initializeData();
                    }
                });
    }
}
