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

import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.CheckPswdUtil;
import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.GetPhoneRunnable;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.SMSUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.conn.Login;
import com.mob.MobSDK;

public class Log_Pswd_Manage extends AppCompatActivity implements View.OnClickListener,DialogUtil.Actiongo,SMSUtil.Numhandler,CheckPswdUtil.CheckPswd
                                                                                ,GetPhoneRunnable.GetP{
    private static final String TAG="Log_Pswd_Manage";
    private Button send_sms_bt,q_send_sms,resend,q_send_pswd;
    private LinearLayout q_sms_v,q_pswd_v;
    private DialogUtil dialogUtil;
    private TextView choose_ways_safety,p_s_tv;
    private EditText editsms,input_s_pswd;
    private ImageView safety_back;
    private boolean choode_flag = true;
    private boolean flag = false;
    private boolean old_pswd_show =false;
    String readstring,id, pswdexit,maskNumber,num_end,result,code_number,phone,choose_pswd;
    protected static final int ERROR = -1;
    private GetPhoneRunnable phoneRunn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log__pswd__manage);
        MobSDK.init(this, "2655d8bd164c4", "1cc86a3bb018d61c1a53632f2f10bcb5");
        initView();
        viewListener();
        diaLogView();
        interData();
        initData();
    }
    private void initView() {
        choose_ways_safety = (TextView) findViewById(R.id.choose_ways_safety);
        p_s_tv= (TextView) findViewById(R.id.p_s_tv);
        send_sms_bt = (Button) findViewById(R.id.send_sms_btn);
        q_send_sms = (Button) findViewById(R.id.q_send_sms);
        resend = (Button) findViewById(R.id.resend);
        q_send_pswd = (Button) findViewById(R.id.q_send_pswd);
        q_sms_v = (LinearLayout) findViewById(R.id.q_sms_v);
        q_pswd_v = (LinearLayout) findViewById(R.id.q_pswd_v);
        editsms = (EditText) findViewById(R.id.editsms);
        input_s_pswd = (EditText) findViewById(R.id.input_s_pswd);
        safety_back= (ImageView) findViewById(R.id.safety_back);
    }
    private void viewListener() {
        choose_ways_safety.setOnClickListener(this);
        send_sms_bt.setOnClickListener(this);
        q_send_sms.setOnClickListener(this);
        q_sms_v.setOnClickListener(this);
        resend.setOnClickListener(this);
        q_pswd_v.setOnClickListener(this);
        q_send_pswd.setOnClickListener(this);
        safety_back.setOnClickListener(this);
    }
    private void diaLogView() {
        dialogUtil = new DialogUtil();
        dialogUtil.setActionInterface(this);
        dialogUtil.showRightDialog(this);
    }
    private void interData() {
        SMSUtil smsUtil = new SMSUtil();
        smsUtil.setNumInterface(this);
        CheckPswdUtil checkPswdUtil = new CheckPswdUtil();
        checkPswdUtil.setCheckPInterface(this);
        phoneRunn = new GetPhoneRunnable();
        phoneRunn.setGetP(this);
    }
    private void initData() {
        //账号与安全里的三个修改功能共用一个acticity
        //默认显示修改登陆密码view，以下判断其他方式进来的view
        phone = getIntent().getStringExtra("phone"); //更手机号码方式进来的
        choose_pswd = getIntent().getStringExtra("pswd");   //更密码方式进来的
            if (phone!=null&&phone!=""&&phone.equals("phone")) {
                p_s_tv.setText("绑定手机");
            }else if(choose_pswd!=null&&choose_pswd!=""&&choose_pswd.equals("pswd")){
                dialogUtil.showoldPswd("通过旧支付密码验证");
                old_pswd_show = true;
     }
        //从本地获取登录信息
        readstring = LocalSpUtil.getLogining(Log_Pswd_Manage.this);
        id  = LocalSpUtil.getUserId(Log_Pswd_Manage.this);
        pswdexit = LocalSpUtil.getPswdExit(Log_Pswd_Manage.this);
        if(!pswdexit.equals("exist")){
                dialogUtil.setViewVisibilty();
        }
        if (!readstring.equals("success")) {
            startActivity(new Intent(Log_Pswd_Manage.this, Login.class));
        } else if(checkNetwork()){
            phoneRunn.goGet(id);
        }
    }

    @Override
    public void result(String res) {
        result = res;
        if(result!="ERROR") {
            maskNumber = result.substring(0, 3) + "****" + result.substring(7, result.length());
            num_end = "手机号[" + maskNumber + "]";
            choose_ways_safety.setText(num_end);
            dialogUtil.setPhone(num_end);
        }else {
            handler(result);
        }
    }
    private void send() {
        if (choode_flag){
            send_sms_bt.setVisibility(View.GONE);
            //String num = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}";
            if (result==null) {
                send_sms_bt.setVisibility(View.VISIBLE);
                Toast.makeText(Log_Pswd_Manage.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
            }else {
                q_sms_v.setVisibility(View.VISIBLE);
                resend.setVisibility(View.VISIBLE);
                q_pswd_v.setVisibility(View.GONE);
                //通过SMSSDK发送短信
                SMSUtil.setPhone(result);
                SMSUtil.getCode();
                resend.setEnabled(false);//禁止按钮的可点击性
                mTimer.start();//开始倒计时
            }
        }else {
            send_sms_bt.setVisibility(View.GONE);
            q_sms_v.setVisibility(View.GONE);
            resend.setVisibility(View.GONE);
            q_pswd_v.setVisibility(View.VISIBLE);
        }
    }
    private void check_code() {
        if (judCode()) {
                SMSUtil.check(code_number);
            }
        }
    //验证码
    private boolean judCode() {
        if (TextUtils.isEmpty(editsms.getText().toString().trim())) {
            Toast.makeText(this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            editsms.requestFocus();
            return false;
        } else if (editsms.getText().toString().trim().length() != 6) {
            Toast.makeText(this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            editsms.requestFocus();
            return false;
        } else {
            code_number = editsms.getText().toString().trim();
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_sms_btn:
                if (checkNetwork()) {
                    send();
                }
                break;
            case R.id.choose_ways_safety:
                dialogUtil.show();
                break;
            case R.id.q_send_sms:
                check_code();
                break;
            case R.id.resend:
                SMSUtil.setPhone(result);
                SMSUtil.getCode();
                resend.setEnabled(false);//禁止按钮的可点击性
                mTimer.start();//开始倒计时
                break;
            case R.id.q_send_pswd:
                if(input_s_pswd.getText().toString().trim().length() == 6) {
                    CheckPswdUtil.check(id, input_s_pswd.getText().toString().trim());
                }else {
                    Toast.makeText(this, "您的密码位数不正确", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.safety_back:
                finish();
                break;
        }
    }
    /**
     * 倒计时60s，使用CountDownTimer类，只需实现onTick()和onFinish()方法
     */
    private CountDownTimer mTimer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //时间间隔固定回调该方法
            resend.setText(millisUntilFinished/1000+"秒重新获取");
        }
        @Override
        public void onFinish() {
            //倒计时结束时，回调该方法
            resend.setText("重新获取");
            resend.setEnabled(true);
        }
    };
    @Override
    public void setflag(boolean flag) {
        choode_flag = flag;
        send_sms_bt.setVisibility(View.VISIBLE);
        q_sms_v.setVisibility(View.GONE);
        resend.setVisibility(View.GONE);
        q_pswd_v.setVisibility(View.GONE);
        if (!flag&&!old_pswd_show){
            choose_ways_safety.setText("支付密码验证");
        }else {
            choose_ways_safety.setText("旧支付密码验证");
        }
    }
    @Override
    public void p_result(String result_pswd) {
        if (result_pswd.equals("success")){
            smsgoactivity();
        }else if(result_pswd.equals("fail")){
            Toast.makeText(Log_Pswd_Manage.this, "密码错误！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(Log_Pswd_Manage.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void put(int result_num) {
        if(result_num==1){
            Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_LONG).show();
            smsgoactivity();
        }else if(result_num==2){
            Toast.makeText(Log_Pswd_Manage.this, "验证码不正确", Toast.LENGTH_SHORT).show();
        }else if(result_num==3){
            Toast.makeText(getApplicationContext(), "验证码已发送，请注意查收！", Toast.LENGTH_LONG).show();
        }else if(result_num==4){
            Toast.makeText(Log_Pswd_Manage.this, "每日发送验证码已达上限！", Toast.LENGTH_SHORT).show();
        }
    }
    //进入修改activity
    private void smsgoactivity(){
        Intent intent = new Intent(Log_Pswd_Manage.this, Reset_L_Pswd.class);
        intent.putExtra("phone", result);
        //更改手机号验证f判断
        intent.putExtra("flag", phone);
        if(old_pswd_show) {
                //更改旧密码判断
            intent.putExtra("pswd_old", "pswd_old");
        }
        startActivity(intent);
    }
    private void handler( String str){
        if (str=="ERROR"){
            Toast.makeText(Log_Pswd_Manage.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
        }
    }
    // 检测网络
    private boolean checkNetwork() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showS(this,"当前网络不可用");
            return false;
        }else {
            return true;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSUtil.Clear();
    }
}
