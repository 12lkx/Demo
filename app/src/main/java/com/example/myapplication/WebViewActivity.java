package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.SlidingVerificationCode;
import com.example.myapplication.utils.VerificationCallback;
import com.example.myapplication.utils.VerificationResult;
import com.google.gson.Gson;

public class WebViewActivity extends AppCompatActivity {
    private WebView webview;
    private WebSettings webSettings;
    SlidingVerificationCode slidingVerificationCode;
    private VerificationCallback verificationCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webview = (WebView) findViewById(R.id.webview);
        createVerificationCode();
    }

    public void createVerificationCode() {

        //设置透明度
        webview.setBackgroundColor(0); // 设置背景色
        webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 禁用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 开启js支持
        //webSettings.setAllowFileAccess(true);//本地读取文件
        webSettings.setJavaScriptEnabled(true);
        //webview.addJavascriptInterface(new JsBridge(), "jsBridge");
        // 也可以加载本地html(webView.loadUrl("file:///android_asset/xxx.html"))
        webview.loadUrl("file:///android_asset/Code.html");

        //方法内部类，充当Android和JS的桥梁
        class JsBridgeSlidingVerificationCode {
            @JavascriptInterface
            public void getData(String data) {
                Gson gson = new Gson();
                VerificationResult result = gson.fromJson(data, VerificationResult.class);
                Log.d("JsBridge", String.valueOf(result.getRet()));
                if(result.getRet()==2) {
                    Intent it = new Intent();
                    it.setClass(WebViewActivity.this, MoreLogin.class);
                    startActivity(it);
                }
                if (result.getRet()==0){
                    Intent it = new Intent();
                    it.setClass(WebViewActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        }
        JsBridgeSlidingVerificationCode jsBridge = new JsBridgeSlidingVerificationCode();
        webview.addJavascriptInterface(jsBridge, "jsBridge");

    }



//
//    private void initView() {
//        webview = (WebView) findViewById(R.id.webview);
//        webSettings = webview.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//// 禁用缓存
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webview.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//// 开启js支持
//        webSettings.setJavaScriptEnabled(true);
//        //webview.addJavascriptInterface(new JsBridge(), "jsBridge");
//// 也可以加载本地html(webView.loadUrl("file:///android_asset/xxx.html"))
//        webview.loadUrl("file:///android_asset/Code.html");
//        class JsBridgeSlidingVerificationCode {
//            @JavascriptInterface
//            public void getData(String data) {
//                Gson gson = new Gson();
//                VerificationResult result = gson.fromJson(data, VerificationResult.class);
//                verificationCallback.verify(result);
//                Log.d("JsBridge", data);
//            }
//        }
//        JsBridgeSlidingVerificationCode jsBridge = new JsBridgeSlidingVerificationCode();
//        webview.addJavascriptInterface(jsBridge, "jsBridge");
//


}