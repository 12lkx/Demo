package com.example.myapplication.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;

public class WindowBackground {
    /**
     * 设置导航栏背景为白色，字体图标为黑色
     * 说明：
     * 1. SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖。
     * 2. SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：设置状态栏图标为黑色或者白色
     * 3. StatusBarUtil 工具类是修改状态栏的颜色为白色。
     *
     * @param window
     * @param activity
     */
//    public static void initStatusBar(Window window, Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            StatusBarUtil.setStatusBarColor(activity, R.color.white);
//        }
//    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    private static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }
}
