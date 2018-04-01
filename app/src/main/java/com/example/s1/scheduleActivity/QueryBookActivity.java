package com.example.s1.scheduleActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.adapter.BooksAdapter;

import java.util.ArrayList;
import java.util.List;

public class QueryBookActivity extends AppCompatActivity {

    EditText bookNameE;
    Button searchBook;
    List<String> booksList=new ArrayList<>();;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_book);
        bookNameE=(EditText)findViewById(R.id.book_name);
        searchBook=(Button)findViewById(R.id.book_search);

        //获取缓存的用户信息
        SharedPreferences userPrefer=getSharedPreferences("TJuser",MODE_PRIVATE);

        final String userName=userPrefer.getString("username","");
        final String  password=userPrefer.getString("password","");

        recyclerView=(RecyclerView)findViewById(R.id.books_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        searchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookName=bookNameE.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       booksList= MyOkHttp.loginLib(userName,password,bookName);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BooksAdapter booksAdapter=new BooksAdapter(booksList);
                                if(booksList.size()==0)
                                {
                                    Toast.makeText(QueryBookActivity.this,"没有查询结果！",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    recyclerView.setAdapter(booksAdapter);
                                }
                            }
                        });

                    }
                }).start();



            }
        });
    }
}
