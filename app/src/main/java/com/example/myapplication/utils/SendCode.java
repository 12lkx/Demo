package com.example.myapplication.utils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Tencent Cloud Sms Sendsms
 *
 */
public class SendCode
{
  private int a=0;
    public  int send(String phone,String Phonecode) {

                String json="{\"phone\":\"1760594912\"}";;
                OkHttpClient client=new OkHttpClient();
                MediaType JSON=MediaType.parse("application/json;charset=utf-8");

                RequestBody body =RequestBody.create(JSON,json);
                Request request=new Request.Builder().url("http://192.168.123.74:8080/SMScode/send?phone="+phone)
                        .build();
//        client.newCall(request).enqueue(callback);
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }
                });
  return a;

                }

}
//    public HashMap<String,Object> sendcode(String tel) throws TencentCloudSDKException {
//        HashMap<String,Object> map = new HashMap<String,Object>();
//        //创建短信发送请求对象
//        SendSmsRequest req = new SendSmsRequest();
//        //接收短信手机号
//        String[] phone = {"17605944912"};
//        req.setPhoneNumberSet(phone);
//        //个人短信模板模板Id
//        req.setTemplateId("1419758");
//        //个人短信发送SDK
//        req.setSmsSdkAppId("1400685052");
//        //个人短信签名
//        req.setSignName("窝窝旅游公众号");
//        //短信验证码
//        //String code=RandomUtil.getFourBitRandom();
//        String[] p = {"1234"};
//        Credential cred = new Credential("AKIDFJ8j4A3h5E9pFQL1RfeNq43mGUTEk01m", "XpreBSUHwTnumOlxta2EBtLYVDguuhGb");
//
//        // 实例化一个http选项，可选，没有特殊需求可以跳过
//        HttpProfile httpProfile = new HttpProfile();
//        // 设置代理（无需要直接忽略）
//        // httpProfile.setProxyHost("真实代理ip");
//        // httpProfile.setProxyPort(真实代理端口);
//        /* SDK默认使用POST方法。
//         * 如果你一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
//        httpProfile.setReqMethod("POST");
//        /* SDK有默认的超时时间，非必要请不要进行调整
//         * 如有需要请在代码中查阅以获取最新的默认值 */
//        httpProfile.setConnTimeout(60);
//        /* 指定接入地域域名，默认就近地域接入域名为 sms.tencentcloudapi.com ，也支持指定地域域名访问，例如广州地域的域名为 sms.ap-guangzhou.tencentcloudapi.com */
//        httpProfile.setEndpoint("sms.tencentcloudapi.com");
//        req.setTemplateParamSet(p);
//        //发送对象，并获取获取响应对象
//        ClientProfile clientProfile = new ClientProfile();
//        /* SDK默认用TC3-HMAC-SHA256进行签名
//         * 非必要请不要修改这个字段 */
//        clientProfile.setSignMethod("HmacSHA256");
//        clientProfile.setHttpProfile(httpProfile);
//        /* 实例化要请求产品(以sms为例)的client对象
//         * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8 */
//        SmsClient smsClient = new SmsClient(cred, "ap-guangzhou",clientProfile);
//        SendSmsResponse resp = smsClient.SendSms(req);
//        //将响应结果转换成字符串格式
//        String info = SendSmsResponse.toJsonString(resp);
//        // 打印结果信息
//        System.out.println(info);
//
//
//        return map;
//    }
