package com.example.myapplication.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * 数据库连接
 */
public class MysqlHelp {
    public static PreparedStatement pstmt;
    public static Statement st;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url="jdbc:mysql://192.168.123.74:3306/ad?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
    private static String username="root";
    private static String password="123456";
    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);//获取连接
            Log.d("测试数据库是否连接", "连接成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
