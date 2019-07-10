package com.example.restaurant.biz;

import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

public class OrderBiz {

    public void listByPage(int currentPage , CommonCallback<List<Order>> commonCallback){

        OkHttpUtils.post().url(Config.baseUrl + "order_find")
                .addParams("currentPage", currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallback);

    }



    public void add(Order order , CommonCallback<String> commonCallback){

        StringBuilder sb = new StringBuilder();
        Map<Product , Integer> prductMap = order.productMap;
        for (Product p : prductMap.keySet()){
            sb.append(p.getId() + "_" + prductMap.get(p));
            sb.append("|");
        }

        sb.deleteCharAt(sb.length()-1);


        OkHttpUtils.post()
                .url(Config.baseUrl + "order_add")
                .addParams("res_id", order.getRestaurant().getId() + "")
                .addParams("product_str" , sb.toString())
                .addParams("count", order.getCount() + "")
                .addParams("price", order.getPrice() + "")
                .tag(this)
                .build()
                .execute(commonCallback);

    }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
