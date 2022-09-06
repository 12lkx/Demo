package com.example.myapplication.utils;
/***
 * 图片验证码
 */

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

public class JsBridge {
    @JavascriptInterface
    public String getData(String data) {
        Gson gson = new Gson();
        VerificationResult result = gson.fromJson(data, VerificationResult.class);
        Log.d("JsBridge", String.valueOf(result.getRet()));
        return String.valueOf(result.getRet());
    }
}
