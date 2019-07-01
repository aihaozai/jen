package com.example.haozai.jen.MyRunnable.Runn.BooksRunn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.WebService;

import java.util.List;

/**
 * Created by haozai on 23/11/2018.
 */

public class BooksinfoRunn {
        private static String Url,ERROR;
        private static List<Books> list;
        private static Info info;
        public void inter(Info info){
            this.info = info;
        }
        public static void settingData(String str) {
            Url = str;
            new Thread(new MyRunnable()).start();
        }
        private static class MyRunnable implements Runnable {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                Message message = new Message();
                try {
                    String  info = WebService.booksinfo(Url);
                    bundle.putString("info", info);
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
                String result = bundle.getString("info");
                if(result!=null) {
                    list = all_json.jsonbuyinfoList(result);
                    info.getInfo(list);
                }else if (ERROR=="ERROR"||result==null){
                   info.failinfo("ERROR");
                }
            }
        };
        public interface Info{
            void getInfo(List<Books> lists);
            void failinfo(String str);
        }
}
