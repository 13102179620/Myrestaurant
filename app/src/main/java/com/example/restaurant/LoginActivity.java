package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.bean.User;
import com.example.restaurant.biz.UserBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.utils.KeyBoardUtil;
import com.example.restaurant.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.CookieJar;

public class LoginActivity extends BaseActivity {

    public static final String TAG = "LoginActivity-app";
    private UserBiz mUserBiz = new UserBiz();
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";



    @BindView(R.id.id_tv_title)
    TextView idTvTitle;
    @BindView(R.id.id_et_username)
    EditText idEtUsername;
    @BindView(R.id.id_et_password)
    EditText idEtPassword;
    @BindView(R.id.id_btn_login)
    Button idBtnLogin;
    @BindView(R.id.id_btn_register)
    TextView idBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initEvent();

        initIntent(getIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyBoardUtil.hideInputWhenTouchOtherView(this, ev, null);
        return super.dispatchTouchEvent(ev);
    }

    private void initEvent() {

        idBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: 点击了登录确定！");

                String username = idEtUsername.getText().toString();
                String password = idEtPassword.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码不能为空");
                    return;
                }
                
                //开启对话框
                startLoadingProgress();
                
                
                
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        //关闭对话框
                        stopLoadingProgress();
                        T.showToast("登陆成功");
                        // 保存用户信息
                        UserInfoHolder.getInstance().setUser(response);
                        toOrderActivity();
                    }
                });

            }
        });
        
        idBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if (intent == null){
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            return;
        }

        idEtUsername.setText(username);
        idEtPassword.setText(password );
    }


    private void toRegisterActivity() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    
    
    
    
    private void toOrderActivity() {

        Intent intent = new Intent(this , OrderActivity.class);
        startActivity(intent);
        finish();
    }

    public static void launch(Context context , String username , String password){
        Intent intent = new Intent(context , LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }
}
