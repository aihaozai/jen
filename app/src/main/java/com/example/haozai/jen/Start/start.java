package com.example.haozai.jen.Start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.haozai.jen.R;

import java.io.OutputStream;

public class start extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private OutputStream os;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //android 7.0 MODE_WORLD_READABLE替换MODE_PRIVATE
        preferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = preferences.getInt("count", 0);
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),daohang.class);
            startActivity(intent);
            this.finish();
        }
        if (count != 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),SplashActivity.class);
            startActivity(intent);
            this.finish();
        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();

    }
}