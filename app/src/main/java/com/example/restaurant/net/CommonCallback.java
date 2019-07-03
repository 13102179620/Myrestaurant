package com.example.restaurant.net;


import com.example.restaurant.MainActivity;
import com.example.restaurant.bean.User;
import com.example.restaurant.utils.GsonUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CommonCallback<T> extends StringCallback {

    Type mType;


    //获取Type类型
    public CommonCallback() {
        Class<? extends CommonCallback> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("missing type params");
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        //T
        mType = parameterizedType.getActualTypeArguments()[0];

    }


    @Override
    public void onError(okhttp3.Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {

        try {
            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");

            //请求成功
            if (resultCode == 1) {
                String data = resp.getString("data");
                Gson gson = new Gson();
                T user = (T) GsonUtil.getGson().fromJson(data, mType);
                onSuccess((T) GsonUtil.getGson().fromJson(data, mType));

                //请求失败
            } else {
                onError(new RuntimeException(resp.getString("resultMessage")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }

    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T response);
}
