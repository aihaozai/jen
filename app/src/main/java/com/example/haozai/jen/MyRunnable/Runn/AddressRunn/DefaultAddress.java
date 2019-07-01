package com.example.haozai.jen.MyRunnable.Runn.AddressRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Utils.WebService;
import com.example.haozai.jen.Utils.WebServicePost;

/**
 * Created by haozai on 2018-11-15.
 */

public class DefaultAddress {
    private static String readstring,ERROR;
    private static getDefaultAddress defaultAddress;
    public void inter(getDefaultAddress  defaultAddress){
        this.defaultAddress = defaultAddress;
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
                String addressinfo= WebService.GetDefaultAddress(readstring,"1");
                bundle.putString("ye", ye);
                bundle.putString("address", addressinfo);
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
            String info = bundle.getString("address");
            if(result!=null&&info!=null) {
               defaultAddress.result(result,info);
            }else if (ERROR=="ERROR"||result==null){
               defaultAddress.result("ERROR","ERROR");
            }
        }
    };
    public interface getDefaultAddress{
        void result(String res,String str);
    }
}

