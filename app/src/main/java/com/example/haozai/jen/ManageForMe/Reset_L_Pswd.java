package com.example.haozai.jen.ManageForMe;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.ResetLoginPswd;
import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.ResetPhone;
import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.ResetPswd;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.SMSUtil;

public class Reset_L_Pswd extends AppCompatActivity implements View.OnClickListener,ResetLoginPswd.Reset,SMSUtil.Numhandler,ResetPhone.ResetUserPhone,ResetPswd.ResetUserPswd{
    private static final String TAG=Reset_L_Pswd.class.getSimpleName();
    private EditText login_pswd;
    private Button send_p,btn_sms;
    private String last_pswd,phone;
    private ImageView safety_s_back;
    private TextView p_s_tv;
    private EditText p_edit,edit_sms,resset_pay;
    private ResetLoginPswd reloginpswd;
    private SMSUtil smsUtil;
    private ResetPhone resetPhone;
    private ResetPswd resetPswd;
    private LinearLayout login_lin,phone_lin,phone_sms_lin,set_pay_lin;
    private boolean flag =false,phone_flag = false,pswd_flag = false;
    String code_number,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset__l__pswd);
        initView();
        initData();
        interfaceData();
    }

    private void initView() {
        login_pswd = (EditText) findViewById(R.id.login_password);
        send_p = (Button) findViewById(R.id.send_p_btn);
        btn_sms = (Button) findViewById(R.id.sms_btn);
        safety_s_back = (ImageView) findViewById(R.id.safety_s_back) ;
        p_s_tv = (TextView) findViewById(R.id.p_s_tv);
        edit_sms = (EditText) findViewById(R.id.edit_sms);
        p_edit = (EditText) findViewById(R.id.change_phone) ;
        login_lin = (LinearLayout) findViewById(R.id.loginpswd_lin) ;
        phone_lin = (LinearLayout) findViewById(R.id.phone_lin) ;
        phone_sms_lin = (LinearLayout) findViewById(R.id.phone_sms_lin) ;
        set_pay_lin = (LinearLayout) findViewById(R.id.set_pay_lin) ;
        resset_pay = (EditText) findViewById(R.id.resset_pay);
        login_pswd.setOnClickListener(this);
        send_p.setOnClickListener(this);
        safety_s_back.setOnClickListener(this);
        btn_sms.setOnClickListener(this);
    }

    private void initData() {
        id = LocalSpUtil.getUserId(Reset_L_Pswd.this);
        //默认是修改登录密码view
        phone = getIntent().getStringExtra("phone");
        //更换手机号判断，显示相关view
        String check= getIntent().getStringExtra("flag");
        //更换支付密码判断，显示相关view
        String resetpswd = getIntent().getStringExtra("pswd_old");
        if (check!=null&&check!=""){
            phone_flag = true;
            checkFlag();
            p_s_tv.setText("绑定手机");
        }else if(resetpswd!=null&&resetpswd!=""){
            pswd_flag = true;
            checkFlag();
            p_s_tv.setText("设置新密码");
        }else {
            //修改登陆密码的
            flag = true;
            checkFlag();
            p_s_tv.setText("设置登录密码");

        }
    }
    private void interfaceData() {
        reloginpswd = new ResetLoginPswd();
        reloginpswd.resetLogon(this);
        smsUtil = new SMSUtil();
        smsUtil.setNumInterface(this);
        resetPhone = new ResetPhone();
        resetPhone.resetPhone(this);
        resetPswd = new ResetPswd();
        resetPswd.resetPswd(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_p_btn:
                if(checkNet()&&flag) {
                    reloginpswd.settingData(phone, login_pswd.getText().toString().trim());   //登录密码
                }else if(phone_flag){
                    if(!checktext()) {
                        check_code();
                        resetPhone.settingData(id, p_edit.getText().toString().trim());  //手机号
                    }
                }else if(pswd_flag){
                    resetPswd.settingData(id,resset_pay.getText().toString().trim());
                }
                break;
            case R.id.safety_s_back:
                finish();
                break;
            case R.id.sms_btn:
                if(!checktext()&&checkNet()) {
                    SMSUtil.setPhone(p_edit.getText().toString().trim());
                    SMSUtil.getCode();
                    btn_sms.setEnabled(false);//禁止按钮的可点击性
                    mTimer.start();//开始倒计时
                }
                break;
        }
    }
    private void checkFlag(){
        if(flag){
            login_pswd.setVisibility(View.VISIBLE);
            login_lin.setVisibility(View.VISIBLE);
            phone_lin.setVisibility(View.GONE);
            set_pay_lin.setVisibility(View.GONE);
        }if(phone_flag){
            login_pswd.setVisibility(View.VISIBLE);
            phone_lin.setVisibility(View.VISIBLE);
            login_lin.setVisibility(View.GONE);
            set_pay_lin.setVisibility(View.GONE);
        }if(pswd_flag){
            login_pswd.setVisibility(View.GONE);
            login_lin.setVisibility(View.GONE);
            phone_lin.setVisibility(View.GONE);
            set_pay_lin.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void result(String res) {
        if (res.equals("success")) {
            Toast.makeText(Reset_L_Pswd.this, "修改成功", Toast.LENGTH_LONG).show();
            if(pswd_flag) {LocalSpUtil.setPswdExit(this);}
            startActivity(new Intent(Reset_L_Pswd.this,Safety.class));
        }else if (res.equals("fail")) {
            Toast.makeText(Reset_L_Pswd.this, "修改失败！", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(Reset_L_Pswd.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checktext(){
        String num = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}";
         String new_p_edit = p_edit.getText().toString().trim();
        if (!new_p_edit.matches(num)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
            return true;
        }else if(phone==p_edit.getText().toString().trim()){
            Toast.makeText(this, "请输入新的手机号码", Toast.LENGTH_LONG).show();
            return true;
        }else {
            return false;
        }
    }
    private void check_code() {
        if (judCode()) {
            SMSUtil.check(code_number);
        }
    }
    //验证码
    private boolean judCode() {
        if (TextUtils.isEmpty(edit_sms.getText().toString().trim())) {
            Toast.makeText(this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            edit_sms.requestFocus();
            return false;
        } else if (edit_sms.getText().toString().trim().length() != 6) {
            Toast.makeText(this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            edit_sms.requestFocus();
            return false;
        } else {
            code_number = edit_sms.getText().toString().trim();
            return true;
        }
    }
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(Reset_L_Pswd.this)&&!NetUtil.isMobileConnected(Reset_L_Pswd.this)){
            Toast.makeText(Reset_L_Pswd.this,"当前网络不可用",Toast.LENGTH_LONG).show();
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
            btn_sms.setText(millisUntilFinished/1000+"秒重新获取");
        }
        @Override
        public void onFinish() {
            //倒计时结束时，回调该方法
            btn_sms.setText("重新获取");
            btn_sms.setEnabled(true);
        }
    };

    @Override
    public void put(int result_num) {
        if(result_num==1){
            Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_LONG).show();
            resetPhone.settingData(id,p_edit.getText().toString().trim());
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
}
