package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.table.userinfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button query_count,edit_user,query_delete,insert_user,query_info,send;
    private TextView tv_text,tv_text1,tv_show1,tv_show2,tv_show3,tv_show4;
    private EditText tv_text2,tv_text3,edit_tv1,edit_tv2,edit_tv3,edit_tv4;
    private userinfo userinfo;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if(msg.what==0){
                tv_text1.setText("删除成功");
            }
            else if (msg.what==1){
                tv_text.setText(String.valueOf((Integer)msg.obj));
            }
            else if (msg.what==2){
                edit_user.setText("修改成功");
            }
            else if (msg.what==3){
                insert_user.setText("添加成功");
            }
            else{
                tv_show1.setText(userinfo.getUser());
                tv_show2.setText(userinfo.getPhone());
                tv_show3.setText(userinfo.getPassword());
                tv_show4.setText(userinfo.getLogin_time());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","___________create_____________");
        initView();
    }
    public void initView(){
        query_count=findViewById(R.id.query_count);
        edit_user=findViewById(R.id.edit_user);
        query_delete=findViewById(R.id.query_delete);
        insert_user=findViewById(R.id.insert_user);
        query_info=findViewById(R.id.query_info);
        send=findViewById(R.id.send);
        tv_text=(TextView) findViewById(R.id.tv_text);
        tv_text1=(TextView) findViewById(R.id.tv_text1);
        tv_text2= findViewById(R.id.tv_text2);
        tv_text3= findViewById(R.id.tv_text3);
        edit_tv1=findViewById(R.id.edit_tv1);
        edit_tv2=findViewById(R.id.edit_tv2);
        edit_tv3=findViewById(R.id.edit_tv3);
        edit_tv4=findViewById(R.id.edit_tv4);
        tv_show1=findViewById(R.id.tv_show1);
        tv_show2=findViewById(R.id.tv_show2);
        tv_show3=findViewById(R.id.tv_show3);
        tv_show4=findViewById(R.id.tv_show4);
        query_count.setOnClickListener(this);
        edit_user.setOnClickListener(this);
        query_delete.setOnClickListener(this);
        insert_user.setOnClickListener(this);
        query_info.setOnClickListener(this);
        send.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query_delete:
                querydelete();
                break;
            case R.id.edit_user:
                editUser();
                break;
            case R.id.query_count:
                queryCount();
                break;
            case R.id.insert_user:
                insertUser();
                break;
            case R.id.query_info:
                queryInfo();
                break;
            case R.id.send:


                break;
        }
    }

    private void queryInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBdao dBdao=new DBdao();
                userinfo=dBdao.queryinfo(2);
                Log.d("查询","---------------------------");
                Message msg=new Message();
                msg.what=4;
               handler.sendMessage(msg);
            }
        }).start();
    }

    private void insertUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userinfo userinfo=new userinfo();
                userinfo.setUser(String.valueOf(edit_tv1.getText()));
                userinfo.setPhone(String.valueOf(edit_tv2.getText()));
                userinfo.setPassword(String.valueOf(edit_tv3.getText()));
                userinfo.setLogin_time(String.valueOf(edit_tv4.getText()));
                DBdao dBdao=new DBdao();
                dBdao.insert(userinfo);
                Message msg=new Message();
                msg.what=3;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void editUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username= String.valueOf(tv_text2.getText());
                String pwd= String.valueOf(tv_text3.getText());
                DBdao dBdao=new DBdao();
                int count=dBdao.edit(username,pwd,2);
                Message msg=new Message();
                msg.what=2;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void queryCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBdao dBdao=new DBdao();
                int count=dBdao.Count();
                Message msg=new Message();
                msg.what=1;
                msg.obj=count;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void querydelete(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBdao mysqlHelp=new DBdao();
                int count=mysqlHelp.delete(1);
                Message msg=Message.obtain();
                msg.what=0;
                msg.obj=count;
                Log.d("msg",String.valueOf(count));
                handler.sendMessage(msg);
            }
        }).start();
    }
}