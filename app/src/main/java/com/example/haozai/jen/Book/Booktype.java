package com.example.haozai.jen.Book;

/**
 * Created by haozai on 2018-06-25.
 */

public class Booktype {
    @Override
    public String toString() {
        return "Booktype [type=" + type + ", number=" + number + "]";
    }
    private String type;
    private String number;
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}

