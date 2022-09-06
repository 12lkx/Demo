package com.example.myapplication.table;

public class News_letter {
    private String newstitle,content,newsimg,newstime,classfiy,troduce;
    private int img_index;

    public String getNewsimg() {
        return newsimg;
    }

    public void setNewsimg(String newsimg) {
        this.newsimg = newsimg;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewstime() {
        return newstime;
    }

    public void setNewstime(String newstime) {
        this.newstime = newstime;
    }

    public String getClassfiy() {
        return classfiy;
    }

    public void setClassfiy(String classfiy) {
        this.classfiy = classfiy;
    }

    public String getTroduce() {
        return troduce;
    }

    public void setTroduce(String troduce) {
        this.troduce = troduce;
    }

    public int getImg_index() {
        return img_index;
    }

    public void setImg_index(int img_index) {
        this.img_index = img_index;
    }

    @Override
    public String toString() {
        return
                "newstitle='" + newstitle + '\'' +
                ", content='" + content + '\'' +
                ", newstime='" + newstime + '\'' +
                ", classfiy='" + classfiy + '\'' +
                ", troduce='" + troduce + '\'' +
                ", img_index='" + img_index + '\''
                ;
    }
}
