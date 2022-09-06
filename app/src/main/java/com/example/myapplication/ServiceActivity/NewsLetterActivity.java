package com.example.myapplication.ServiceActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.table.News_letter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsLetterActivity extends AppCompatActivity {
    private Toolbar newsLetter_toolbar;
    private RecyclerView newsLetter_recycleview;
    private RecyclerAdapter recyclerAdapter;
    private OkHttpClient client;
    private Request request;
    private Call call;
    private List<News_letter> list;
    private Button btn_news,btn_informations,btn_media;
    private LinearLayoutManager mlayoutManager;
    private String ip="http://192.168.123.74:8080/hello/selectNews";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);
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
        btn_informations=findViewById(R.id.btn_informations);
        btn_news=findViewById(R.id.btn_news);
        btn_media=findViewById(R.id.btn_media);
        //
        //布局管理器
        //mlayoutManager = new LinearLayoutManager(NewsLetterActivity.this);
        newsLetter_recycleview=findViewById(R.id.newsletter_recycleview1);
        newsLetter_recycleview.scrollToPosition(0);
        newsLetter_recycleview.setLayoutManager(new LinearLayoutManager(NewsLetterActivity.this));
        client=new OkHttpClient();

        btn_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ip="http://192.168.123.74:8080/hello/selectMedia";
                request=new Request.Builder().url(ip).build();
                call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str=response.body().string();
                        // Log.d("list数据",str);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson=new Gson();
                                News_letter newsLetter;
                                list=new ArrayList<>();
                                try {
                                    JSONArray jsonArray=new JSONArray(str);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                                        newsLetter=gson.fromJson(String.valueOf(jsonObject),News_letter.class);
                                        list.add(newsLetter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerAdapter=new RecyclerAdapter(list);
                                        newsLetter_recycleview.setAdapter(recyclerAdapter);
                                    }
                                });

                            }
                        }).start();
                    }
                });
            }
        });
        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ip="http://192.168.123.74:8080/hello/selectNews";
                request=new Request.Builder().url(ip).build();
                call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str=response.body().string();
                        // Log.d("list数据",str);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson=new Gson();
                                News_letter newsLetter;
                                list=new ArrayList<>();
                                try {
                                    JSONArray jsonArray=new JSONArray(str);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                                        newsLetter=gson.fromJson(String.valueOf(jsonObject),News_letter.class);
                                        list.add(newsLetter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerAdapter=new RecyclerAdapter(list);
                                        newsLetter_recycleview.setAdapter(recyclerAdapter);
                                    }
                                });

                            }
                        }).start();
                    }
                });
            }
        });
        btn_informations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ip="http://192.168.123.74:8080/hello/selectInformations";
                request=new Request.Builder().url(ip).build();
                call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str=response.body().string();
                        // Log.d("list数据",str);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson=new Gson();
                                News_letter newsLetter;
                                list=new ArrayList<>();
                                try {
                                    JSONArray jsonArray=new JSONArray(str);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                                        newsLetter=gson.fromJson(String.valueOf(jsonObject),News_letter.class);
                                        list.add(newsLetter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerAdapter=new RecyclerAdapter(list);
                                        newsLetter_recycleview.setAdapter(recyclerAdapter);
                                    }
                                });

                            }
                        }).start();
                    }
                });
            }
        });
        request=new Request.Builder().url(ip).build();
        call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str=response.body().string();
                // Log.d("list数据",str);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        News_letter newsLetter;
                        list=new ArrayList<>();
                        try {
                            JSONArray jsonArray=new JSONArray(str);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                                newsLetter=gson.fromJson(String.valueOf(jsonObject),News_letter.class);
                                list.add(newsLetter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerAdapter=new RecyclerAdapter(list);
                                newsLetter_recycleview.setAdapter(recyclerAdapter);
                            }
                        });

                    }
                }).start();
            }
        });
        initView();
    }

    private void initView() {
        newsLetter_toolbar=findViewById(R.id.newsLetter_toolbar);
        newsLetter_toolbar.setTitle("学校新闻");
        newsLetter_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        newsLetter_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        newsLetter_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHoder> {
        private List<News_letter> news_letterList;
        private Context context;
        public RecyclerAdapter(List<News_letter> list){
            this.news_letterList=list;

            //Log.d("测试newslist", String.valueOf(news_letterList));
        }
        @NonNull
        @Override
        public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_item, parent, false);
            View view= LayoutInflater.from(NewsLetterActivity.this).inflate(R.layout.newsletter_recycleview,parent,false);
            ViewHoder viewHolder=new ViewHoder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
            //Log.d("c---------", String.valueOf(position));
            holder.tv_title.setText(news_letterList.get(position).getNewstitle());
            holder.tv_time.setText(news_letterList.get(position).getNewstime());
            holder.btn_information.setBackgroundResource(R.drawable.ic_baseline_navigate_next_24);
            holder.btn_information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return news_letterList.size();
        }

        public class ViewHoder extends RecyclerView.ViewHolder {
            private TextView tv_title,tv_time;
            private Button btn_information;
            public ViewHoder(@NonNull View itemView) {
                super(itemView);
                tv_time=itemView.findViewById(R.id.tv_time);
                tv_title=itemView.findViewById(R.id.tv_title);
                btn_information=itemView.findViewById(R.id.btn_information);

            }
        }
    }
}