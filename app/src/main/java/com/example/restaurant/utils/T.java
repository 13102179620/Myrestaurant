package com.example.restaurant.utils;

import android.content.Context;
import android.widget.Toast;


public class T {
    // Toast 辅助类

    private static Toast toast;

    public static void showToast(
            String content) {
        toast.setText(content);
        toast.show();
    }

    public static void init(Context context) {
        toast = Toast.makeText(context,
                "",
                Toast.LENGTH_SHORT);
    }
}
