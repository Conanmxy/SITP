package com.example.s1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class JudgeActivity extends AppCompatActivity {
    boolean isFirstIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);

        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        Intent login = new Intent(this, LoginActivity.class);
        Intent main1 = new Intent(this,MainActivity.class);
        if (isFirstIn) {
            //startActivity(main1);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.apply();
            startActivity(login);
        }
        else {
            startActivity(main1);
        }
        finish();
    }

}
