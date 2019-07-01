package com.example.haozai.jen.Utils;


import android.os.Handler;
import android.os.Message;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by haozai on 2018-11-14.
 */

public class SMSUtil {
    private static final String TAG="SMSUtil";
    private static String Phone;
    private static int result_num;
    private static Numhandler numhandler;
    public void setNumInterface(Numhandler numhandler) {
        this.numhandler = numhandler;
    }
    public static void setPhone(String phone){
        Phone = phone;
    }
    public static void getCode(){
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getVerificationCode("86", Phone);
    }
    public static void check(String code_number){
        SMSSDK.submitVerificationCode("86", Phone, code_number);
    }
    public static void Clear(){
        SMSSDK.unregisterEventHandler(eventHandler);
    }
   private static EventHandler eventHandler = new EventHandler(){
        @Override
        public void afterEvent(int event, int res, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = res;
            msg.obj = data;
            checksmshandler.sendMessage(msg);
        }
    };
    private static Handler checksmshandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int res = msg.arg2;
            //提交验证码成功
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                if (res == SMSSDK.RESULT_COMPLETE) {
                    numhandler.put(1);
                }else {
                    numhandler.put(2);
                }
            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (res == SMSSDK.RESULT_COMPLETE) {
                    numhandler.put(3);
                }else {
                    numhandler.put(4);
                }
            }
        }
    };
    public interface Numhandler {
        void put(int result_num);
    }
}
