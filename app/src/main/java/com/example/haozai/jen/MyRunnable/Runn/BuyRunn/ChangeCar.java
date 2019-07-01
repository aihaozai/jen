package com.example.haozai.jen.MyRunnable.Runn.BuyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 2018-11-15.
 */

public class ChangeCar {
    private static String userid,url,change,ERROR;
    private static Del del;
    public void inter( Del del){
        this.del= del;
    }
    public static void settingData(String str,String strr) {
        userid = str;
        url = strr;
        new Thread(new deleCarRunn()).start();
    }
    public static void changgeData(String str,String strr,String changeing) {
        userid = str;
        url = strr;
        change = changeing;
        new Thread(new ChangeCarRunn()).start();
    }
    /**
    * 删除商品
    */

    private static class deleCarRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  dele_res = WebServicePost.deleoneCar(userid,url);
            } catch (Exception e) {
                e.printStackTrace();
                setERROR();
            }
        }
    }
    /**
    * 改变商品数量
    */
    private static class ChangeCarRunn implements Runnable {
        @Override
        public void run() {
            try {
                String  res = WebServicePost.changeCar(userid,url,change);
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
                del.result(-1);
            }
        }
    };
public interface Del {
    void result(int num);
}
}

