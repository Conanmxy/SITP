package com.example.s1.scheduleActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyllabusActivity extends AppCompatActivity {

    ArrayList<ArrayList<String> >CourseList;
    String week_idtemp;
    String content;
    Boolean flag_threadend=false;
    long cha;
    public int k;
    String userName;
    String password;
    SharedPreferences.Editor editor;
    String []Course_backgroundcolor=new String[]{"#4EF037","#2FE1D6","#ED5485","#44D9E6","#1c92d2"
            ,"#F677F7","#ED93CB"};
    //TextView Course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        //获取第几次登录
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("FirstRun", true);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        //获取缓存的用户信息
        SharedPreferences userPrefer=getSharedPreferences("TJuser",MODE_PRIVATE);

        // boolean isIn=userPrefer.getBoolean("isIn",false);
        // System.out.println("isIn:"+isIn);



//        userName="1552237";/
//        password="113803";

        if(isFirstRun)
        {
            editor2.putBoolean("FirstRun",false);
            editor2.apply();
            userName=userPrefer.getString("username","");
            password=userPrefer.getString("password","");
            showCourses_online(userName,password);
        }
        else showCourses_local();
    }

    private void judge_dorsweek()
    {
        Date curdate = new Date(System.currentTimeMillis());
        try {
            String s2 = "2018-3-5";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d2 = df.parse(s2);
            cha = (curdate.getTime() - d2.getTime()) / (60 * 60 * 1000 * 24) / 7 + 1;
            TextView t = (TextView) findViewById(R.id.txt1_gird);
            t.setText("第" + String.valueOf(cha) + "周");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

    }
    private void showCourses_online( final String userName, final String password)//联网获取课程
    {
        judge_dorsweek();
        flag_threadend=false;
        new Thread(new Runnable(){
            @Override
            public void run() {

                CourseList = MyOkHttp.login4m3(userName,password);

                k=0;
                editor=getSharedPreferences("course",MODE_PRIVATE).edit();//将课程信息缓存到本地
                editor.putInt("size",CourseList.size());
                editor.apply();

                runOnUiThread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        for (int i = 0; i < CourseList.size(); i++)
                        {
                            String CourseName = CourseList.get(i).get(2);
                            String CourseArrangement = CourseList.get(i).get(8);

                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                            Matcher m = p.matcher(CourseArrangement);
                            CourseArrangement = m.replaceAll("");

                            //CourseArrangement=王继成星期四1-2双[2-16]博楼B310
                            p = Pattern.compile("星期(.*?)([0-9]+)-([0-9]+)(.*?)([\\u4e00-\\u9fa5]*)(.*?)([\\u4e00-\\u9fa5]+[A-Z]*[0-9]*)");
                            m = p.matcher(CourseArrangement);
                            Boolean isFind = m.find();
                            while (isFind)
                            {
                                String weekday =m.group(1);          //星期几
                                int courseorder_begin =Integer.parseInt(m.group(2));//课程开始节次
                                int courseorder_end=Integer.parseInt(m.group(3));    //课程结束节次
                                String dors_week = m.group(5);       //单双周
                                String courseplace = m.group(7);      //上课地点
                                content = CourseName + "@" + courseplace;
                                System.out.println(weekday+dors_week+content);
                                switch (weekday)
                                {
                                    case "一":weekday = "1";break;
                                    case "二":weekday = "2";break;
                                    case "三":weekday = "3";break;
                                    case "四":weekday = "4";break;
                                    case "五":weekday = "5";break;
                                    case "六":weekday = "6";break;
                                    case "日":weekday = "7";break;
                                }
                                week_idtemp = "column_"+weekday;
                                int id = getResources().getIdentifier(week_idtemp, "id", getBaseContext().getPackageName());

                                RelativeLayout relativeLayout=(RelativeLayout) findViewById(id);
                                TextView Course = new TextView(getBaseContext());
                                Course.setLayoutParams(new RelativeLayout.LayoutParams(
                                        getPixelsFromDp(60),getPixelsFromDp((courseorder_end-courseorder_begin+1)*59)
                                ));
                                Course.setText(content);
                                Course.setAlpha(0.9f);
                                Course.setTextColor(Color.WHITE);
                                Course.setTextSize(12);
                                Course.setBackgroundResource(R.drawable.course_text_shap);
                                GradientDrawable myGrad=(GradientDrawable)Course.getBackground();
                                myGrad.setColor(Color.parseColor(Course_backgroundcolor[k%7]));









                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Course.getLayoutParams();
                                params.setMargins(0,
                                        getPixelsFromDp((courseorder_begin-1)*60),
                                        0,0);
                                Course.setLayoutParams(params);




                                editor.putInt("id"+k, id);
                                editor.putString("content"+k, content);
                                editor.putString("dors_week"+k, dors_week);
                                editor.putInt("courseorder_begin"+k,courseorder_begin);
                                editor.putInt("courseorder_end"+k,courseorder_end);
                                editor.apply();

                                //Course.setBackgroundColor(Color.parseColor(Course_backgroundcolor[k%7]));
                                k=k+1;

                                if (!dors_week.equals("双") && !dors_week.equals("单"))
                                {
                                    relativeLayout.addView(Course);
                                }
                                else if (dors_week.equals("双") && cha % 2 == 0) {
                                    relativeLayout.addView(Course);
                                }
                                else if (dors_week.equals("单") && cha % 2 == 1)
                                {
                                    relativeLayout.addView(Course);
                                }
                                isFind = m.find();
                            }
                        }
                        editor.putInt("count",k);
                        editor.apply();
                    }
                });
            }
        }).start();
        flag_threadend=true;
    }

    private void showCourses_local()//课程已被存储在本地
    {
        judge_dorsweek();
        SharedPreferences pref=getSharedPreferences("course",MODE_PRIVATE);
        // Log.d("Syllabusssssssss",String.valueOf(flag_threadend));
        // Log.d("Syllabusssssssss",String.valueOf(pref.getInt("count",0)));
        for(int i=0;i<pref.getInt("count",0);i++)
        {
            int id = pref.getInt("id" + i, 0);
            int courseorder_begin=pref.getInt("courseorder_begin"+i,0);
            int courseorder_end=pref.getInt("courseorder_end"+i,0);
            String content = pref.getString("content" + i, "");
            String dors_week = pref.getString("dors_week" + i, "");

            RelativeLayout relativeLayout=(RelativeLayout) findViewById(id);
            TextView Course = new TextView(getBaseContext());
            Course.setLayoutParams(new RelativeLayout.LayoutParams(
                    getPixelsFromDp(60),getPixelsFromDp((courseorder_end-courseorder_begin+1)*59)
            ));
            Course.setText(content);
            Course.setAlpha(0.9f);
            Course.setTextColor(Color.WHITE);
            Course.setTextSize(12);
            Course.setBackgroundResource(R.drawable.course_text_shap);
            GradientDrawable myGrad=(GradientDrawable)Course.getBackground();
            myGrad.setColor(Color.parseColor(Course_backgroundcolor[i%7]));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Course.getLayoutParams();
            params.setMargins(0,
                    getPixelsFromDp((courseorder_begin-1)*60),
                    0,0);
            Course.setLayoutParams(params);


            if (!dors_week.equals("双") && !dors_week.equals("单"))
            {
                relativeLayout.addView(Course);

            }
            else if (dors_week.equals("双") && cha % 2 == 0)
            {
                relativeLayout.addView(Course);

            }
            else if (dors_week.equals("单") && cha % 2 == 1)
            {
                relativeLayout.addView(Course);

            }
        }
    }
}

