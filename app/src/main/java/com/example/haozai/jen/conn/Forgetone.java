package com.example.haozai.jen.conn;

/**
 * Created by haozai on 2018-06-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MyRunnable.Runn.ConnRunn.CheckPhone;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.SMSUtil;
import com.example.haozai.jen.Utils.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Forgetone extends Activity implements View.OnClickListener,SMSUtil.Numhandler,CheckPhone.Cp {
    private EditText for_phone;
    private EditText for_code;
    private Button btn_next;
    private Button fg_code;
    private TextView cancel;
    private String phone_number;
    private String code_number;
    private SMSUtil smsUtil;
    private CheckPhone checkPhone;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgetone);
        initView();
        interData();

    }
    private void initView() {
        for_phone = (EditText) findViewById(R.id.for_phone);
        for_code = (EditText) findViewById(R.id.for_code);
        cancel = (TextView) findViewById(R.id.cancel);
        btn_next = (Button)  findViewById(R.id.btn_next);
        fg_code = (Button) findViewById(R.id.fg_code);
        cancel.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        fg_code.setOnClickListener(this);
    }

    private void interData() {
        smsUtil = new SMSUtil();
        smsUtil.setNumInterface(this);
        checkPhone = new CheckPhone();
        checkPhone.setinter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (judCode())
                    SMSUtil.check(code_number);
                break;
            case R.id.cancel:
                Intent regItn = new Intent(Forgetone.this, Login.class);
                startActivity(regItn);
                break;
            case R.id.fg_code:
                //去掉左右空格获取字符串
                if (judcheck())
                SMSUtil.setPhone(phone_number);
                SMSUtil.getCode();
                fg_code.setEnabled(false);
                mTimer.start();
                break;
        }
    }


    private boolean judcheck() {
        // 检测输入
        phone_number = for_phone.getText().toString().trim();
        String num = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}";
        if (!checkNet()) {
            return false;
        }
        if (TextUtils.isEmpty(phone_number)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone_number = for_phone.getText().toString().trim();
            if (for_phone.getText().toString().trim().length() != 11) {
                Toast.makeText(this, "您的电话号码位数不正确", Toast.LENGTH_LONG).show();
                return false;
            }
            if (!phone_number.matches(num)) {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    //验证码
    private boolean judCode() {
        judcheck();
        if (TextUtils.isEmpty(for_code.getText().toString().trim())) {
            Toast.makeText(this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            for_code.requestFocus();
            return false;
        } else if (for_code.getText().toString().trim().length() != 6) {
            Toast.makeText(this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            for_code.requestFocus();
            return false;
        } else {
            code_number = for_code.getText().toString().trim();
            return true;
        }
    }

    @Override
    public void put(int result_num) {
        if(result_num==1){
            Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_LONG).show();

            checkPhone.gocheck(for_phone.getText().toString());
        }else if(result_num==2){
            Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
        }else if(result_num==3){
            Toast.makeText(getApplicationContext(), "验证码已发送，请注意查收！", Toast.LENGTH_LONG).show();
        }else if(result_num==4){
            Toast.makeText(getApplicationContext(), "每日发送验证码已达上限！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void result(String result) {
        if (result=="success"){
            Intent intent = new Intent(this,Forgettwo.class);
            intent.putExtra("phone",for_phone.getText().toString().trim());
            startActivity(intent);
        }else if(result=="fail"){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("该手机号不存在！")
                    .show();
        }else {
            ToastUtil.showS(this,"连接服务器失败！");
        }
    }
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
    /**
     * 倒计时60s，使用CountDownTimer类，只需实现onTick()和onFinish()方法
     */
    private CountDownTimer mTimer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //时间间隔固定回调该方法
            fg_code.setText(millisUntilFinished/1000+"秒");
        }
        @Override
        public void onFinish() {
            //倒计时结束时，回调该方法
            fg_code.setText("重新获取");
            fg_code.setEnabled(true);
        }
    };
}
