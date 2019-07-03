package com.example.restaurant.bean;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private int id;
    private String icon;
    private String name;
    private String description;
    private float date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
    }
}
