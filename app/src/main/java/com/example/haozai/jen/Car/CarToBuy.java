package com.example.haozai.jen.Car;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haozai.jen.Address.AddressActivity;
import com.example.haozai.jen.Address.UserAddress;
import com.example.haozai.jen.GoodsFragment.GoodsActivity;
import com.example.haozai.jen.GridView.GridViewCarBuyAdapter;
import com.example.haozai.jen.ManageForMe.Safety;
import com.example.haozai.jen.MyRunnable.Runn.AddressRunn.DefaultAddress;
import com.example.haozai.jen.MyRunnable.Runn.BuyRunn.BuyCheck;
import com.example.haozai.jen.PassWordView.OnPasswordInputFinish;
import com.example.haozai.jen.PassWordView.PasswordView;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.wheel.ChooseWheelView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CarToBuy extends AppCompatActivity implements View.OnClickListener,DefaultAddress.getDefaultAddress,BuyCheck.CBuy,ChooseWheelView.setWheel{
    private static final String TAG = CarToBuy.class.getSimpleName();
    private GridView carinfo_view;
    private  ArrayList<ShoppingCartBean> list;
    private List<UserAddress> addresslist;
    private GridViewCarBuyAdapter carAdapter;
    private DefaultAddress defaultAddress;
    private BuyCheck buyCheck;
    private TextView carbuyinfo_money,carbuyinfo_amount,carbuyinfo_free,carpay_end;
    private TextView buyname_car,buyphone_car,address_info_car,detail_info_car,cash_car;
    private TextView bill_tv_car,pay_way_car,cartotal,caryunfei;
    private LinearLayout choose_ways,car_bill,addresschoosecar;
    private Dialog PWbottomDialog;
    private ChooseWheelView wheelView;
    private Button carbtn_send;
    double totalPrice;
    String readstring,pswdexit,pswd;
    private SweetAlertDialog pDialog;
    boolean money=false,address=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        setContentView(R.layout.activity_car_to_buy);
        initview();
        intentData();
        viewData();
        interfaceData();
        Pwdialogdata();
    }

    private void initview() {
        carbuyinfo_money = (TextView) findViewById(R.id.carbuyinfo_money) ;
        carbuyinfo_amount = (TextView) findViewById(R.id.carbuyinfo_amount) ;
        carbuyinfo_free = (TextView) findViewById(R.id.carbuyinfo_free);
        carinfo_view = (GridView) findViewById(R.id.grid_carview);
        carpay_end  = (TextView) findViewById(R.id.carpay_end);
        buyname_car = (TextView) findViewById(R.id. buyname_car);
        address_info_car = (TextView) findViewById(R.id. address_info_car);
        buyphone_car = (TextView) findViewById(R.id. buyphone_car);
        detail_info_car = (TextView) findViewById(R.id.detail_info_car);
        cash_car  = (TextView) findViewById(R.id.cash_car);
        choose_ways = (LinearLayout) findViewById(R.id.carchoose_pay_ways);
        car_bill  = (LinearLayout) findViewById(R.id.car_bill);
        addresschoosecar = (LinearLayout) findViewById(R.id.address_choose_car);
        bill_tv_car = (TextView) findViewById(R.id.bill_tv_car);
        pay_way_car = (TextView) findViewById(R.id.pay_way_car);
        cartotal = (TextView) findViewById(R.id.cartotal);
        caryunfei = (TextView) findViewById(R.id.caryunfei);
        carbtn_send  = (Button) findViewById(R.id.carbtn_send);
        choose_ways.setOnClickListener(this);
        car_bill.setOnClickListener(this);
        carbtn_send.setOnClickListener(this);
        addresschoosecar.setOnClickListener(this);
        carAdapter = new GridViewCarBuyAdapter(this);
        wheelView = new ChooseWheelView();
        wheelView.setInter(this);
    }

    private void intentData() {
        Intent intent = getIntent();
        int count=0;
        if ("action".equals(intent.getAction())) {
            list = (ArrayList<ShoppingCartBean>) intent.getSerializableExtra("bean");
            totalPrice = intent.getDoubleExtra("totalPrice",totalPrice);
            count = intent.getIntExtra("count",0);
        }
        int listSize = list.size();
        carbuyinfo_amount.setText("共"+count+"件，");
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density; // 像素密度
        ViewGroup.LayoutParams par_gridViews = carinfo_view.getLayoutParams();
        int itemWidth_jieduan = (int) (70 * density);//每个item宽度
        int spacingWidth = (int) (10 * density);
        par_gridViews.width = itemWidth_jieduan * listSize + (listSize - 1) * spacingWidth;
        carinfo_view.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式
        carinfo_view.setNumColumns(listSize);
        carinfo_view.setHorizontalSpacing(spacingWidth);
        carinfo_view.setColumnWidth(itemWidth_jieduan);
        carinfo_view.setLayoutParams(par_gridViews);
        carAdapter.setCarView(list);
        carinfo_view.setAdapter(carAdapter);
    }
    /**
    * 判断金额是否超过50
    */
    private void viewData() {
        carbuyinfo_money.setText(totalPrice+"");
        if(totalPrice>=50) {
            carbuyinfo_free.setText("免运费");
            carpay_end.setText(totalPrice+"");
            cartotal.setText(totalPrice+"");
            caryunfei.setText(0.00+"");
        }else {
            carbuyinfo_free.setText("运费:6元");
            carpay_end.setText(totalPrice+6+"");
            caryunfei.setText(6.00+"");
            cartotal.setText(totalPrice+"");
        }
    }
    /**
    * 设置接口
    */
    private void interfaceData() {
        readstring = LocalSpUtil.getUserId(this);
        pswdexit = LocalSpUtil.getPswdExit(this);
        defaultAddress = new DefaultAddress();
        defaultAddress.inter(this);
        buyCheck = new BuyCheck();
        buyCheck.inter(this);
        readstring = LocalSpUtil.getUserId(this);
        pswdexit = LocalSpUtil.getPswdExit(this);
        defaultAddress.settingData(readstring);
    }
    /**
     * 弹出输入支付密码框
     */
    private void Pwdialogdata() {
        PWbottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.pw_bottom_dialog, null);
        PWbottomDialog.setContentView(contentView);
        PWbottomDialog.setCanceledOnTouchOutside(false);
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
                pswd = pwdView.getStrPassword();
                buyCheck.PswdCheck(readstring,pswd);
            }
            //取消支付
            @Override
            public void outfo() {
                //关闭支付页面
                buyCheck.cancelPay(readstring,list);
                PWbottomDialog.dismiss();
                new SweetAlertDialog(CarToBuy.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("取消支付!")
                        .setContentText("点击前往支付!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent in = new Intent();
                                in.setClass(CarToBuy.this, GoodsActivity.class);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.carchoose_pay_ways:
                wheelView.setData(true);  //true是payview
                wheelView.showPayView(this);
                break;
            case R.id.car_bill:
                wheelView.setData(false);  //false是billview
                wheelView.showPayView(this);
                break;
            case R.id.carbtn_send:
                if (checkNet()&&checktext()&&checkinfo()&&checkpswd())
                    PWbottomDialog.show();
                break;
            case R.id.address_choose_car:
                startActivity(new Intent(this, AddressActivity.class));
                break;
        }
    }
    /**
     * 得到默认地址返回结果
     */
    @Override
    public void result(String res, String string) {
        if(res!=null&&string!=null) {
            cash_car.setText(res);
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
                address = true;
                addresslist = all_json.jsonaddressList(string);
                String str = addresslist.get(0).getPnumber();
                String maskNumber = str.substring(0, 3) + "****" + str.substring(7, str.length());
                buyname_car.setText(addresslist.get(0).getMing());
                buyphone_car.setText(maskNumber);
                address_info_car.setText(addresslist.get(0).getAddress());
                detail_info_car.setText(addresslist.get(0).getDetail());
            }
        }else {
            ToastUtil.showS(this,"连接服务器失败！");
        }
    }

    /**
    * 输入密码回调结果
    */
    @Override
    public void resultnum(int num) {
        switch (num){
            case -1:
                ToastUtil.showS(this,"连接服务器失败");
                break;
            case 0:
                ToastUtil.showS(this,"密码正确");
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("正在购买....");
                pDialog.setCancelable(false);
                pDialog.show();
                PWbottomDialog.dismiss();
               buyCheck.goBuy(readstring,pswd,carpay_end.getText().toString().trim());
                break;
            case  1:
                ToastUtil.showS(this,"密码错误，清再次输入！");
                break;
            case  2:
                ToastUtil.showS(this,"取消支付,请前往支付！");
                break;
            case  3:
                pDialog.dismiss();
                ToastUtil.showS(this,"支付成功！");
                buyCheck.addOrder(readstring,list);
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("购买成功!")
                        .setContentText("点击前往查看订单!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent in = new Intent();
                                in.setClass(CarToBuy.this, GoodsActivity.class);
                                in.putExtra("id", 3);
                                startActivity(in);
                            }
                        })
                        .show();
                break;
            case  4:
                pDialog.dismiss();
                ToastUtil.showS(this,"支付失败！");
                break;
            case  5:
                ToastUtil.showS(this,"购买成功！");
                wheelView.dis();
                break;
            case  6:
                pDialog.dismiss();
                ToastUtil.showS(this,"购买失败！");
                break;
        }
    }

/**
* wheelView回调结果
*/
    @Override
    public void wheelresult(String res,boolean flag) {
        if (flag) {
            pay_way_car.setText(res);
        } else{
            bill_tv_car.setText(res);
    }
}
    public void setPswd() {
        startActivity(new Intent(this,Safety.class));
    }
    public boolean checktext(){
        String  check_pay_may = pay_way_car.getText().toString().trim();
        String check_bill_way = bill_tv_car.getText().toString().trim();
        if (check_pay_may.equals("请选择")){
            ToastUtil.showS(this, "请选择支付方式");
            return false;
        }
        if (check_bill_way.equals("请选择")) {
            ToastUtil.showS(this, "是否打印发票");
            return false;
        }
        if (check_pay_may!="余额") {
            ToastUtil.showS(this, "目前只支持余额付款");
            return false;
        }
        if(check_pay_may=="余额"&&!check_bill_way.equals("请选择")){
            if (pswdexit!=null||pswdexit!=""&&pswdexit.equals("exist")) {
                return true;
            } else {
                ToastUtil.showS(this, "请设置密码!");
                setPswd();
                return false;
            }
        }else {
            ToastUtil.showS(this, "是否打印发票");
            return false;
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
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showL(this,"当前网络不可用");
            return false;
        }else {
            return true;
        }
    }
    private boolean checkpswd() {
        String pwd = LocalSpUtil.getPswdExit(this);
        Log.i(TAG,pwd+"");
        if(pwd.equals("exist")){
            return true;
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("请设置支付密码!")
                    .setContentText("点击前往设置!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(CarToBuy.this,Safety.class));
                        }
                    })
                    .show();
            return false;
        }
    }
}
