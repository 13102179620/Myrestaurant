package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.id_btn_skip)
    Button idBtnSkip;

    //使用handler实现3s后加载login
    private Handler mHandler = new Handler();

    //开启新线程执行 toLoginActivity
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initEvent();

        //三秒后执行mRunable
        mHandler.postDelayed(mRunnable, 3000);


    }

    private void initEvent() {
        idBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击了跳过，则直接进入login，忽略之间的runnable
                mHandler.removeCallbacks(mRunnable);
                toLoginActivity();
                finish();
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        //销毁handler放置leak
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
