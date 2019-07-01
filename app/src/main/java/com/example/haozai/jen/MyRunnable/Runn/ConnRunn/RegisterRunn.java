package com.example.haozai.jen.MyRunnable.Runn.ConnRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebService;

/**
 * Created by haozai on 26/11/2018.
 */

public class RegisterRunn {
    private static String et_name,et_pwd,et_apwd,et_phone,result,ERROR="";
    private static Reg reg;
    public void setRegister(Reg reg) {
        this.reg = reg;
    }
    public static void goReg(String name, String psw,String pw,String ph) {
        et_name =name;
        et_pwd = psw;
        et_apwd = pw;
        et_phone = ph;
        new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            Bundle bundle = new Bundle();
            try {
                String check = WebService.register(et_name,et_pwd,et_apwd,et_phone);
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
                    reg.Regresult("success");
                } else if (result.equals("fail")) {
                    reg.Regresult("fail");
                }
            }else if (ERROR=="ERROR"||result==null){
                reg.Regresult("ERROR");
            }
        }
    };
    public interface Reg {
        void Regresult(String result);
    }
}
