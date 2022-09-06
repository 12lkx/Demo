package com.example.myapplication.utils;

public class VerificationResult {

    private String bizState;
    private String randstr;
    private String ticket;
    private int ret;// resultCode 0:Success , 2:Quit

    public void setBizState(String bizState) {
        this.bizState = bizState;
    }

    public String getBizState() {
        return bizState;
    }

    public void setRandstr(String randstr) {
        this.randstr = randstr;
    }

    public String getRandstr() {
        return randstr;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getRet() {
        return ret;
    }

    @Override
    public String toString() {
        return "VerificationResult{" +
                "bizState='" + bizState + '\'' +
                ", randstr='" + randstr + '\'' +
                ", ticket='" + ticket + '\'' +
                ", ret=" + ret +
                '}';
    }
}

