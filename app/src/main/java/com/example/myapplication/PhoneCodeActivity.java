package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.PhoneNumberUtils;
import com.example.myapplication.utils.SendCode;
import com.example.myapplication.utils.SlidingVerification;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneCodeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private SlidingVerification seekbar;
    private EditText mEtInput, et_code,phone;
    private Button login, code;
    private TextView mTvTop;
    private TimeCount time;
    private int a=0;
    private boolean flag=false;
    private Call call;
    private Request request;
    private OkHttpClient client;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_code);
        initView();
    }

    private void initView() {
        seekbar = findViewById(R.id.sb_progress);
        mEtInput = findViewById(R.id.phone);
        login = findViewById(R.id.login);
        mTvTop = findViewById(R.id.tv_top);
        code = findViewById(R.id.iv_login_showCode);
        et_code = findViewById(R.id.et_login_phoneCodes);
        seekbar.setOnSeekBarChangeListener(this);
        phone=findViewById(R.id.phone);

        code.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DBdao dBdao=new DBdao();
                                    int cnt=dBdao.QueryPhone(mEtInput.getText().toString().trim());
                                    if (et_code.getText().toString().trim().equals(phoneCode)||cnt==1){
                                        Intent it=new Intent();
                                        it.setClass(PhoneCodeActivity.this, HomeActivity.class);
                                        startActivity(it);
                                    }
                                }
                            }).start();
                    }
                });
        time = new TimeCount(300000, 1000);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getProgress() >= seekBar.getMax() && !TextUtils.isEmpty(mEtInput.getText().toString().trim())) {
            String phone1=phone.getText().toString().trim();
            flag=PhoneNumberUtils.isMobile(phone1);
            if(flag){
                seekBar.setThumb(getResources().getDrawable(R.mipmap.check_pass1));
                seekBar.setThumbOffset(seekBar.getMax());
                seekBar.setProgress(seekBar.getMax());
                seekBar.setEnabled(false);
                mTvTop.setVisibility(View.VISIBLE);
                code.performClick();
            }else{
                Toast.makeText(this,"手机号码有误",Toast.LENGTH_LONG).show();
            }


        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getProgress() >= seekBar.getMax()) {
            if (TextUtils.isEmpty(mEtInput.getText().toString().trim())||!PhoneNumberUtils.isMobile(phone.getText().toString().trim())) {

                    seekBar.setThumb(getResources().getDrawable(R.mipmap.seekbar_thumb1));
                    seekBar.setThumbOffset(0);
                    seekBar.setProgress(0);
                      Toast.makeText(this, "手机号码有误", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(HuaKaiActivity.this, "跳转到输入验证码页", Toast.LENGTH_SHORT).show();
            }
        } else {
            seekBar.setProgress(0);
        }
    }

    @Override
    public void onClick(View view) {
       SendCode sendCode = new SendCode();
        //a=sendCode.send(mEtInput.getText().toString().trim(),et_code.getText().toString().trim());
//        Log.d("验证码", String.valueOf(a));
        time.start();
        send(mEtInput.getText().toString().trim());


    }
   public void send(String phone){
        client=new OkHttpClient();
       //MediaType JSON=MediaType.parse("application/json;charset=utf-8");

       //RequestBody body =RequestBody.create(JSON,json);
       request=new Request.Builder().url("http://192.168.123.74:8080/SMScode/send?phone="+phone)
               .build();
       call = client.newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               String code = response.body().string();
               setPhoneCode(code);
               Log.d("验证码", "---------成功----------");
           }

           @Override
           public void onFailure(@NonNull Call call, @NonNull IOException e) {
               Log.d("验证码", e.getMessage());
           }




       });

//        client.newCall(request).enqueue(callback);

   }
   public void setPhoneCode(String phoneCode1){

        phoneCode=phoneCode1;
       Log.d("测试requet",phoneCode);
   }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            code.setBackgroundColor(Color.parseColor("#B6B6D8"));
            code.setClickable(false);
            code.setText("("+l / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            code.setText("重新获取验证码");
            code.setClickable(true);
            code.setBackgroundColor(Color.parseColor("#4EB84A"));
        }
    }

}