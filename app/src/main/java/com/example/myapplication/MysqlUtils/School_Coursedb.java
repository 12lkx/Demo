package com.example.myapplication.MysqlUtils;

import com.example.myapplication.table.School_Course;
import com.example.myapplication.utils.MysqlHelp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class School_Coursedb extends MysqlHelp {
    private Connection conn=getConn();
    private ResultSet resultSet;
    public List<School_Course> SelectCourse(){
        String sql="select * from school_course";
        List<School_Course> list=new ArrayList<>();
        try {
            pstmt=conn.prepareStatement(sql);
            resultSet=pstmt.executeQuery();
            while (resultSet.next()){
//                String week=resultSet.getString(4).substring(0,4);
//                String[] week1=week.split("-");
//                int a=Integer.parseInt(week1[0]);
//                int b=Integer.parseInt(week1[1]);
                School_Course school_course=new School_Course();
                school_course.setCname(resultSet.getString(2));
                school_course.setTeacher(resultSet.getString(3));
                school_course.setWeek(resultSet.getString(4));
                school_course.setSchedule(resultSet.getString(5));
                school_course.setAddress(resultSet.getString(6));
                school_course.setXingqi(resultSet.getInt(7));

                list.add(school_course);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
