package com.example.haozai.jen.MyRunnable.Runn.OrderRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.WebServicePost;

import java.util.List;

/**
 * Created by haozai on 26/11/2018.
 */

public class OrderList {
    private static String userid,ordering,result,ERROR="";
    private static Check check;
    private static List<OrderInfo> goodslist;
    public void setCheckInterface(Check check) {
        this.check = check;
    }
    public static void getgoodlist(String reads, String ing) {
        userid = reads;
        ordering = ing;
        new Thread(new MyRunnable()).start();
    }
    public static void getalllist(String reads) {
        userid = reads;
        new Thread(new allRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                String orderlist = WebServicePost.Orderlist(userid,ordering);
                bundle.putString("check", orderlist);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putString("ERROR", "ERROR");
                message.setData(bundle);
                checkHandler.sendMessage(message);
            }
        }
    }
     private static class allRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                String orderlist = WebServicePost.AllOrderlist(userid);
                bundle.putString("list", orderlist);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putString("ERROR", "ERROR");
                message.setData(bundle);
                checkHandler.sendMessage(message);
            }
        }
    }
    private static Handler checkHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            bundle = msg.getData();
            ERROR= bundle.getString("ERROR");
            result = bundle.getString("check");
            String alllist = bundle.getString("list");
            if(result!=null&&result!="") {
                goodslist = all_json.jsonorderList(result);
                if(result.length()==6){
                    check.ortherResult("six");
                }else {
                    check.resultList(goodslist);
                }
            }
            if(alllist!=null&&alllist!="") {
                goodslist = all_json.allorderList(alllist);
                if(alllist.length()==6){
                    check.ortherResult("six");
                }else {
                    check.resultList(goodslist);
                }
            }
            if(ERROR=="ERROR"||result==null){
                check.ortherResult("ERROR");
            }
        }
    };
    public interface Check {
        void resultList(List<OrderInfo> goodslists);
        void ortherResult(String orther);
    }
}
