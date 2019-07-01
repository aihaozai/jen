package com.example.haozai.jen.conn;

/**
 * Created by haozai on 2018-06-13.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.WebService;
import com.example.haozai.jen.Utils.WebServicePost;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends Activity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button btn_login;
    private TextView register_tv;
    private TextView forget_tv;
    protected static final int ERROR = 1;
    //定义变量
    private SharedPreferences sp;
    // 创建等待框
    private SweetAlertDialog pDialog;
    // 返回的数据
    String info,setpswd;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        //控件
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        register_tv = (TextView) findViewById(R.id.register_tv);
        forget_tv = (TextView) findViewById(R.id.forget_tv);
        // 设置按钮监听器
        btn_login.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        forget_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                // 检测输入

                final String na = username.getText().toString().trim();
                final String pwd = password.getText().toString().trim();
                if (TextUtils.isEmpty(na)) {
                    Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 检测网络，无法检测wifi
                if (!checkNet()){
                    return;
                }
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("正在登陆....");
                pDialog.setCancelable(false);
                pDialog.show();
                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
                break;
            case R.id.register_tv:
                Intent regItn = new Intent(Login.this, Register.class);
                startActivity(regItn);
                break;
            case R.id.forget_tv:
                Intent forgeto = new Intent(Login.this, Forgetone.class);
                startActivity(forgeto);
                break;
        }
        ;
    }

    // 检测网络
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    // 子线程接收数据，主线程修改数据
    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {

                info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
                setpswd = WebServicePost.pswdexist(username.getText().toString().trim());
                if(info==null) {
                    Toast.makeText(Login.this, info, Toast.LENGTH_LONG).show();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //去掉提示框
                        pDialog.dismiss();
                        //登录成功
                        if (!info.equals("fail")) {
                            Toast.makeText(Login.this, "登陆成功！", Toast.LENGTH_LONG).show();
                            //保存数据到本地
                            sp = getSharedPreferences("Datadefault", Context.MODE_PRIVATE);//创建对象，Datadefault是储存数据的对象名
                            SharedPreferences.Editor editor = sp.edit();//获取编辑对象ID
                            //记录登陆状态
                            editor.putString("keyname", "success");
                            //保存用户ID
                            editor.putString("id", info);
                            //检查支付密码是否存在
                            editor.putString("pswdexist", setpswd);
                            editor.commit();//提交保存修改
                            startActivity(new Intent(Login.this, MainActivity.class));

                        }
                        //登录失败
                        else if (info.equals("fail")) {
                            Toast.makeText(Login.this, "账号或密码错误！", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = ERROR;
                loginHandler.sendMessage(msg);

            }
        }

        Handler loginHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ERROR:
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "连接服务器失败", Toast.LENGTH_LONG).show();
                        break;
                }
            }
      };
    }

}

