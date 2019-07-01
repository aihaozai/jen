package com.example.haozai.jen.MyRunnable.Runn.SafetyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 2018-11-15.
 */

public class ResetPhone {
    private static String newphone,id,ERROR,result;
    private static ResetUserPhone reset;
    public void resetPhone(ResetUserPhone reset){
        this.reset = reset;
    }
    public static void settingData(String str,String strr) {
        id = str;
        newphone = strr;
        new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Message message = new Message();
            try {
                String resultWeb = WebServicePost.resetPhone(id,newphone);
                bundle.putString("result", resultWeb);
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
            result = bundle.getString("result");
            if(result!=null) {
               reset.result(result);
            }else if (ERROR=="ERROR"||result==null){
                reset.result("ERROR");
            }
        }
    };
    public interface ResetUserPhone{
        void result(String res);
    }
}

