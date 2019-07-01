package com.example.haozai.jen.MyRunnable.Runn.BuyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.Utils.WebServicePost;
import java.util.List;

/**
 * Created by haozai on 24/11/2018.
 */

public class SendinfoRunn {
    private static String readstring,pswd,cash,count,pay,yunfei,order,ERROR;
    private static List<Books> list;
    private static CBuy cBuy;
    public void inter( CBuy  cBuy){
        this.cBuy= cBuy;
    }
    public static void PswdCheck(String str,String strr) {
        readstring = str;
        pswd = strr;
        new Thread(new MyRunnable()).start();
    }
    public static void ordering(String name,List<Books> bean,String counts,String str,String strr,String ordering) {
        readstring = name;
        list =bean;
        count =counts;
        pay = str;
        yunfei = strr;
        order = ordering;
        new Thread(new OrderRunn()).start();
    }
    public static void goBuy(String str,String strr,String strrr) {
        readstring = str;
        pswd = strr;
        cash = strrr;
        new Thread(new goBuyRunn()).start();
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
     * 付款状态
     */
    private static class OrderRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  canelpay = WebServicePost.SendDaiFu(readstring,list.get(0).getBooknum(),list.get(0).getName(),list.get(0).getAuthor(),
                        list.get(0).getPress(),list.get(0).getPrice(),count, pay,yunfei,order);
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
                if( result!=null&&result!=""){
                    if (result.equals("success")) {
                            cBuy.resultnum(0);
                    } else if (result.equals("fail")) {
                            cBuy.resultnum(1);
                    }
                }
                if( cancel!=null&&cancel!="") {
                    if (cancel.equals("success")) {
                        if (order=="daiing") {
                            cBuy.resultnum(2);
                        }else {
                            cBuy.resultnum(5);
                        }
                    }
                }
                if( buy_res!=null&&buy_res!=""){
                    if (buy_res.equals("success")) {
                        cBuy.resultnum(3);
                    } else if (buy_res.equals("fail")) {
                        cBuy.resultnum(4);
                    }
                }
            }
        }
    };

    public interface  CBuy{
        void resultnum(int num);
    }
}
