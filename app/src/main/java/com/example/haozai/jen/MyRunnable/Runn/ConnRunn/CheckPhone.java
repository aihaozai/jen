package com.example.haozai.jen.MyRunnable.Runn.ConnRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebService;

/**
 * Created by haozai on 26/11/2018.
 */

public class CheckPhone {
    private static String et_phone,result,ERROR="";
    private static Cp cp;
    public void setinter(Cp cp) {
        this.cp = cp;
    }
    public static void gocheck(String ph) {
        et_phone = ph;
        new Thread(new MyRunnable()).start();
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                String check = WebService.forone(et_phone);
                bundle.putString("check", check);
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
            if(result!=null) {
                if (result.equals("success")) {
                    cp.result("success");
                } else if (result.equals("fail")) {
                    cp.result("fail");
                }
            }else if (ERROR=="ERROR"||result==null){
                cp.result("ERROR");
            }
        }
    };
    public interface Cp {
        void result(String result);
    }
}
