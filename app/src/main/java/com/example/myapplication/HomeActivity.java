package com.example.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动动画特征
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_home);
//        // 过渡效果
//        Explode explode = new Explode(); // 从屏幕中间进入或退出，移动视图
//        //explode.setInterpolator();// 设置插值器
//        explode.setDuration(2000) ;// 过渡动效执行时间
//// 主界面
//        getWindow().setReenterTransition(explode);  // 重新进入
//        getWindow().setExitTransition(explode); // 出场
// 启动Activity，让过渡动画生效
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    public void run(){

                        //需要执行的任务
                        Intent it = new Intent(HomeActivity.this,MoreLogin.class);
                        startActivity(it,activityOptions.toBundle());


                    }
                }, 3000);
            }
        }).start();


    }
}