package com.example.haozai.jen.MyRunnable.Runn.SafetyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 2018-11-15.
 */

public class CheckPswdUtil {
    private static final String TAG=" CheckPswdUtil";
    private static String checkPswd,readstring,pswd,result,pswd_result,ERROR="";
    private static CheckPswd check;
    public void setCheckPInterface(CheckPswd check) {
        this.check = check;
    }
    public static void check(String reads, String psw) {
        readstring = reads;
        pswd = psw;
         new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                checkPswd = WebServicePost.CheckPswd(readstring,pswd);
                bundle.putString("check", checkPswd);
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
                    check.p_result("success");
                } else if (result.equals("fail")) {
                    check.p_result("fail");
                }
            }else if (ERROR=="ERROR"||result==null){
                check.p_result("ERROR");
            }
        }
    };
    public interface CheckPswd {
        void p_result(String result_pswd);
    }
}