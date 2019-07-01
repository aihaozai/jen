package com.example.haozai.jen.conn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.ResetLoginPswd;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Forgettwo extends Activity implements View.OnClickListener,ResetLoginPswd.Reset {
    protected static final int ERROR = 1;
    private EditText et_gai_pwd;
    private EditText et_gai_apwd;
    private Button btn_con;
    private TextView two_cancel;
    // 创建等待框
    private SweetAlertDialog pDialog;
    private ResetLoginPswd reloginpswd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgettwo);
        et_gai_pwd = (EditText) findViewById(R.id.et_gai_pwd);
        et_gai_apwd = (EditText) findViewById(R.id.et_gai_apwd);
        two_cancel = (TextView) findViewById(R.id.two_cancel);
        btn_con = (Button) findViewById(R.id.btn_con);;
        two_cancel.setOnClickListener(this);
        btn_con.setOnClickListener(this);
        reloginpswd = new ResetLoginPswd();
        reloginpswd.resetLogon(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_con:
                // 检测输入
                final String new_pwd = et_gai_pwd.getText().toString().trim();
                final String new_apwd = et_gai_apwd.getText().toString().trim();

                if (!checkNet())
                if (TextUtils.isEmpty(new_pwd)) {
                    Toast.makeText(this, "请输入密码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (TextUtils.isEmpty(new_apwd)) {
                    Toast.makeText(this, "请输入新密码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (!new_pwd.equals(new_apwd)) {
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return ;
                }
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("正在修改....");
                pDialog.setCancelable(false);
                pDialog.show();
                String phone= getIntent().getStringExtra("phone");
                reloginpswd.settingData(phone, et_gai_pwd.getText().toString());

                break;
            case R.id.two_cancel:
                Intent reg = new Intent(Forgettwo.this, Login.class);
                startActivity(reg);
                break;

        }
        ;
    }


    @Override
    public void result(String res) {
        if (res.equals("success")) {
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("修改成功!")
                    .setContentText("点击返回登陆页面!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(Forgettwo.this,Login.class));
                        }
                    })
                    .show();
        }else if (res.equals("fail")) {
            ToastUtil.showS(this, "修改失败！");
            pDialog.dismiss();
        }else {
            ToastUtil.showS(this, "服务器连接失败!");
            pDialog.dismiss();
        }
    }

    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showS(this, "当前网络不可用");
            return false;
        }else {
            return true;
        }
    }
}