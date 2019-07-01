package com.example.haozai.jen.GridView;



/**
 * Created by haozai on 2018-10-24.
 */

public class SecondKill {
    private int sid;
    private String item_num;
    private String name;
    private String item_price;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    @Override
    public String toString() {
        return "SecondKill{" +
                "sid=" + sid +
                ", item_num='" + item_num + '\'' +
                ", name='" + name + '\'' +
                ", item_price='" + item_price + '\'' +
                '}';
    }
}
