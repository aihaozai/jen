package com.example.haozai.jen.MyRunnable.Runn.BuyRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.Utils.WebServicePost;

import java.util.List;

/**
 * Created by haozai on 24/11/2018.
 */

public class AddCar {
    private static String userid,ERROR;
    private static List<Books> list;
    private static Add add;
    public void inter(Add add){
        this.add= add;
    }
    public static void settingData(String str, List<Books> bookslist) {
        userid = str;
        list = bookslist;
        new Thread(new addCarRunn()).start();
    }
    /**
     * 加入购物车
     */

    private static class addCarRunn implements Runnable {
        @Override
        public void run() {
            try {
                String add_result = WebServicePost.AddCar(list.get(0).getBooknum(), list.get(0).getName(), list.get(0).getAuthor(),
                        list.get(0).getPress(), list.get(0).getPrice(), userid);
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("add_result", add_result);
                message.setData(bundle);
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                setERROR();
            }
        }
    }
    public static void setERROR(){
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putString("ERROR", "ERROR");
        message.setData(bundle);
        checkHandler.sendMessage(message);
    }
    private static Handler checkHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            bundle = msg.getData();
            ERROR= bundle.getString("ERROR");
            String result= bundle.getString("add_result");
            if (ERROR=="ERROR"){
                add.resultnum(-1);
            }
            if( result!=null&&result!=""){
                if (result.equals("success")) {
                    add.resultnum(0);
                } else if (result.equals("fail")) {
                    add.resultnum(1);
                }
            }
        }
    };
    public interface Add {
        void resultnum(int num);
    }
}
