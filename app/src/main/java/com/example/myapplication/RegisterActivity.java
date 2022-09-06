package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.table.userinfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button,btn_register;
    private EditText et_user,et_phone,et_pwd1,et_pwd2,et_phonecode;
    private TimeCount time;
    private   int a=0;
    private Request request;
    private Call call;
    private OkHttpClient client;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if (msg.what==0){
                showMsg("注册成功可以返回登录");
            }else{
                showMsg("验证码有误，请重新输入");
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        button=findViewById(R.id.iv_registeractivity_showCode);
        btn_register=findViewById(R.id.bt_registeractivity_register);
        et_user=findViewById(R.id.et_registeractivity_username);
        et_phone=findViewById(R.id.et_registeractivity_phone);
        et_pwd1=findViewById(R.id.et_registeractivity_password1);
        et_pwd2=findViewById(R.id.et_registeractivity_password2);
        et_phonecode=findViewById(R.id.et_registeractivity_phoneCodes);
        btn_register.setOnClickListener(view -> registInto());
        button.setOnClickListener(this);
        time = new TimeCount(60000, 1000);
    }

    private void registInto() {
        Log.d("测试","开始注册");
        DBdao dBdao=new DBdao();
        Message msg=new Message();
        userinfo userinfo1=new userinfo();
        if (et_pwd1.getText().toString().trim().equals(et_pwd2.getText().toString().trim())){
            showMsg(et_user.getText().toString());
            userinfo1.setUser(et_user.getText().toString());
            userinfo1.setPassword(et_pwd1.getText().toString());
            userinfo1.setPhone(et_phone.getText().toString());
            //a=new SendCode().send(et_phone.getText().toString(),et_phonecode.getText().toString());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String code=response.body().toString();
                    if (code.equals(et_phonecode.getText().toString().trim())){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int count = dBdao.insert(userinfo1);
                                if (count > 0) {
                                    msg.what=0;
                                    handler.sendMessage(msg);
                                }
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }


            });

        }else{
            Toast.makeText(this,"密码不一致",Toast.LENGTH_LONG).show();
        }

    }
    public  void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
}
    @Override
    public void onClick(View view) {
        time.start();
        request = new Request.Builder().url("http://192.168.123.74:8080/SMScode/send?phone="+et_phone.getText().toString().trim())
                .build();
        call = client.newCall(request);

    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            button.setBackgroundColor(Color.parseColor("#B6B6D8"));
            button.setClickable(false);
            button.setText("("+l / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            button.setText("重新获取验证码");
            button.setClickable(true);
            button.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}