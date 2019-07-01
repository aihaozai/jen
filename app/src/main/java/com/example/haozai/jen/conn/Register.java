package com.example.haozai.jen.conn;

/**
 * Created by haozai on 2018-06-13.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MyRunnable.Runn.ConnRunn.CheckPhone;
import com.example.haozai.jen.MyRunnable.Runn.ConnRunn.CheckUsername;
import com.example.haozai.jen.MyRunnable.Runn.ConnRunn.RegisterRunn;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.SMSUtil;
import com.example.haozai.jen.Utils.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends Activity implements View.OnClickListener ,SMSUtil.Numhandler,RegisterRunn.Reg,CheckPhone.Cp,CheckUsername.Cu{
    protected static final int ERROR = 1;
    private EditText et_pwd, et_name, et_apwd, et_phone, et_code;
    private Button btn_register;
    private Button code;
    private TextView cancel;
    private boolean flag = true ,userflag;
    private String phone_number,code_number;
    private SMSUtil smsUtil;
    private RegisterRunn registerRunn;
    private CheckPhone checkPhone;
    private CheckUsername checkUsername;
    private SweetAlertDialog pDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        initView();
        interData();
    }

    private void initView() {
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_name = (EditText) findViewById(R.id.et_name);
        et_apwd = (EditText) findViewById(R.id.et_apwd);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_register = (Button) findViewById(R.id.btn_register);
        code = (Button) findViewById(R.id.code);
        cancel = (TextView) findViewById(R.id.cancel);
        btn_register.setOnClickListener(this);
        cancel.setOnClickListener(this);
        code.setOnClickListener(this);
    }
    private void interData() {
        smsUtil = new SMSUtil();
        smsUtil.setNumInterface(this);
        registerRunn = new RegisterRunn();
        registerRunn.setRegister(this);
        checkPhone = new CheckPhone();
        checkPhone.setinter(this);
        checkUsername = new CheckUsername();
        checkUsername.setinter(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (judCode()) {
                    userflag=true;
                    checkUsername.gocheck(et_name.getText().toString().trim());
                    SMSUtil.check(code_number);
                }
                break;
            case R.id.cancel:
                Intent regItn = new Intent(Register.this, Login.class);
                startActivity(regItn);
                break;
            case R.id.code:
                if (judcheck()) {
                    userflag=false;
                    checkUsername.gocheck(et_name.getText().toString().trim());
                }
                break;
        }
    }


    private boolean judcheck() {
        // 检测输入
        final String new_name = et_name.getText().toString().trim();
        final String new_pwd = et_pwd.getText().toString().trim();
        final String new_apwd = et_apwd.getText().toString().trim();
        phone_number = et_phone.getText().toString().trim();
        String num = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}";
        // 检测网络
        if (!checkNet()) {
            return false;
        }

        if (TextUtils.isEmpty(new_name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(new_pwd)) {
            Toast.makeText(this, "请输入密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(new_apwd)) {
            Toast.makeText(this, "请输入新密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!new_pwd.equals(new_apwd)) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone_number)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone_number = et_phone.getText().toString().trim();
            if (et_phone.getText().toString().trim().length() != 11) {
                Toast.makeText(this, "您的电话号码位数不正确", Toast.LENGTH_LONG).show();
                return false;
            }
            if (!phone_number.matches(num)) {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                return false;
            }
        }    /*
             *中国电信号段 133、149、153、173、177、180、181、189、199
             * 中国联通号段 130、131、132、145、155、156、166、175、176、185、186
             *中国移动号段 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
             */
        return true;
    }

    //验证码
    private boolean judCode() {
        judcheck();
        if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
            Toast.makeText(this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_code.getText().toString().trim().length() != 6) {
            Toast.makeText(this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            return false;
        } else {
            code_number = et_code.getText().toString().trim();
            return true;
        }
    }

    @Override
    public void put(int result_num) {
        if(result_num==1){
            Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_LONG).show();
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("正在注册....");
            pDialog.setCancelable(false);
            pDialog.show();
            checkPhone.gocheck( et_phone.getText().toString().trim());
        }else if(result_num==2){
            Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
        }else if(result_num==3){
            Toast.makeText(getApplicationContext(), "验证码已发送，请注意查收！", Toast.LENGTH_LONG).show();
        }else if(result_num==4){
            Toast.makeText(getApplicationContext(), "每日发送验证码已达上限！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag) {
            SMSUtil.Clear();
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

    @Override
    public void result(String result) {
        if (result=="success"){
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("该手机号已被注册")
                    .show();
        }else if(result=="fail"){
            registerRunn.goReg(et_name.getText().toString().trim(),et_pwd.getText().toString().trim(),
                    et_apwd.getText().toString().trim(), et_phone.getText().toString().trim());
        }else {
            pDialog.dismiss();
            ToastUtil.showS(this,"连接服务器失败！");
        }
    }

    @Override
    public void Regresult(String result) {
        if (result=="success"){
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("注册成功!")
                    .setContentText("点击返回登陆页面!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(Register.this,Login.class));
                        }
                    })
                    .show();
            ToastUtil.showS(this,"注册成功！");
        }else if(result=="fail"){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("注册失败!")
                    .show();
            ToastUtil.showS(this,"注册失败！");
        }else {

            ToastUtil.showS(this,"连接服务器失败！");
        }
    }
    /**
     * 倒计时60s，使用CountDownTimer类，只需实现onTick()和onFinish()方法
     */
    private CountDownTimer mTimer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //时间间隔固定回调该方法
            code.setText(millisUntilFinished/1000+"秒");
        }
        @Override
        public void onFinish() {
            //倒计时结束时，回调该方法
            code.setText("重新获取");
            code.setEnabled(true);
        }
    };

    @Override
    public void resultCu(String result) {
        if (result=="success"){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("该用户名已经存在")
                    .show();
        }else if(result=="fail"){
            if (!userflag){
                getsms();
            }
        }else {
            ToastUtil.showS(this,"连接服务器失败！");
        }

    }
    public void getsms(){
        SMSUtil.setPhone(phone_number);
        SMSUtil.getCode();
        code.setEnabled(false);//禁止按钮的可点击性
        mTimer.start();//开始倒计时
    }
}