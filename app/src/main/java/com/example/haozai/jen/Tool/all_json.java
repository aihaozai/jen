package com.example.haozai.jen.Tool;

import com.example.haozai.jen.Address.UserAddress;
import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.Book.Booktype;
import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.Car.ShoppingCartBean;
import com.example.haozai.jen.GridView.RecommendBean;
import com.example.haozai.jen.GridView.SecondKill;
import com.example.haozai.jen.View.ImageTitleBean;
import com.example.haozai.jen.conn.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haozai on 2018-10-24.
 */

public class all_json {
    private static String IP = IpAddress.IP;
    private static String ViewIP = IpAddress.VIEWIP;
    //private static String IP = "10.23.32.100:8080";
    public static List<SecondKill> jsonkill(String jsonString) {
        List<SecondKill> sks = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                SecondKill sk = new SecondKill();
                sk.setName(jsonObject.optString("name"));
                sk.setItem_num(jsonObject.optString("item_num"));
                sk.setItem_price(jsonObject.optString("item_price"));
                sk.setSid(jsonObject.optInt("sid"));
                sks.add(sk);
            }
            return sks;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ImageTitleBean> jsonimageview(String jsonString) {
        List<ImageTitleBean> itb = new ArrayList<>();
        //String ip = "http://10.23.32.100:8080/haozai/view/";
        //String ip = "http://116.255.170.88:8080/haozai/view/";
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                ImageTitleBean ibean = new ImageTitleBean();
                ibean.setImageUrl(ViewIP+jsonObject.optString("imageUrl")+".jpg");
                ibean.setTitle(jsonObject.optString("title"));
                itb.add(ibean);
            }
            return itb;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Booktype> jsonBkType(String jsonString) {
        List<Booktype> bktyList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Booktype bk = new Booktype();
                bk.setType(jsonObject.optString("type"));
                bk.setNumber(jsonObject.optString("number"));
                bktyList.add(bk);
            }
            return bktyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Books> jsonbookList(String jsonString) {
        List<Books> bsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookjsonObject = (JSONObject) jsonArray.get(i);
                Books bs = new Books();
                bs.setName(bookjsonObject.optString("name"));
                bs.setAuthor(bookjsonObject.optString("author"));
                bs.setPress(bookjsonObject.optString("press"));
                bs.setPrice(bookjsonObject.optString("price"));
                bs.setBooknum(bookjsonObject.optString("booknum"));
                bsList.add(bs);
            }
            return bsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> jsonUserList(String jsonString) {
        List<User> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                User user = new User();
                user.setUsername(jsonObject.optString("username"));
                user.setPassword(jsonObject.optString("password"));
                user.setSid(jsonObject.optInt("sid"));
                userList.add(user);
            }
            return userList;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
    public static List<Books> jsonbuyinfoList(String jsonString) {
        List<Books> bsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookjsonObject = (JSONObject) jsonArray.get(i);
                Books bs = new Books();
                bs.setName(bookjsonObject.optString("name"));
                bs.setAuthor(bookjsonObject.optString("author"));
                bs.setPress(bookjsonObject.optString("press"));
                bs.setPrice(bookjsonObject.optString("price"));
                bs.setBooknum(bookjsonObject.optString("booknum"));
                bsList.add(bs);
            }
            return bsList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static List<ShoppingCartBean> jsonCarList(String jsonString) {
        List<ShoppingCartBean> shopcar = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                ShoppingCartBean scb = new ShoppingCartBean();
                scb.setShoppingPress(jsonObject.optString("shoppingPress"));
                scb.setShoppingName(jsonObject.optString("shoppingName"));
                scb.setShoppingAuthor(jsonObject.optString("shoppingAuthor"));
                scb.setPrice(jsonObject.optDouble("price"));
                scb.setCount(jsonObject.optInt("mount"));
                scb.setImageUrl(jsonObject.optString("imageUrl"));
                scb.setImageUrl("http://" + IP + "/haozai/img/"+scb.getImageUrl()+".jpg");
                shopcar.add(scb);
            }
            return shopcar;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static List<RecommendBean> jsonREList(String jsonString) {
        List<RecommendBean> reList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookjsonObject = (JSONObject) jsonArray.get(i);
                RecommendBean re = new RecommendBean();
                re.setName(bookjsonObject.optString("name"));
                re.setAuthor(bookjsonObject.optString("author"));
                re.setNum(bookjsonObject.optString("num"));
                re.setPrice(bookjsonObject.optString("price"));
                re.setPress(bookjsonObject.optString("press"));
                //re.setSid(bookjsonObject.optInt("sid"));
                reList.add(re);
            }
            return reList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<UserAddress> jsonaddressList(String jsonString) {
        List<UserAddress> useraddress = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                UserAddress uas = new UserAddress();
                uas.setAddress(jsonObject.optString("address"));
                uas.setDetail(jsonObject.optString("detail"));
                uas.setMing(jsonObject.optString("ming"));
                uas.setPnumber(jsonObject.optString("pnumber"));
                uas.setKeynum(jsonObject.optInt("keynum"));
                uas.setSid(jsonObject.optInt("sid"));
                useraddress.add(uas);
            }
            return useraddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<OrderInfo> jsonorderList(String jsonString) {
        List<OrderInfo> orderlist = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                OrderInfo ord = new OrderInfo();
                ord.setOrderinfo_author(jsonObject.optString("orderinfo_author"));
                ord.setOrderinfo_img(jsonObject.optString("orderinfo_img"));
                ord.setOrderinfo_mount(jsonObject.optString("orderinfo_mount"));
                ord.setOrderinfo_price(jsonObject.optString("orderinfo_price"));
                ord.setOrderinfo_uname(jsonObject.optString("orderinfo_uname"));
                ord.setOrderinfo_press(jsonObject.optString("orderinfo_press"));
                ord.setOrderinfo_youfei(jsonObject.optString("orderinfo_yunfei"));
                ord.setOrderinfo_ming(jsonObject.optString("orderinfo_ming"));
                ord.setOrderinfo_zonge(jsonObject.optString("orderinfo_zonge"));
                ord.setSid(jsonObject.optInt("sid"));
                orderlist.add(ord);
            }
            return orderlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<OrderInfo> allorderList(String jsonString) {
        List<OrderInfo> orderlist = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                OrderInfo ord = new OrderInfo();
                ord.setOrderinfo_author(jsonObject.optString("orderinfo_author"));
                ord.setOrderinfo_img(jsonObject.optString("orderinfo_img"));
                ord.setOrderinfo_mount(jsonObject.optString("orderinfo_mount"));
                ord.setOrderinfo_price(jsonObject.optString("orderinfo_price"));
                ord.setOrderinfo_uname(jsonObject.optString("orderinfo_uname"));
                ord.setOrderinfo_press(jsonObject.optString("orderinfo_press"));
                ord.setOrderinfo_youfei(jsonObject.optString("orderinfo_yunfei"));
                ord.setOrderinfo_ming(jsonObject.optString("orderinfo_ming"));
                ord.setOrderinfo_zonge(jsonObject.optString("orderinfo_zonge"));
                ord.setOrdering(jsonObject.optString("ordering"));
                ord.setSid(jsonObject.optInt("sid"));
                orderlist.add(ord);
            }
            return orderlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    }
