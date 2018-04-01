package com.example.s1.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.s1.R;
import com.example.s1.scheduleActivity.BookDetailActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ma XiuYu on 2018/3/30.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private List<String> mBooksList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView booksView;
        TextView booksNameT;
        TextView booksAuthorT;
        TextView booksPublishT;
        TextView booksLanguageT;
        TextView sn;//馆藏副本
        TextView bn;//可借副本
        public ViewHolder(View view){
            super(view);
            Log.d("bs1",R.id.book_name+"");
            booksView=(CardView)view;
            booksNameT=(TextView)view.findViewById(R.id.books_name);
            booksAuthorT=(TextView)view.findViewById(R.id.books_author);
            booksPublishT=(TextView)view.findViewById(R.id.books_publish);
            booksLanguageT=(TextView)view.findViewById(R.id.books_language);
            sn=(TextView)view.findViewById(R.id.stored_number);
            bn=(TextView)view.findViewById(R.id.borrowed_number);
        }

    }

    public BooksAdapter(List<String>BooksList){
        mBooksList=BooksList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater
                .from(parent.getContext()).inflate(R.layout.books_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        Log.d("bs1","m3");
        holder.booksView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                //获取连接上的书号
                String bookId=mBooksList.get(position);
                if(bookId!=null)
                {
                    Pattern p = Pattern.compile("[0-9]{10}");
                    Matcher m = p.matcher(bookId);
                    while(m.find())
                    {
                        bookId=m.group();
                    }
                }

                //获取图书名，这次的数据封装的不好
                String tempStr=mBooksList.get(position);
                Document doc2= Jsoup.parse(tempStr);
                String h3text=doc2.select("h3").text();
                String bookName=h3text.substring(h3text.indexOf("图书")+2);
                //bookName=bookName.split(" ")[0];


                Intent intent=new Intent(v.getContext(), BookDetailActivity.class);
                intent.putExtra("bookId",bookId);
                intent.putExtra("tbdName",bookName);

                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        String  tempStr=mBooksList.get(position);

        //这里要对数据进行处理，分配到相应的view

        Document doc2= Jsoup.parse(tempStr);
        String h3text=doc2.select("h3").text();
        Elements elementsSpan=doc2.select("span");
        String spanText1=elementsSpan.get(0).text();//图书名
        String spanText2=elementsSpan.get(1).text();//图书馆藏可借信息
        String borrowedNum=spanText2.substring(spanText2.indexOf("可借"));
        String storedNum=spanText2.substring(0,spanText2.indexOf("可借"));

        String ptext=doc2.text();
        String phtml=doc2.html();

        //获取作者
        String author="";
        String publish="";
        if(phtml!=null)
        {
            Pattern p = Pattern.compile("</span>\\s*(.*?)<br>");
            Matcher m = p.matcher(phtml);
            while(m.find())
            {
                author=m.group(1);
            }

        }

        System.out.println("ptext:"+ptext);
        String pub1="";

        try {
            pub1=phtml.substring(phtml.indexOf("可借复本"),phtml.indexOf("&nbsp;"));
            publish= pub1.substring(pub1.indexOf("<br>")+4);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        String language=spanText1;
        String bookName=h3text.substring(h3text.indexOf("图书")+2);
        System.out.println("language:"+language);
        System.out.println("bookName:"+bookName);
        System.out.println("author:"+author);
        System.out.println("borrow:"+spanText2);
        System.out.println("publish:"+publish);



        holder.booksNameT.setText(bookName);
        holder.booksAuthorT.setText(author);
        holder.booksPublishT.setText(publish);
        holder.booksLanguageT.setText(language);
        holder.sn.setText(storedNum);
        holder.bn.setText(borrowedNum);


    }

    @Override
    public int getItemCount(){
        return mBooksList.size();
    }



}
