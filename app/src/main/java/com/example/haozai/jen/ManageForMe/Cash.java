package com.example.haozai.jen.ManageForMe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.MyRunnable.Runn.BuyRunn.BuyCheck;
import com.example.haozai.jen.MyRunnable.Runn.CashRunn.checkCash;
import com.example.haozai.jen.MyRunnable.Runn.CashRunn.recharge;
import com.example.haozai.jen.PassWordView.PassWordUtil;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.ToastUtil;

public class Cash extends AppCompatActivity implements View.OnClickListener,BuyCheck.CBuy,PassWordUtil.Pw ,checkCash.getCash, recharge.getCash{
   private static final String TAG=Cash.class.getSimpleName();
    private LinearLayout chrage,showchrage;
    private ImageView chrage_go,cash_back;
    private TextView cashview;
    private EditText jine;
    private Button btn_cash;
    private boolean flag;
    private BuyCheck buycheck;
    private PassWordUtil pwu;
    private checkCash checkcash;
    private recharge rechar;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        getSupportActionBar().hide();
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        chrage = (LinearLayout) findViewById(R.id.chrage);
        showchrage = (LinearLayout) findViewById(R.id.showchrage);
        chrage_go = (ImageView) findViewById(R.id.chrage_go) ;
        btn_cash = (Button) findViewById(R.id.btn_cash);
        jine = (EditText)  findViewById(R.id.jine);
        cashview = (TextView) findViewById(R.id.cashview);
        cash_back = (ImageView) findViewById(R.id.cash_back);
        chrage.setOnClickListener(this);
        btn_cash.setOnClickListener(this);
        cash_back.setOnClickListener(this);
        jine.addTextChangedListener(mTextWatcher);
        id = LocalSpUtil.getUserId(this);
        checkligin();
        initerData();
    }

    private void checkligin() {
        String ing =LocalSpUtil.getLogining(this);
        if(ing.equals("success")){
            checkcash.settingData(id);
        }else {
            ToastUtil.showS(this,"请登录！......");
        }
    }

    private void initerData() {
        buycheck = new BuyCheck();
        buycheck.inter(this);
        pwu = new PassWordUtil();
        pwu.inter(this);
        checkcash = new checkCash();
        checkcash.inter(this);
        rechar = new recharge();
        rechar.inter(this);
    }
    private TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int len = s.toString().length();
            if (len == 1 && text.equals("0")) {
                s.clear();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chrage:
                show(!flag);
                break;
            case R.id.btn_cash:
                if(checkinput()&&checkNetwork()){
                    pwu.showView(this);
                }
                break;
            case R.id.cash_back:
                Intent i = new Intent();
                i.setClass(this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
                break;
        }
    }
    public boolean show(boolean flags){
        if(flags){
            showchrage.setVisibility(View.VISIBLE);
            chrage_go.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.upgo));
            return flag=flags;
        }else {
            showchrage.setVisibility(View.GONE);
            chrage_go.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.downgo));
            return flag=flags;
        }
    }

    @Override
    public void resultnum(int num) {
        if (num==0){
            pwu.dis();
            rechar.settingData(id,jine.getText().toString().trim());
        }else if(num==1){
            ToastUtil.showS(this,"密码错误，请重新输入！");
        }else {
            failerror();
        }
    }

    @Override
    public void pwresult(String res) {
        buycheck.PswdCheck("jen",res);
    }
    public boolean checkinput(){
        String num = jine.getText().toString().trim();
        if(num.isEmpty()){
            ToastUtil.showS(this,"请输入金额！");
            return false;
        }
        return true;
    }
    private boolean checkNetwork() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showS(this,"当前网络不可用");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void resultcash(String res) {
        if(res!=""&&!res.equals("fail")&&!res.equals("ERROR")){
            cashview.setText(res);
        }else{
            cashview.setText(0.00+"");
        }
        if(res.equals("fail")) {
            ToastUtil.showS(this,"获取余额失败！");
        }
        if(res.equals("ERROR")){
            failerror();
        }
    }
    public void failerror(){
        ToastUtil.showS(this,"连接服务器失败！");
    }

    @Override
    public void rechargeCash(String res) {
        if(res.equals("success")){
            ToastUtil.showS(this,"充值成功！");
            checkcash.settingData(id);
        }else if(res.equals("fail")){
            ToastUtil.showS(this,"充值失败！");
        }else if(res.equals("ERROR")){
            failerror();
        }
    }
}
