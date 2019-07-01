package com.example.haozai.jen.buy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haozai.jen.Address.AddressActivity;
import com.example.haozai.jen.Address.UserAddress;
import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.GoodsFragment.GoodsActivity;
import com.example.haozai.jen.ManageForMe.Safety;
import com.example.haozai.jen.MyRunnable.Runn.AddressRunn.DefaultAddress;
import com.example.haozai.jen.MyRunnable.Runn.BuyRunn.SendinfoRunn;
import com.example.haozai.jen.PassWordView.OnPasswordInputFinish;
import com.example.haozai.jen.PassWordView.PasswordView;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.IpAddress;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Tool.ToastRunn;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.wheel.ChooseWheelView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SendBuyinfo extends AppCompatActivity  implements View.OnClickListener ,DefaultAddress.getDefaultAddress,SendinfoRunn.CBuy,ChooseWheelView.setWheel{
    private static final String TAG = SendBuyinfo.class.getSimpleName();
    private TextView free,amount,total,yunfei_end,pay_end;
    private TextView buyname,buyphone,address_tv,detail_info;
    private LinearLayout choose_pay_ways,bill,addresschoose;
    private TextView pay_way,bill_tv,buyinfo_bkname;
    private ImageView buyinfo_img,nowbuy_back;
    private TextView buyinfo_money,cash;
    private Button btn_send;
    private List<UserAddress> addresslist;
    private List<Books> bookslist;
    private ChooseWheelView wheelView;
    String readstring,pswd,pswdexit,num="";
    private  Dialog PWbottomDialog;
    private DefaultAddress defaultaddresss;
    private SendinfoRunn sendinfoRunn;
    private ToastRunn toastRunn;
    private SweetAlertDialog pDialog;
    private boolean flag,money=false,address=false;
    double data=0.01d;
    int number = 0;
    double totalPrice = 0.01d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        setContentView(R.layout.send_buyinfo);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        //创建数据
        initView();
        data();
        interfaceData();
        Pwdialogdata();
    }

    private void initView() {
        buyinfo_bkname = (TextView) findViewById(R.id.buyinfo_bkname);
        buyinfo_money = (TextView) findViewById(R.id.buyinfo_money);
        buyinfo_img = (ImageView) findViewById(R.id.buybookimg);
        free = (TextView) findViewById(R.id.buyinfo_free);
        amount = (TextView) findViewById(R.id.buyinfo_amount);
        total = (TextView) findViewById(R.id.total);
        yunfei_end = (TextView) findViewById(R.id.yunfei_end);
        pay_end = (TextView) findViewById(R.id.pay_end);
        buyname = (TextView) findViewById(R.id.buyname);
        buyphone = (TextView) findViewById(R.id.buyphone);
        address_tv= (TextView) findViewById(R.id.address_info);
        detail_info= (TextView) findViewById(R.id.detail_info);
        btn_send= (Button) findViewById(R.id.btn_send);
        cash= (TextView) findViewById(R.id.cash);
        btn_send.setOnClickListener(this);
        pay_way = (TextView) findViewById(R.id.pay_way);
        bill_tv = (TextView) findViewById(R.id.bill_tv);
        choose_pay_ways = (LinearLayout)  findViewById(R.id.choose_pay_ways);
        addresschoose = (LinearLayout)  findViewById(R.id.address_choose);
        bill = (LinearLayout)  findViewById(R.id.bill);
        nowbuy_back = (ImageView) findViewById(R.id.nowbuy_back);
        choose_pay_ways.setOnClickListener(this);
        bill.setOnClickListener(this);
        addresschoose.setOnClickListener(this);
        nowbuy_back.setOnClickListener(this);
    }
/**
* 判断接受数据
*/
    private void data() {
        Intent intent = getIntent();

        if ("kill".equals(intent.getAction())) {
            bookslist = (ArrayList<Books>) intent.getSerializableExtra("bean");
            num = intent.getStringExtra("count");
            String getprice = intent.getStringExtra("price");
            buyinfo_money.setText(getprice);
            amount.setText("共"+num+"件"+",");
            flag =false;
        }
        if ("buyinfo".equals(intent.getAction())) {
            bookslist = (ArrayList<Books>) intent.getSerializableExtra("bean");
            num = intent.getStringExtra("count");
            buyinfo_money.setText(bookslist.get(0).getPrice());
            amount.setText("共"+num+"件"+",");
            flag = true;
        }
        buyinfo_bkname.setText(bookslist.get(0).getName());
        String imageip = IpAddress.imgIP;
        Glide.with(this).load(imageip+bookslist.get(0).getBooknum()+".jpg").into(buyinfo_img);
        try{
            data=Double.parseDouble(buyinfo_money.getText().toString().trim());
            number=Integer.parseInt(num);
        }catch (Exception e){
            e.printStackTrace();
        }
        //把得到的值保留两位小数四舍五入
        BigDecimal Price = new BigDecimal(data*number);
        totalPrice = Price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(totalPrice>=50||!flag){
            free.setText("免运费");
            yunfei_end.setText("0.00");
            total.setText(totalPrice+"");
            pay_end.setText(totalPrice+"");
        }else{
            free.setText("运费:6元");
            yunfei_end.setText("6.00");
            total.setText(totalPrice+"");
            pay_end.setText(totalPrice+6+"");
        }
    }

    private void interfaceData() {
        defaultaddresss = new DefaultAddress();
        defaultaddresss.inter(this);
        sendinfoRunn = new SendinfoRunn();
        sendinfoRunn.inter(this);
        wheelView = new ChooseWheelView();
        wheelView.setInter(this);
        readstring = LocalSpUtil.getUserId(this);
        pswdexit = LocalSpUtil.getPswdExit(this);
        defaultaddresss.settingData(readstring);
        toastRunn = new ToastRunn();
    }

/**
* 得到默认地址返回结果
*/
    @Override
    public void result(String res, String string) {
        if(res!=null&&string!=null) {
            cash.setText(res);
            double cdata=0.01d;
            try{
                cdata=Double.parseDouble(res);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (cdata>=totalPrice){
                money = true;
            }
            if(!string.equals("fail")) {
                address =true;
                addresslist = all_json.jsonaddressList(string);
                String str = addresslist.get(0).getPnumber();
                String maskNumber = str.substring(0, 3) + "****" + str.substring(7, str.length());
                buyname.setText(addresslist.get(0).getMing());
                buyphone.setText(maskNumber);
                address_tv.setText(addresslist.get(0).getAddress());
                detail_info.setText(addresslist.get(0).getDetail());
            }
        }else {
            ToastUtil.showS(this,"连接服务器失败！");
        }
    }

    /**
    * 弹出输入支付密码框
    */
    private void Pwdialogdata() {
        PWbottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.pw_bottom_dialog, null);
        PWbottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        PWbottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        PWbottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        final PasswordView pwdView = (PasswordView) contentView.findViewById(R.id.pwd_view);
        // 添加回调接口
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                // 输入完成后,输入的密码验证正确
                checkNet();
                pswd = pwdView.getStrPassword();
                sendinfoRunn.PswdCheck(readstring,pswd);
            }
            //取消支付
            @Override
            public void outfo() {
                checkNet();
                sendinfoRunn.ordering(readstring,bookslist,num,pay_end.getText().toString().trim(),
                        yunfei_end.getText().toString().trim(),"daiing");
                PWbottomDialog.dismiss();
                new SweetAlertDialog(SendBuyinfo.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("取消支付!")
                        .setContentText("点击前往支付!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent in = new Intent();
                                in.setClass(SendBuyinfo.this, GoodsActivity.class);
                                in.putExtra("id", 1);
                                startActivity(in);
                            }
                        })
                        .show();
            }
            //忘记密码回调事件
            @Override
            public void forgetPwd() {
                setPswd();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_pay_ways:
                wheelView.setData(true);  //true是payview
                wheelView.showPayView(this);
                break;
            case R.id.bill:
                wheelView.setData(false);  //false是billview
                wheelView.showPayView(this);
                break;
            case R.id.btn_send:
                if (checkNet()&&checktext()&&checkinfo()&&checkpswd())
                    PWbottomDialog.show();
                break;
            case R.id.nowbuy_back:
                startActivity(new Intent(this, Buyinfo.class));
                break;
            case R.id.address_choose:
                startActivity(new Intent(this, AddressActivity.class));
                break;
        }
    }

    private boolean checkpswd() {
        String pwd = LocalSpUtil.getPswdExit(this);
        if(pwd.equals("exist")){
            return true;
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("请设置支付密码!")
                    .setContentText("点击前往设置!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(SendBuyinfo.this,Safety.class));
                        }
                    })
                    .show();
            return false;
        }
    }

    /**
* 支付回调接口
*/
    @Override
    public void resultnum(int number) {
            switch (number) {
                case 0:
                    ToastUtil.showS(this, "密码正确");
                    pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("正在购买....");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    sendinfoRunn.goBuy(readstring, pswd, pay_end.getText().toString().trim());
                    PWbottomDialog.dismiss();
                    break;
                case 3:
                    ToastUtil.showS(this, "支付成功！");
                    sendinfoRunn.ordering(readstring, bookslist, num, pay_end.getText().toString().trim(),
                            yunfei_end.getText().toString().trim(), "accepting");
                    pDialog.dismiss();
                    PWbottomDialog.dismiss();
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("购买成功!")
                            .setContentText("点击前往查看订单!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent in = new Intent();
                                    in.setClass(SendBuyinfo.this, GoodsActivity.class);
                                    in.putExtra("id", 3);
                                    startActivity(in);
                                }
                            })
                            .show();
                    break;
            }
            /*if (!num.equals(0)||!num.equals(3)){
                pDialog.dismiss();
            }*/
        toastRunn.showBuyToast(this,number);
    }
    /**
    * WheelView回调接口
    */
    @Override
    public void wheelresult(String res, boolean flag) {
        if (flag) {
            pay_way.setText(res);
        } else{
            bill_tv.setText(res);
        }
    }

    public boolean checktext(){
        String check_pay_may=pay_way.getText().toString().trim();
        String check_bill_way=bill_tv.getText().toString().trim();
        if(!toastRunn.goset()){
            setPswd();
        }
       return toastRunn.showPayToast(this,check_pay_may,check_bill_way,pswdexit);
    }

    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showL(this,"当前网络不可用");
            return false;
        }else {
            return true;
        }
    }
    private  boolean checkinfo() {
        if (!money){
            ToastUtil.showL(this,"余额不足");
            return false;
        }else if(!address){
            ToastUtil.showL(this,"请填写地址");
            return false;
        }
        return true;
    }

    public void setPswd() {
        startActivity(new Intent(this,Safety.class));
    }
}

