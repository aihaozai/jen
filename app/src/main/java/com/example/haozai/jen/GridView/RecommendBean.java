package com.example.haozai.jen.GridView;

/**
 * Created by haozai on 2018-11-10.
 */

public class RecommendBean {
    private int sid;
    private String num;
    private String name;
    private String author;
    private String price;
    private String press;
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    @Override
    public String toString() {
        return "RecommendBean{" +
                "sid=" + sid +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price='" + price + '\'' +
                ", press='" + press + '\'' +
                '}';
    }
}
