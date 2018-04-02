package com.example.s1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    TextView t1;
    TextView t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        String info=getIntent().getStringExtra("menu_info");
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        if(info.equals("use_help"))
        {
            t1.setText(R.string.use_help1);
            t2.setText(R.string.use_help2);
        }
        else if(info.equals("about_us"))
        {
            t1.setText(R.string.about_us);
            t2.setText("");
        }
    }
}
