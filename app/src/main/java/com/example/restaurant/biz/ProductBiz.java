package com.example.restaurant.biz;

import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class ProductBiz  {

    public void listPage(int currentPage , CommonCallback<List<Product>> commonCallback){

        OkHttpUtils.post()
                .url(Config.baseUrl + "product_find")
                .addParams("currentPage", currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallback);

    }



    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
