package com.example.haozai.jen.MyRunnable.Runn.FreagmentNum;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 2018-11-18.
 */

public class FouthRunn {
    private static String id,ERROR;
    private static GetNum getNum;
    public void inter(GetNum getNum){
        this.getNum = getNum;

    }
    public static void settingData(String str) {
        id = str;
        new Thread(new MyRunnable()).start();
    }
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Message message = new Message();
            try {
                String result_info = WebServicePost.infor(id);
                String num_car= WebServicePost.checkcar(id);
                String num_daifu= WebServicePost.checkall(id,"daiing");
                String num_accept= WebServicePost.checkall(id,"accepting");
                bundle.putString("result", result_info);
                bundle.putString("num_car", num_car);
                bundle.putString("num_daifu", num_daifu);
                bundle.putString("num_accept", num_accept);
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

            String ress = bundle.getString("result");
            String  num_a = bundle.getString("num_daifu");
            String  num_c = bundle.getString("num_car");
            String  num_b = bundle.getString("num_accept");
            if(ress!=null) {
                getNum.result(ress,num_a,num_c,num_b);
            }else if (ERROR=="ERROR"||ress==null){
                getNum.result("ERROR","ERROR","ERROR","ERROR");
            }
        }
    };
    public interface GetNum{
        void result(String res,String a,String c,String b);
    }
}

