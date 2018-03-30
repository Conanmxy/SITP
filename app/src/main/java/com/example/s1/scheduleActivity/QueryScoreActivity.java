package com.example.s1.scheduleActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.s1.R;

import org.w3c.dom.Text;

public class QueryScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_score);
        final TextView userName=(TextView)findViewById(R.id.s_user_name);
        final TextView password=(TextView)findViewById(R.id.s_password);
        Button login=(Button)findViewById(R.id.s_longin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=userName.getText().toString();
                String pass=password.getText().toString();
                Intent intent=new Intent(QueryScoreActivity.this,ScoreActivity.class);
                intent.putExtra("sUserName",name);
                intent.putExtra("sPassword",pass);
                startActivity(intent);
            }
        });

    }
}
