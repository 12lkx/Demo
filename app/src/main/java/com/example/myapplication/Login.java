package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.table.userinfo;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button btn_loginl;
    private EditText username,password;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if(msg.what==0){
                int count= (int) msg.obj;
                if(count==1){
                    Intent intent=new Intent();
                    intent.setClass(Login.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intitView();
    }

    private void intitView() {
        btn_loginl=findViewById(R.id.btn_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btn_loginl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        userinfo userinfo=new userinfo();
        userinfo.setUser(String.valueOf(username.getText()));
        userinfo.setPassword(String.valueOf(password.getText()));
        DBdao dBdao=new DBdao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count=dBdao.login(userinfo);
                Message message=new Message();
                message.what=0;
                message.obj=count;
                handler.sendMessage(message);
                Log.d("count", String.valueOf(count));
            }
        }).start();
    }


}