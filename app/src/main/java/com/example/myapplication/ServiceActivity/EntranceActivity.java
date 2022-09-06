package com.example.myapplication.ServiceActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class EntranceActivity extends AppCompatActivity {
    private Toolbar entrance_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           Window window = getWindow();
            //透明状态栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setStatusBarColor(Color.parseColor("#f1f1f1"));
        }
        initView();
    }

    private void initView() {
        entrance_toolbar=findViewById(R.id.entrance_toolbar);
        entrance_toolbar.setTitle("快捷入口");
        entrance_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        entrance_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        entrance_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}