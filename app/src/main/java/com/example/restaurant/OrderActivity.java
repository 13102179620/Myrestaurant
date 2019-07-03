package com.example.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.bean.User;
import com.example.restaurant.biz.OrderBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.ui.adapter.OrderAdapter;
import com.example.restaurant.ui.view.refresh.SwipeRefresh;
import com.example.restaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurant.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity {

    public static final String TAG = "OrderActivity-app";
    private OrderAdapter orderAdapter;
    private List<Order> mdatas = new ArrayList<>();
    private OrderBiz orderBiz;
    private int mCurrentPage = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_toolbar)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.id_iv_icon)
    ImageView idIvIcon;
    @BindView(R.id.id_tv_name)
    TextView idTvName;
    @BindView(R.id.id_btn_take_order)
    Button idBtnTakeOrder;
    @BindView(R.id.id_recyclerview)
    RecyclerView idRecyclerview;
    @BindView(R.id.id_refresh_layout)
    SwipeRefreshLayout idRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        initView();
        initEvent();
        loadDatas();
        setUpToolBar();


    }

    @Override
    public void setUpToolBar() {

        toolbar.setTitle("我的订单");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);


    }

    private void initView() {
        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            idTvName.setText(user.getUserName());
        } else {
            toLoginActivity();
            finish();
            return;
        }
        idRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        idRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);



        Glide.with(this).load(R.drawable.icon).placeholder(R.drawable.pictures_no)
                .circleCrop().into(idIvIcon);

        orderAdapter = new OrderAdapter(this, mdatas);
        idRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        idRecyclerview.setAdapter(orderAdapter);
        orderBiz = new OrderBiz();

    }


    private void initEvent() {

        //下拉
        idRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });

        //上拉
        idRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        //点餐
        idBtnTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this , ProductListActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            loadDatas();
        }
    }









    private void loadMore() {
        startLoadingProgress();
        orderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {

            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                if (mCurrentPage > 0 ){
                    mCurrentPage--;
                }
                if (idRefreshLayout.isRefreshing()) {
                    idRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();

                if (response.size() == 0){
                    T.showToast("没有订单了");
                    idRefreshLayout.setPullUpRefreshing(false);
                    return;
                }else {
                    T.showToast("订单加载成功,找到" + mdatas.size() + "份新的订单");
                    mdatas.addAll(response);
                    orderAdapter.notifyDataSetChanged();
                    idRefreshLayout.setPullUpRefreshing(false);
                    Log.d(TAG, "onSuccess: " + mdatas.size() + "");
                }

            }
        });
    }







    private void loadDatas() {
        startLoadingProgress();
        orderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                if (idRefreshLayout.isRefreshing()) {
                    idRefreshLayout.setRefreshing(false);
                }

                if("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                T.showToast("订单更新成功,共有" + mdatas.size() + "份订单!");
                mCurrentPage = 0;
                mdatas.clear();
                mdatas.addAll(response);
                orderAdapter.notifyDataSetChanged();
                if (idRefreshLayout.isRefreshing()) {
                    idRefreshLayout.setRefreshing(false);
                }
                Log.d(TAG, "onSuccess: " + mdatas.size() + "");
            }
        });
    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderBiz.onDestroy();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        try {
            if (keyCode == KeyEvent.KEYCODE_BACK){
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }
        }catch (Exception e){

        }

        return super.onKeyDown(keyCode, event);
    }
}
