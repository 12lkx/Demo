package com.example.myapplication.table;

public class userinfo {
    private int id,index_user;
    private String user,phone,password,login_time,finger,imagepath;

    public userinfo() {
    }
//    public userinfo(String user,String phone,String password){
//
//        this.user=user;
//        this.phone=phone;
//        this.password=password;
//
//    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public int getIndex_user() {
        return index_user;
    }

    public void setIndex_user(int index_user) {
        this.index_user = index_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    @Override
    public String toString() {
        return "userinfo{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", login_time='" + login_time + '\'' +
                '}';
    }
}
