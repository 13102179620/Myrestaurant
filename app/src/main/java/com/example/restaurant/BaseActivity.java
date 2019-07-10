package com.example.restaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;


/**
 * @ClassName: BaseActivity
 * @Author SYT
 * @Description 基类Activity ， 包括统一的toolbar ， 统一管理加载dialog
 **/


public class BaseActivity extends AppCompatActivity {

    // TODO: 2019/7/2 修改弹窗样式
    private ProgressDialog mLoadingDialog;

    public static final String TAG = "BaseActivity-app";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(0xff000000);
            Log.d(TAG, "onCreate: 设置状态栏颜色为黑色");
        }

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage("加载中...");

    }


    //管理toolbar样式
    public void setUpToolBar() {
        Toolbar idToolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(idToolbar);
        idToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }




    //管理loadingDialog的启停
    protected void startLoadingProgress() {
        mLoadingDialog.show();
    }


    protected void stopLoadingProgress() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        mLoadingDialog = null;
    }



    // 在所有的activity检测是否登录 ， 没有的话就跳转
    protected void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
