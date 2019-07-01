package com.example.haozai.jen.MyRunnable.Runn.CashRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 28/11/2018.
 */

public class checkCash {
    private static String readstring,ERROR;
    private static getCash Cash;
    public void inter(getCash Cash){
        this.Cash = Cash;
    }
    public static void settingData(String str) {
        readstring = str;
        new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Message message = new Message();
            try {
                String  ye = WebServicePost.checkYe(readstring);
                bundle.putString("ye", ye);
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
            String result = bundle.getString("ye");
            if(result!=null ){
                Cash.resultcash(result);
            }else if (ERROR=="ERROR"||result==null){
               Cash.resultcash("ERROR");
            }
        }
    };
    public interface getCash{
        void resultcash(String res);
    }
}
