package com.example.restaurant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.bean.ProductItem;
import com.example.restaurant.biz.OrderBiz;
import com.example.restaurant.biz.ProductBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.ui.adapter.ProductListAdapter;
import com.example.restaurant.ui.view.refresh.SwipeRefresh;
import com.example.restaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurant.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends BaseActivity {
    
    private ProductBiz productBiz;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();
    private int mCurrentPage;
    private OrderBiz orderBiz = new OrderBiz();

    private float mTotalPrice;
    private int mTotalCount;

    private Order mOrder = new Order();

    @BindView(R.id.id_toolbar_title)
    TextView idToolbarTitle;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recyclerview)
    RecyclerView idRecyclerview;
    @BindView(R.id.id_refresh_layout)
    SwipeRefreshLayout idRefreshLayout;
    @BindView(R.id.id_tv_count)
    TextView idTvCount;
    @BindView(R.id.id_btn_pay)
    Button idBtnPay;
    @BindView(R.id.content_product_list)
    LinearLayout contentProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        setUpToolBar();
        setTitle("订餐");

        initView();

        initEvent();
    }


    private void initView() {
        idRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        idRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);
        productBiz = new ProductBiz();
        loadDatas();
        mAdapter = new ProductListAdapter(this, mDatas);
        idRecyclerview.setAdapter(mAdapter);
        idRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }



    private void initEvent() {

        //上拉
        idRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        

        //下拉
        idRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });


        //点击付款
        idBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTotalCount <= 0 ){
                    T.showToast("您还没有选择菜品");
                    return;
                }

                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());

                startLoadingProgress();


                orderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功!");

                        //返回给OrderActivity
                        setResult(RESULT_OK);

                        finish();
                    }
                });
            }
        });

        //ProductListAdapter回调接口 , 设置总金额和数量
        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                idTvCount.setText("数量：" + mTotalCount);

                mTotalPrice += productItem.getPrice();

                if (mTotalCount == 0){
                    mTotalPrice = 0;
                }

                idBtnPay.setText(mTotalPrice + "元 立即支付");

                mOrder.addProduct(productItem);

            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                idTvCount.setText("数量：" + mTotalCount);

                //当数量为0时，价格也为零，浮点数加减运算回损失精度
                if (mTotalCount == 0){
                    mTotalPrice = 0;
                }

                mTotalPrice -= productItem.getPrice();
                idBtnPay.setText(mTotalPrice + "元 立即支付");

                mOrder.removeProduct(productItem);
            }
        });




    }

    private void loadDatas() {

        productBiz.listPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                idRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                idRefreshLayout.setRefreshing(false);
                mCurrentPage = 0;
                mDatas.clear();
                for (Product p : response){
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();

                mTotalCount = 0 ;
                mTotalPrice = 0 ;


                idTvCount.setText("数量：" + mTotalCount);
                idBtnPay.setText(mTotalPrice + "元 立即支付");
            }
        });

    }

    private void loadMore() {


        productBiz.listPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                T.showToast(e.getMessage());
                stopLoadingProgress();
                mCurrentPage--;
                idRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                idRefreshLayout.setPullUpRefreshing(false);
                if (response.size() == 0) {
                    T.showToast("没有咯~~~");
                    return;
                }
                T.showToast("又找到" + response.size() + "道菜~~~");
                //mDatas.clear();
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
            }
        });


    }







    @Override
    protected void onDestroy(){
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        productBiz.onDestroy();
    }




}
