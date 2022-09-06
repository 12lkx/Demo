package com.example.myapplication.MysqlUtils;

import com.example.myapplication.table.School_comment;
import com.example.myapplication.utils.MysqlHelp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class School_commentdb extends MysqlHelp {
    private Connection conn=getConn();
    private ResultSet resultSet;
    public List<School_comment> selectAll(){
        conn=getConn();
        String sql="select * from school_comment";
        List<School_comment> school_comment= new ArrayList<>();
        ResultSet resultSet=null;
        try {
            pstmt=conn.prepareStatement(sql);
            resultSet=pstmt.executeQuery();
            while (resultSet.next()){
                School_comment schoolComment1=new School_comment();
                schoolComment1.setImagepath(resultSet.getString(1));
                schoolComment1.setIndex_user(resultSet.getInt(3));
                schoolComment1.setContent(resultSet.getString(4));
                schoolComment1.setComment(resultSet.getInt(5));
                schoolComment1.setPraise(resultSet.getInt(6));
                schoolComment1.setId(resultSet.getInt(7));
                schoolComment1.setComment_time(resultSet.getTimestamp(8));
                school_comment.add(schoolComment1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return school_comment;
    }
    public void UpdatePraise(int id){
        conn=getConn();
        String sql="update school_comment set praise=praise+1 where id=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            int count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void UpdatePraise1(int id){
        conn=getConn();
        String sql="update school_comment set praise=praise-1 where id=?";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            int count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void InsertPraise(int praise ,String user){
        conn=getConn();
        String sql="insert into school_praise(praise_id,user) value(?,?)";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,praise);
            pstmt.setString(2,"liweichao1");
            int count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int deletePraise(String user ,int id){
        String sql="delete  from school_praise where user=? and praise_id=?";
        int count=0;

        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,user);
            pstmt.setInt(2,id);
            count=pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;

    }
    public int SelectUser(String user,int id){
        String sql="select * from  school_praise where user=? and praise_id=?";
        int count=0;
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,user);
            pstmt.setInt(2,id);
            resultSet=pstmt.executeQuery();
            while (resultSet.next()){
                count=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
