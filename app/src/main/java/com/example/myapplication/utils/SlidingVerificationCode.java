package com.example.myapplication.utils;


import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

public class SlidingVerificationCode {
    private static SlidingVerificationCode instance;
    private WebView webView;
    private WebSettings webSettings;

    public SlidingVerificationCode() {
    }

    public static SlidingVerificationCode getInstance() {
        if (instance == null) {
            instance = new SlidingVerificationCode();
        }
        return instance;
    }

    public void createVerificationCode(WebView webview, final VerificationCallback verificationCallback) {
        this.webView = webview;
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
        // 也可以加载本地html(webView.loadUrl("file:///android_asset/xxx.html"))
        webview.loadUrl("file:///android_asset/verificationCode.html");
        //方法内部类，充当Android和JS的桥梁
        class JsBridgeSlidingVerificationCode {
            @JavascriptInterface
            public void getData(String data) {
                Gson gson = new Gson();
                VerificationResult result = gson.fromJson(data, VerificationResult.class);
                verificationCallback.verify(result);
                Log.d("JsBridge", data);
            }
        }
        JsBridgeSlidingVerificationCode jsBridge = new JsBridgeSlidingVerificationCode();
        webview.addJavascriptInterface(jsBridge, "jsBridge");
    }

}

