package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.table.userinfo;
import com.example.myapplication.utils.MysqlHelp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBdao extends MysqlHelp {
    private Connection conn=null;

    public int delete(int id){
        int count=0;
        conn=MysqlHelp.getConn();
        if (conn!=null){
            Log.d("success","------成功--------");
        }else{
            Log.d("error","--------失败---------");
        }
        String sql="delete from user where id=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
             count=pstmt.executeUpdate();
//            while (rs.next()){
//                count=rs.getInt("num");
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int Count(){
        int count=0;
        conn=getConn();
        if(conn==null){
            Log.d("conn","---------conn为空------------");
        }
        String sql="select count(*) as num from user";
        try {
            pstmt=conn.prepareStatement(sql);
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()){
                count=rs.getInt("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int edit(String user,String pwd,int id){
        conn=getConn();
        int count=0;
        String sql="update user set user=?,password=? where id=?";
        try {
            pstmt=conn.prepareStatement(sql);

            pstmt.setString(1,user);
            pstmt.setString(2,pwd);
            pstmt.setInt(3,id);
            count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int insert(userinfo userinfo){
        conn=getConn();
        int count=0;
        if(conn==null){
            Log.d("conn","---------conn为空------------");
        }
        String sql="insert into user(user,phone,password) value(?,?,?)";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,userinfo.getUser());
            pstmt.setString(2,userinfo.getPhone());
            pstmt.setString(3,userinfo.getPassword());

             count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public userinfo queryinfo(int id){
        conn=getConn();
        userinfo userinfo=new userinfo();
        String sql="select * from user where id=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()){
                userinfo.setUser(rs.getString(2));
                userinfo.setPhone(rs.getString(3));
                userinfo.setPassword(rs.getString(4));
                userinfo.setLogin_time(rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userinfo;
    }
    public int login(userinfo userinfo){
        conn=getConn();
        Log.d("连接成功","-------------");
        int count=0;
        ResultSet resultSet=null;
        String sql="select * from user where user=? and password=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,userinfo.getUser());
            pstmt.setString(2,userinfo.getPassword());
            resultSet=pstmt.executeQuery();
            while (resultSet.next()){
                count=1;
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int QueryPhone(String phone){
        int count=0;
        conn=getConn();
        ResultSet resultSet=null;
        String sql="select * from user where phone =?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,phone);
            resultSet= pstmt.executeQuery();
            while (resultSet.next()){
                count=1;
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
     return count;
    }
    public int QueryFingerprint(String fingerPrint){
        int count=0;
        conn=getConn();
        ResultSet resultSet=null;
        String sql="select * from user where fingerprint =?";
        try {
            pstmt =conn.prepareStatement(sql);
            pstmt.setString(1,fingerPrint);
            resultSet= pstmt.executeQuery();
            while ( resultSet.next()){
                count=1;
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public void insertImageview(String imagepath,int index){
        conn=getConn();
        int resultSet=0;
        String sql="update user set imgpath=?,index_user=?  where user=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,imagepath);
            pstmt.setInt(2,index);
            pstmt.setString(3,"333");
            resultSet= pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   public List<userinfo> SelectAll(){
        conn=getConn();
        String sql="select * from user";
        List<userinfo> list=new ArrayList<>();
        ResultSet resultSet;
       try {
           pstmt=conn.prepareStatement(sql);
           resultSet=pstmt.executeQuery();
           while (resultSet.next()){
               userinfo u=new userinfo();
               u.setUser(resultSet.getString(2));
               u.setImagepath(resultSet.getString(6));
               u.setIndex_user(resultSet.getInt(7));
               list.add(u);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return list;
   }

}
