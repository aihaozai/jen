package com.example.haozai.jen.Car;

import java.io.Serializable;

/**
 * Created by haozai on Created by AYD on 2018/11/21.
 * <p>
 * 购物车
 */
public class ShoppingCartBean implements Serializable {

    private int id;
    private String imageUrl;
    private String shoppingName;
    private String shoppingAuthor;
    private String shoppingPress;
    private double price;

    public boolean isChoosed;
    public boolean isCheck = false;
    private int count;



    public String getShoppingPress() {
        return shoppingPress;
    }

    public void setShoppingPress(String shoppingPress) {
        this.shoppingPress = shoppingPress;
    }

    public ShoppingCartBean() {
    }

    public ShoppingCartBean(int id, String shoppingName, String shoppingPress, int dressSize,
                            double price, int count) {
        this.id = id;
        this.shoppingName = shoppingName;
        this.shoppingPress = shoppingPress;
        this.price = price;
        this.count = count;

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShoppingName() {
        return shoppingName;
    }

    public void setShoppingName(String shoppingName) {
        this.shoppingName = shoppingName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShoppingAuthor() {
        return shoppingAuthor;
    }

    public void setShoppingAuthor(String shoppingAuthor) {
        this.shoppingAuthor = shoppingAuthor;
    }

    @Override
    public String toString() {
        return "ShoppingCartBean{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", shoppingName='" + shoppingName + '\'' +
                ", shoppingAuthor='" + shoppingAuthor + '\'' +
                ", shoppingPress='" + shoppingPress + '\'' +
                ", price=" + price +
                ", isChoosed=" + isChoosed +
                ", isCheck=" + isCheck +
                ", count=" + count +
                '}';
    }
}
