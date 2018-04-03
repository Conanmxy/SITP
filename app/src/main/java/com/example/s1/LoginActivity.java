package com.example.s1;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.Utils.JellyInterpolator;
import com.example.s1.Utils.MyOkHttp;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;
    EditText userNameE;
    EditText passwordE;
    TextView skip;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Window window = getWindow();
//设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(R.color.transparent));

        initView();


    }

//判断网络状态
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        userNameE=(EditText)findViewById(R.id.login_user_name);
        passwordE=(EditText)findViewById(R.id.login_password);
        skip=(TextView)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor=getSharedPreferences("TJuser",MODE_PRIVATE).edit();
                editor.putBoolean("isIn",false);
                editor.apply();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final String userName=userNameE.getText().toString();
        final String password=passwordE.getText().toString();

        String toastInfo="";
        if(isNetworkConnected(LoginActivity.this))
        {
            toastInfo="请稍等...";
            Toast.makeText(LoginActivity.this,toastInfo, Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean flag=false;
                    flag=MyOkHttp.judgeLogin(userName,password);
                    if(flag)//成功登陆
                    {
                        editor=getSharedPreferences("TJuser",MODE_PRIVATE).edit();
                        editor.putString("username",userName);
                        editor.putString("password",password);
                        editor.putBoolean("isIn",true);
                        editor.apply();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"信息错误！",Toast.LENGTH_SHORT).show();
//                            Intent intent = getIntent();
//                            finish();
//                            startActivity(intent);


                            }
                        });
                    }
                }
            }).start();
        }
        else
        {
            toastInfo="网络未连接！";
            Toast.makeText(LoginActivity.this,toastInfo, Toast.LENGTH_SHORT).show();

        }







        // 计算出控件的高与宽
        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        // 隐藏输入框
//        mName.setVisibility(View.INVISIBLE);
//        mPsw.setVisibility(View.INVISIBLE);

        //inputAnimator(mInputLayout, mWidth, mHeight);

    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *      控件
     * @param w
     *      宽
     * @param h
     *      高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
}

