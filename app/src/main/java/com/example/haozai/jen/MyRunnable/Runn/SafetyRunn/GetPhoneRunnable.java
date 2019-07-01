package com.example.haozai.jen.MyRunnable.Runn.SafetyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.haozai.jen.Utils.WebServicePost;

import static android.content.ContentValues.TAG;

/**
 * Created by haozai on 2018-11-15.
 */

public class GetPhoneRunnable {
    private static String id,ERROR,result;
    private static GetP getP;
    public void setGetP(GetP getP){
        this.getP = getP;
    }
    public static void goGet(String str) {
        id = str;
        new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Message message = new Message();
            try {
                String phone = WebServicePost.getphone(id);
                Log.i(TAG,phone+"aaaaaaaaaaaaaaaaa");
                bundle.putString("phone", phone);
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
            result = bundle.getString("phone");
            if(result!=null&&result!="") {
                getP.result(result);
            }else if (ERROR=="ERROR"||result==null){
                getP.result("ERROR");
            }
        }
    };
    public interface GetP{
        void result(String res);
    }
    }


