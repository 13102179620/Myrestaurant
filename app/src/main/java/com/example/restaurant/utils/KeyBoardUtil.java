package com.example.restaurant.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

public class KeyBoardUtil {








    public static final void hideInputWhenTouchOtherView(Activity activity, MotionEvent ev, List<View> excludeViews){
        /**
         * @Author SYT
         * @Description
         * @Date 20:09 2019/6/10
         * @Param * @param activity
         * @param ev : Activity上分发事件
         * @param excludeViews
         * @return void
         **/


        if (ev.getAction() == MotionEvent.ACTION_DOWN){ //判断事件是否为点击
            if (excludeViews != null && !excludeViews.isEmpty()){
                for (int i = 0; i < excludeViews.size(); i++){
                    if (isTouchView(excludeViews.get(i), ev)){ //View排除，比如发送按钮等
                        return;
                    }
                }
            }
            View v = activity.getCurrentFocus(); // 获取焦点
            if (isShouldHideInput(v, ev)){  //判断当前获取焦点的view是否为EditTex,只有当EditText获取焦点时，才可点击空白收回键盘
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
    }


    private static final boolean isTouchView(View view, MotionEvent event){

        /**
         * @ClassName: KeyBoardUtil
         * @Author SYT
         * @Description 做标判断是否为需要排除的View
         * @Date 20:41 2019/6/10
         **/


        if (view == null || event == null){
            return false;
        }
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        if (event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom){
            return true;
        }
        return false;
    }

    private static final boolean isShouldHideInput(View v, MotionEvent event){
        if (v != null && (v instanceof EditText)){
            return !isTouchView(v, event);
        }
        return false;
    }




}
