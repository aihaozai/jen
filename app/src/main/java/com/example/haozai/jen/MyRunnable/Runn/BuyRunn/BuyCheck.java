package com.example.haozai.jen.MyRunnable.Runn.BuyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Car.ShoppingCartBean;
import com.example.haozai.jen.Utils.WebServicePost;

import java.util.List;

/**
 * Created by haozai on 2018-11-15.
 */

public class BuyCheck {
    private static String readstring,pswd,cash,ERROR;
    private static List<ShoppingCartBean> list;
    private static String userid;
    private static CBuy cBuy;
    public void inter( CBuy  cBuy){
        this.cBuy= cBuy;
    }
    public static void PswdCheck(String str,String strr) {
        readstring = str;
        pswd = strr;
        new Thread(new MyRunnable()).start();
    }
    public static void cancelPay(String name,List<ShoppingCartBean> bean) {
        userid = name;
        list =bean;
        new Thread(new cancelRunn()).start();
    }
    public static void goBuy(String str,String strr,String strrr) {
        readstring = str;
        pswd = strr;
        cash = strrr;
        new Thread(new goBuyRunn()).start();
    }
    public static void addOrder(String name,List<ShoppingCartBean> bean) {
        userid = name;
        list =bean;
        new Thread(new addOrderRunn()).start();
    }
    /**
    * 验证支付密码是否正确
    */
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                String checkPswd = WebServicePost.CheckPswd(readstring,pswd);
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("checkPswd", checkPswd);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                setERROR();
            }
        }
    }
    /**
    * 取消支付添加至待付款状态
    */
    private static class cancelRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  canelpay = WebServicePost.Ordering(userid,list,"daiing");
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("cancelpay", canelpay);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
               setERROR();
            }
        }
    }
    /**
    * 密码正确后扣钱
    */
    private static class goBuyRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  buyresult = WebServicePost.BuyPay(readstring,pswd,cash);
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("buy", buyresult);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
               setERROR();
            }
        }
    }
    /**
    * 扣款成功添加订单
    */
    private static class addOrderRunn implements Runnable {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Message message = new Message();
            try {
                String  order_res = WebServicePost.Ordering(userid,list,"accepting");
                bundle.putString("order_res", order_res);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                setERROR();
            }
        }
    }
    private static class deleCarRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  dele_res = WebServicePost.deleCar(userid,list);
            } catch (Exception e) {
                e.printStackTrace();
                setERROR();
            }
        }
    }
    public static void setERROR(){
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putString("ERROR", "ERROR");
        message.setData(bundle);
        checkHandler.sendMessage(message);
    }
    private static Handler checkHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            bundle = msg.getData();
            ERROR= bundle.getString("ERROR");
            if (ERROR=="ERROR"){
                cBuy.resultnum(-1);
            }else {
                String result = bundle.getString("checkPswd");
                String cancel = bundle.getString("cancelpay");
                String buy_res = bundle.getString("buy");
                String addorder_res = bundle.getString("order_res");
                if( result!=null&&result!=""){
                    if (result.equals("success")) {
                        cBuy.resultnum(0);
                    } else if (result.equals("fail")) {
                        cBuy.resultnum(1);
                    }
                }
                if( cancel!=null&&cancel!="") {
                    if (cancel.equals("success")) {
                        cBuy.resultnum(2);
                    }
                }
                if( buy_res!=null&&buy_res!=""){
                    if (buy_res.equals("success")) {
                        cBuy.resultnum(3);
                    } else if (buy_res.equals("fail")) {
                        cBuy.resultnum(4);
                    }
                }
                if( addorder_res!=null&&addorder_res!=""){
                    if (addorder_res.equals("success")) {
                        cBuy.resultnum(5);
                        new Thread(new deleCarRunn()).start();
                    } else if (addorder_res.equals("fail")) {
                        cBuy.resultnum(6);
                    }
                }
            }
        }
    };

    public interface  CBuy{
        void resultnum(int num);
    }
}

