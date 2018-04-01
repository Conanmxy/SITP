package com.example.s1.scheduleActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    String id_temp;
    String content;
	Boolean flag_threadend=false;
    long cha;
    public int k;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        //获取第几次登陆
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("FirstRun", true);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        //获取缓存的用户信息
        SharedPreferences userPrefer=getSharedPreferences("TJuser",MODE_PRIVATE);

        boolean isIn=userPrefer.getBoolean("isIn",false);
        System.out.println("isIn:"+isIn);
        userName=userPrefer.getString("username","");
        password=userPrefer.getString("password","");

//        userName="1552237";
//        password="113803";

        if(isFirstRun)
        {
            editor2.putBoolean("FirstRun",false);
            editor2.apply();
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

    private void showCourses_online( final String userName, final String password)//联网获取课程
    {
        judge_dorsweek();
        flag_threadend=false;
        new Thread(new Runnable(){
            @Override
            public void run() {

                CourseList = MyOkHttp.login4m3(userName,password);

                k=0;
                final SharedPreferences.Editor editor=getSharedPreferences("course",MODE_PRIVATE).edit();//将课程信息缓存到本地
                editor.putInt("size",CourseList.size());
                editor.apply();

                runOnUiThread(new Runnable() 
				{
                    @Override
                    public void run()
                    {
                        Context ctx = getBaseContext();
                        Resources res = getResources();
                        for (int i = 0; i < CourseList.size(); i++)
                        {
                            String CourseName = CourseList.get(i).get(2);
                            String CourseArrangement = CourseList.get(i).get(8);

                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                            Matcher m = p.matcher(CourseArrangement);
                            CourseArrangement = m.replaceAll("");

                            p = Pattern.compile("星期(.*?)([0-9]+)-[0-9]+(.*?)([\\u4e00-\\u9fa5]*)(.*?)([\\u4e00-\\u9fa5]+[A-Z]*[0-9]*)");
                            m = p.matcher(CourseArrangement);
                            Boolean isFind = m.find();
                            while (isFind)
                            {
                                String weekday = m.group(1);
                                String courseorder = m.group(2);
                                //int courseorder=Integer.parseInt(m.group(2));
                                String dors_week = m.group(4);
                                String courseplace = m.group(6);
                                switch (weekday)
                                {
                                    case "一":
                                        weekday = "mon";
                                        break;
                                    case "二":
                                        weekday = "tus";
                                        break;
                                    case "三":
                                        weekday = "wed";
                                        break;
                                    case "四":
                                        weekday = "thu";
                                        break;
                                    case "五":
                                        weekday = "fri";
                                        break;
                                    case "六":
                                        weekday = "sat";
                                        break;
                                    case "日":
                                        weekday = "sun";
                                        break;
                                }
                                id_temp = weekday + "_" + courseorder;
                                content = CourseName + "@" + courseplace;
                                int id = res.getIdentifier(id_temp, "id", ctx.getPackageName());
                                TextView Course = (TextView) findViewById(id);

                                editor.putInt("id"+k, id);
                                editor.putString("content"+k, content);
                                editor.putString("dors_week"+k, dors_week);
                                k=k+1;
                                editor.apply();

                                if (!dors_week.equals("双") && !dors_week.equals("单"))
                                {
                                    Course.setText(content);
                                }
                                if (dors_week.equals("双") && cha % 2 == 0) {
                                    Course.setText(content);
                                } else if (dors_week.equals("单") && cha % 2 == 1) {
                                    Course.setText(content);
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
        Log.d("Syllabusssssssss",String.valueOf(flag_threadend));
        Log.d("Syllabusssssssss",String.valueOf(pref.getInt("count",0)));
        for(int i=0;i<pref.getInt("count",0);i++) {
            int id = pref.getInt("id" + i, 0);
            String content = pref.getString("content" + i, "");
            String dors_week = pref.getString("dors_week" + i, "");


            TextView Course = (TextView) findViewById(id);

            if (!dors_week.equals("双") && !dors_week.equals("单"))
            {
                Course.setText(content);
            }
            if (dors_week.equals("双") && cha % 2 == 0) {
                Course.setText(content);
            } else if (dors_week.equals("单") && cha % 2 == 1) {
                Course.setText(content);
            }
        }
    }
 }

