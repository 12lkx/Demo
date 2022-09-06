package com.example.myapplication.utils;

import android.view.View;

public class MyItemClick {
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

}
