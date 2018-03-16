package com.example.s1.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.s1.R;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

public class TestMainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        View view=this.getWindow().peekDecorView();
        if(view!=null)
        {
            InputMethodManager inputMethodManager=
                    (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(),0);
        }
        EditText editText=(EditText)findViewById(R.id.gridview);
    }
}
