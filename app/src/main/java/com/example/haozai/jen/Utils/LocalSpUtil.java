package com.example.haozai.jen.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by haozai on 2018-11-14.
 */

public class LocalSpUtil {
    private static SharedPreferences sp;
    private static String readstring,id,pswdexit,tou;
       public static String getUserId(Context context){
           if (context!=null) {
               sp = context.getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
               id = sp.getString("id", "");
           }
           return id;
    }
    public static String getLogining(Context context){
        if (context!=null) {
            sp = context.getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
            readstring = sp.getString("keyname", "");
        }
        return readstring;
    }
    public static String getPswdExit(Context context){
        if (context!=null) {
            sp = context.getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
            pswdexit = sp.getString("pswdexist", "");
        }
        return pswdexit;
    }
    public static String getTou(Context context){
        if (context!=null) {
            sp = context.getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
            tou = sp.getString("keytou", "");
        }
        return tou;
    }
    public static void setPswdExit(Context context){
        if (context!=null) {
            sp = context.getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();//获取编辑对象ID
            editor.putString("pswdexist", "exist");
            editor.commit();
        }
    }
    public static void RemoveAll(){
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("keyname");
            editor.remove("pswdexist");
            editor.remove("id");
            editor.commit();
    }
}