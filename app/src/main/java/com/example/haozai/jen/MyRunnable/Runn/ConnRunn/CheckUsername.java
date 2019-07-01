package com.example.haozai.jen.MyRunnable.Runn.ConnRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 26/11/2018.
 */

public class CheckUsername {
    private static String et_user,result,ERROR="";
    private static Cu cu;
    public void setinter(Cu cu) {
        this.cu = cu;
    }
    public static void gocheck(String str) {
        et_user = str;
        new Thread(new MyRunnable()).start();
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                String check = WebServicePost.checkname(et_user);
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
                    cu.resultCu("success");
                } else if (result.equals("fail")) {
                    cu.resultCu("fail");
                }
            }else if (ERROR=="ERROR"||result==null){
                cu.resultCu("ERROR");
            }
        }
    };
    public interface Cu {
        void resultCu(String result);
    }
}
