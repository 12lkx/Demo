package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.table.userinfo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class MoreLogin extends AppCompatActivity implements View.OnClickListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment f = null;
    private ImageButton image1,image2,image3,image4,biometricLoginButton,face,mybtn;
    private Button btn_loginl,bt_yanzhan;
    private EditText username,password;
    private TextView phone,register;
    private ImageView iv;
    AnimationDrawable animationDrawable;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if(msg.what==0){
                int count= (int) msg.obj;
                if(count==1){
                    Intent intent = new Intent();
                    intent.setClass(MoreLogin.this, SchoolHomeActivity.class);
                    startActivity(intent);

                }
            }
            if (msg.what==3){
                startActivity(new Intent(MoreLogin.this,SchoolHomeActivity.class));
            }
        }
    };
    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动动画特征
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_more_login);
        init();
        mybtn=findViewById(R.id.myBtn);
        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer time=new Timer();
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MoreLogin.this,SchoolHomeActivity.class));
                    }
                },500);

            }
        });
        // 过渡效果
        Fade explode = new Fade(); // 从屏幕中间进入或退出，移动视图
        //explode.setInterpolator();// 设置插值器
        explode.setDuration(1000) ;// 过渡动效执行时间
// 主界面
        getWindow().setReenterTransition(explode);  // 重新进入
        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode); // 出场
        //溶解
        //getWindow().setEnterTransition(new Explode());
        //滑动
        // getWindow().setEnterTransition(new Slide());
        //淡入淡出
        // getWindow().setEnterTransition(new Fade());
        biometricLoginButton=findViewById(R.id.biometricLoginButton);
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(this, "应用可以进行生物识别技术进行身份验证", Toast.LENGTH_SHORT).show();

                biometricLoginButton.setOnClickListener(view -> showBiometricPrompt());


                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "该设备上没有搭载可用的生物特征功能", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "生物识别功能当前不可用", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "用户没有录入生物识别数据", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void init() {
         

        image1 = findViewById(R.id.image1);
        face=findViewById(R.id.face);
        face.setOnClickListener(this);
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        f = new ZhangHaoFragment();
//        fragmentTransaction.add(R.id.fragment, f);
//        fragmentTransaction.commit();
        image1.setOnClickListener(this);
        btn_loginl = findViewById(R.id.btn_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        register=findViewById(R.id.register);
        phone.setOnClickListener(this);
        register.setOnClickListener(this);
        iv=findViewById(R.id.imgv);
//        TranslateAnimation translateAnimation = new
//                TranslateAnimation(-850f, 30f, 0f, 0f); //设置平移的起点和终点
//        translateAnimation.setDuration(2500) ;//动画持续的时间为2.5s
//        translateAnimation.setRepeatCount(Animation.INFINITE);//无限次数
//        translateAnimation.setFillAfter(true) ;//不回到起始位置
//        // translateAnimation.setAnimationListener(this)
//        iv.setAnimation(translateAnimation) ;//给两个长方形imageView添加的动画效果
//        translateAnimation.startNow() ;//动画开始执行 放在最后即可

//        animationDrawable= (AnimationDrawable) iv.getBackground();
//        animationDrawable.start();
        btn_loginl.setOnClickListener(new View.OnClickListener() {
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
        });


    }
    @Override
    public void onClick(View view) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.image1:
                startActivity(new Intent(this,QQLoginActivity.class));
                break;
            case R.id.register:
               //f=new ZuceFragment();
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.phone:
                startActivity(new Intent(this,PhoneCodeActivity.class));
                break;
            case R.id.face:
               // startActivity(new Intent(this,FaceRegisterActivity.class));
                break;
//            case R.id.image4:
//                f=new ZuceFragment();
//                break;

        }
//        //切换动画
//        fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
//        fragmentTransaction.replace(R.id.fragment, f);
//        fragmentTransaction.commit();

    }



    public void fingerprint() {
       
        }

    private void showBiometricPrompt() {
            BiometricPrompt.PromptInfo promptInfo =
                    new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("指纹验证") //设置大标题
                            .setSubtitle("Log in using your biometric credential") // 设置标题下的提示
                            .setNegativeButtonText("取消") //设置取消按钮
                            .build();

            //需要提供的参数callback
            BiometricPrompt biometricPrompt = new BiometricPrompt(MoreLogin.this,
                    executor, new BiometricPrompt.AuthenticationCallback() {
                //各种异常的回调
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(),
                                    "Authentication error: " + errString, Toast.LENGTH_SHORT)
                            .show();
                }

                //认证成功的回调
                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    BiometricPrompt.CryptoObject authenticatedCryptoObject =
                            result.getCryptoObject();
                 /*   try {
                        Field field = result.getClass().getDeclaredField("mFingerprint");
                        field.setAccessible(true);
                        Object fingerPrint = field.get(result);

                        Class<?> clzz = Class.forName("androidx.biometric.BiometricPrompt$AuthenticationResult");
                        Method getName = clzz.getDeclaredMethod("getName");
                        Method getFingerId = clzz.getDeclaredMethod("getFingerId");
                        @SuppressLint("SoonBlockedPrivateApi") Method getGroupId = clzz.getDeclaredMethod("getGroupId");
                        Method getDeviceId = clzz.getDeclaredMethod("getDeviceId");

                        CharSequence name = (CharSequence) getName.invoke(fingerPrint);
                        int fingerId = (int) getFingerId.invoke(fingerPrint);
                        int groupId = (int) getGroupId.invoke(fingerPrint);
                        long deviceId = (long) getDeviceId.invoke(fingerPrint);

                        Log.d("TAG", "name: " + name);
                        Log.d("TAG", "fingerId: " + fingerId);
                        Log.d("TAG", "groupId: " + groupId);
                        Log.d("TAG", "deviceId: " + deviceId);

                    } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |  InvocationTargetException e) {
                        e.printStackTrace();
                    }*/
                    //设备id
                    String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    DBdao dBdao=new DBdao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int count=dBdao.QueryFingerprint(android_id);
                            if (count==1){
                                Message msg=new Message();
                                msg.what=3;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                    // User has verified the signature, cipher, or message
                    // authentication code (MAC) associated with the crypto object,
                    // so you can use it in your app's crypto-driven workflows.
                }

                //认证失败的回调
                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication failed",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            });

            // 显示认证对话框
            biometricPrompt.authenticate(promptInfo);
        }



    public void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
}

}