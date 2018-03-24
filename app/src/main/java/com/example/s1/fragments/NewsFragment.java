package com.example.s1.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s1.entity.DiaryText;
import com.example.s1.newsActivity.NewsFirstRunActivity;
import com.example.s1.entity.News;
import com.example.s1.R;
import com.example.s1.rxjava.RxBus2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/10/17.
 */
public class NewsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.news_layout,container,false);
    }

    private SwipeRefreshLayout swipeRefresh;

    private List<News> NewsList=new ArrayList<>();
    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        register();
        if(isFirstRun)//第一次进入这个app
        {
            editor.putBoolean("isFirstRun",false);
            editor.apply();
            Intent intent=new Intent(view.getContext(), NewsFirstRunActivity.class);
            startActivity(intent);
        }
        else {//不是第一次进入这个app
            sendRequestWithHttpURLConnection();

            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
            swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d("刷新：","abcdedfg");
                    refeshNews();
                }
            });
        }


    }


    //注册RxBus
    public void register(){
        //rxbus
        RxBus2.getDefault().toObservable(String.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String newsKind) {
                        Log.d("refresh news:",newsKind);
                        if(newsKind.equals("政治"))
                        {
                            refeshNews();
                        }


                    }
                });
    }

    private void refeshNews()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(2000);
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequestWithHttpURLConnection();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void sendRequestWithHttpURLConnection()
    {
        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try
                {
                    URL url=new URL("http://www.guancha.cn/");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    //读取输入流
                    reader=new BufferedReader(new InputStreamReader(in));
                    if(reader==null)
                        Log.d("reader: ","null");
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null) {
                        response.append(line);
                    }
                    //showResponse(response.toString());
                    ArrayList<String> results = new ArrayList<String>();
                    results=RegexString(response.toString(),
                            "<h4class=\"module-title\"><ahref=\"(.*?)\"target=\"_blank\">(.*?)</a></h4>",
                            "<divclass=\"content-headline\"><h3><ahref=\"(.*?)\"target=\"_blank\">(.*?)</a></h3>");
                    showResponse(results);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(reader!=null)
                    {
                        try{
                            reader.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null)
                    {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    private void showResponse(final ArrayList<String> results)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //responseText.setText(response);
                RecyclerView newsTitleRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recycler_view_news);
                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                newsTitleRecyclerView.setLayoutManager(layoutManager);
                for(int i=0;i<results.size();i=i+2)
                {
                    News news=new News();
                    news.setTitle(results.get(i+1));
                    news.setLink(results.get(i));
                    NewsList.add(news);
                }
                NewsAdapter adapter=new NewsAdapter(NewsList);
                Log.d("adapter:",NewsList.get(0).getTitle());
                newsTitleRecyclerView.setAdapter(adapter);
            }
        });
    }

    public static ArrayList<String> RegexString(String targetStr, String patternStr, String patternStr_headline)
    {
        ArrayList<String> results = new ArrayList<String>();
        //去除html源码中所有的空格符换行符
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(targetStr);
        targetStr = m.replaceAll("");

        //先获取头条的链接和标题
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
        // 相当于埋好了陷阱匹配的地方就会掉下去
        Pattern pattern_headline=Pattern.compile(patternStr_headline);
        Matcher matcher_headline=pattern_headline.matcher(targetStr);
        if(matcher_headline.find())
        {
            String result_link_headline=matcher_headline.group(1);
            String result_href_headline="http://m.guancha.cn"+result_link_headline;
            results.add(result_href_headline);

            String result_title_headline=matcher_headline.group(2);
            result_title_headline+="\n";
            results.add(result_title_headline);
        }

        //再获取非头条的链接和标题
        Pattern pattern = Pattern.compile(patternStr);
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(targetStr);
        Boolean isFind = matcher.find();
        // 如果找到了
        while (isFind)
        {
            //加入列表
            String result_link=matcher.group(1);
            String result_href="http://m.guancha.cn"+result_link;
            results.add(result_href);

            String result_title=matcher.group(2);
            result_title+="\n";
            results.add(result_title);
            isFind=matcher.find();
        }
        return results;
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
    {
        private List<News> mNewsList;
        class ViewHolder extends RecyclerView.ViewHolder
        {
            View newsView;
            TextView newsTitleText;
            public ViewHolder(View view)
            {
                super(view);
                newsView=view;
                newsTitleText=(TextView)view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News>newsList)
        {
            mNewsList=newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item, parent, false);
            final ViewHolder holder=new ViewHolder(view);
            holder.newsView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position=holder.getAdapterPosition();
                    News news=mNewsList.get(position);
                    //String url="http://www.guancha.cn/";
                    String url=news.getLink();
                    Intent intent=new Intent(v.getContext(),com.example.s1.newsActivity.NewsContentActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
