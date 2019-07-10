package com.example.restaurant.bean;

import android.content.Intent;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Order implements Serializable {

    //内部类 用于gson生成对象
    public static class ProductInO implements Serializable {
        public Product product;
        public int count;
    }

    public Map<Product, Integer> productMap = new HashMap<>();


    private int id;
    private Date data;
    private Restaurant restaurant;

    private int count;
    private float price;

    private List<ProductInO> ps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<ProductInO> getPs() {
        return ps;
    }

    public void setPs(List<ProductInO> ps) {
        this.ps = ps;
    }


    public void addProduct(Product product) {
        Integer count = productMap.get(product);
        if (count == null || count == 0) {
            productMap.put(product, 1);
        } else {
            count++;
            productMap.put(product, count);
        }

    }

    public void removeProduct(Product product) {
        Integer count = productMap.get(product);
        if (count <= 0 || count == null) {
            return;
        }
        count--;
        productMap.put(product, count);
    }
}
