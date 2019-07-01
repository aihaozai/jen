package com.example.haozai.jen.Book;

import java.io.Serializable;

public class Books implements Serializable {
    private String name;
    private String author;
    private String press;
    private String price;
    private String number;
    private String booknum;
    public String getBooknum() {
        return booknum;
    }
    public void setBooknum(String booknum) {
        this.booknum = booknum;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    @Override
    public String toString() {
        return "Books [name=" + name + ", author=" + author + ", press=" + press + ", price=" + price + ", number="
                + number + ", booknum=" + booknum + "]";
    }
    }



