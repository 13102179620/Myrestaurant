package com.example.restaurant;

import android.text.TextUtils;

import com.example.restaurant.bean.User;
import com.example.restaurant.utils.SPUtils;

public class UserInfoHolder {

    private static UserInfoHolder mInstance = new UserInfoHolder();

    private User mUser;

    //持久化，放置被回收
    private static final  String KEY_USERNAME = "key_username";

    public static UserInfoHolder getInstance(){
        return mInstance;
    }

    public  void setUser(User user){
        mUser = user;
        if (user!=null){
            SPUtils.getInstance().put(KEY_USERNAME, user.getUserName());
        }
    }


    public User getUser(){
        User u = mUser;
        if (u == null){
            String username = (String) SPUtils.getInstance().get(KEY_USERNAME , "");
            if (!TextUtils.isEmpty(username)){
                u = new User();
                u.setUserName(username);
            }
        }

        mUser = u;
        return u;

    }

}
