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
import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends BaseActivity {

    private static final String KEY_PRODUCT = "key_product";
    private Product product;


    @BindView(R.id.id_Im_pic)
    ImageView idIvPic;
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


    public static void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        Intent intent = getIntent();
        if (intent != null) {
            product = (Product) intent.getSerializableExtra(KEY_PRODUCT);
        }
        if (product == null) {
            T.showToast("参数传递出错");
            return;
        }
        ButterKnife.bind(this);
        setUpToolBar();
        initView();
    }

    private void initView() {
        Glide.with(this)
                .load(Config.baseUrl + product.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(idIvPic);
        idTvTitle.setText(product.getName());
        idTvDesc.setText(product.getDescription());
        idTvPrice.setText(product.getPrice() + "元/份");
        toolbar.setTitle(product.getName());
    }

    @Override
    public void setUpToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
