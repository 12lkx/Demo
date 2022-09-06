package com.example.myapplication.MysqlUtils;

import com.example.myapplication.utils.MysqlHelp;

import java.sql.Connection;
import java.sql.ResultSet;

public class News_letterdb extends MysqlHelp {
    private Connection conn=getConn();
    private ResultSet resultSet;
}
