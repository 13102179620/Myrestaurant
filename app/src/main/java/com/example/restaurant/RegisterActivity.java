package com.example.restaurant;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.restaurant.bean.User;
import com.example.restaurant.biz.UserBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.utils.KeyBoardUtil;
import com.example.restaurant.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

    public static final String TAG = "RegisterActivity-app";
    private UserBiz userBiz = new UserBiz();

    @BindView(R.id.id_toolbar_title)
    TextView idToolbarTitle;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_et_regusername)
    EditText idEtUsername;
    @BindView(R.id.id_et_regpassword)
    EditText idEtPassword;
    @BindView(R.id.id_et_regrepassword)
    EditText idEtRepassword;
    @BindView(R.id.id_btn_register)
    Button idBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        idToolbarTitle.setText("请注册");
        setUpToolBar();
        initEvent();
    }


    //点击屏幕收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyBoardUtil.hideInputWhenTouchOtherView(this, ev, null);
        return super.dispatchTouchEvent(ev);
    }

    private void initEvent() {
        idBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 点击了注册提交！");

                String username = idEtUsername.getText().toString();
                String password = idEtPassword.getText().toString();
                String repassword = idEtRepassword.getText().toString();



                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码不能为空");
                    return;
                }


                if (!password.equals(repassword)){
                    T.showToast("两次输入的密码不一致");
                    return;
                }

                startLoadingProgress();

                startLoadingProgress();



                userBiz.regist(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        //打印错误信息
                        T.showToast(e.getMessage());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("注册成功" + response.getUserName());
                        LoginActivity.launch(RegisterActivity.this , response.getUserName(),response.getPassword());
                        finish();


                    }
                });
            }
        });

    }
}
