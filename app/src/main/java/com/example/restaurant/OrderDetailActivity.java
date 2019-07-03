package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.utils.T;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity {

    private static final String KEY_ORDER = "key_order";
    private Order order;


    @BindView(R.id.id_Im_pic)
    ImageView idImPic;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_toolbar)
    CollapsingToolbarLayout layoutToolbar;
    @BindView(R.id.id_tv_title)
    TextView idTvTitle;
    @BindView(R.id.id_tv_desc)
    TextView idTvDesc;
    @BindView(R.id.id_tv_price)
    TextView idTvPrice;


    public static void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER , order);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        if (intent != null) {
            order = (Order) intent.getSerializableExtra(KEY_ORDER);
        }
        if (order == null) {
            T.showToast("参数传递出错");
            return;
        }
        ButterKnife.bind(this);
        setUpToolBar();
        initView();

        ButterKnife.bind(this);
    }

    @Override
    public void setUpToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        List<Order.ProductInO> ps = order.getPs();
        StringBuilder sb = new StringBuilder();
        for (Order.ProductInO productInO : ps){
            sb.append(productInO.product.getName())
                    .append("*")
                    .append(productInO.count)
                    .append("\n");
        }

        Glide.with(this)
                .load(Config.baseUrl + order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(idImPic);
        idTvTitle.setText(order.getRestaurant().getName());
        idTvDesc.setText(sb.toString());
        idTvPrice.setText("共消费：" + order.getPrice() + "元");
        toolbar.setTitle("订单详情");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //系统默认home键 actionBar.setDisplayHomeAsUpEnabled(true);
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
