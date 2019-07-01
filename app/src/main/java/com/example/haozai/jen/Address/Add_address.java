package com.example.haozai.jen.Address;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.address.bean.AdressBean;
import com.example.address.bean.City;
import com.example.address.bean.County;
import com.example.address.bean.Province;
import com.example.address.bean.Street;
import com.example.address.db.manager.AddressDictManager;
import com.example.address.utils.LogUtil;
import com.example.address.widget.AddressSelector;
import com.example.address.widget.BottomDialog;
import com.example.address.widget.OnAddressSelectedListener;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Utils.WebServicePost;


public class Add_address extends AppCompatActivity implements View.OnClickListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener{
    private EditText et_add_address_man,et_add_address_phone,et_add_addresss_area,et_add_address_detail_area;
    private Button btn_add;
    private ImageView add_back_address,default_add;
    private BottomDialog dialog;
    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String streetCode;
    private int provincePosition;
    private int cityPosition;
    private int countyPosition;
    private int streetPosition;
    private boolean flag = false;
    private ImageView default_img;
    private AddressDictManager addressDictManager;
    String  add_man,add_phone,add_area,add_detail,result;
    // 创建等待框
    private ProgressDialog adddialog;
    String keynum;
    // 返回主线程更新数据
    private SharedPreferences sp;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_addresss);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        data();
    }

    private void data() {
        et_add_address_man= (EditText) findViewById(R.id.et_add_address_accept);
        et_add_address_phone= (EditText) findViewById(R.id.et_add_address_phone);
        et_add_addresss_area= (EditText) findViewById(R.id.et_add_address_area);
        et_add_address_detail_area= (EditText) findViewById(R.id.et_add_address_detail_area);
        btn_add = (Button)  findViewById(R.id.btn_add_address);
        default_add= (ImageView) findViewById(R.id.default_add);
        add_back_address = (ImageView) findViewById(R.id.add_back_address);
        et_add_addresss_area.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        default_add.setOnClickListener(this);
        add_back_address.setOnClickListener(this);

        AddressSelector selector = new AddressSelector(this);
        //获取地址管理数据库
        addressDictManager = selector.getAddressDictManager();

        selector.setTextSize(14);//设置字体的大小
//        selector.setIndicatorBackgroundColor("#00ff00");
        selector.setIndicatorBackgroundColor(android.R.color.holo_orange_light);//设置指示器的颜色
//        selector.setBackgroundColor(android.R.color.holo_red_light);//设置字体的背景

        selector.setTextSelectedColor(android.R.color.holo_orange_light);//设置字体获得焦点的颜色

        selector.setTextUnSelectedColor(android.R.color.holo_blue_light);//设置字体没有获得焦点的颜色

//        //获取数据库管理
        AddressDictManager addressDictManager = selector.getAddressDictManager();
        AdressBean.ChangeRecordsBean changeRecordsBean = new AdressBean.ChangeRecordsBean();
        changeRecordsBean.parentId = 0;
        changeRecordsBean.name = "测试省";
        changeRecordsBean.id = 35;
        addressDictManager.inserddress(changeRecordsBean);//对数据库里增加一个数据
        selector.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(Province province, City city, County county, Street street) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_add_address_area:
                if (dialog != null) {
                    dialog.show();
                } else {
                    dialog = new BottomDialog(this);
                    dialog.setOnAddressSelectedListener(this);
                    dialog.setDialogDismisListener(this);
                    dialog.setTextSize(14);//设置字体的大小
                    dialog.setIndicatorBackgroundColor(android.R.color.holo_orange_light);//设置指示器的颜色
                    dialog.setTextSelectedColor(android.R.color.holo_orange_light);//设置字体获得焦点的颜色
                    dialog.setTextUnSelectedColor(android.R.color.holo_blue_light);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
                    dialog.setSelectorAreaPositionListener(this);
                    dialog.show();
                }
                break;
            case R.id.default_add:
                flag =!flag;
                if ( flag) {
                    default_add.setImageResource(R.mipmap.select_moren);
                keynum="1";
                } else {
                    default_add.setImageResource(R.mipmap.select_yuan);
                keynum="0";
                }
                break;

            case R.id.btn_add_address:
                add_man = et_add_address_man.getText().toString().trim();
                add_phone = et_add_address_phone.getText().toString().trim();
                add_area = et_add_addresss_area.getText().toString().trim();
                add_detail = et_add_address_detail_area.getText().toString().trim();
                String num = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}";

                if (TextUtils.isEmpty(add_man)) {
                    Toast.makeText(this, "请输入收货人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(add_area)) {
                    Toast.makeText(this, "请选择所在地区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(add_detail)) {
                    Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(add_phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    add_phone = et_add_address_phone.getText().toString().trim();
                    if (et_add_address_phone.getText().toString().trim().length() != 11) {
                        Toast.makeText(this, "您的电话号码位数不正确", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!add_phone.matches(num)) {
                        Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                // 提示框
                adddialog = new ProgressDialog(this);
                adddialog.setMessage("添加中，请稍后...");
                adddialog.setCancelable(false);
                adddialog.show();
                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
                break;
            case R.id.add_back_address:
                finish();
                break;
        }
    }
        // 子线程接收数据，主线程修改数据
        public class MyThread implements Runnable {

            @Override
            public void run() {
                //从本地获取登录id
                        sp = getSharedPreferences("Datadefault", Context.MODE_PRIVATE);
                     String readstring = sp.getString("id", "");
                try {
                    result = WebServicePost.AddAddress(add_man,add_phone,add_area,add_detail,readstring,keynum);

                    if(result==null) {
                        Toast.makeText(Add_address.this, result, Toast.LENGTH_LONG).show();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //去掉提示框
                            adddialog.dismiss();
                            Toast.makeText(Add_address.this, result, Toast.LENGTH_LONG).show();
                            //登录成功
                            if (result.equals("success")) {
                                Toast.makeText(Add_address.this, "添加成功！", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Add_address.this,AddressActivity.class);
                                startActivity(intent);
                            }
                            //登录失败
                            else if (result.equals("fail")) {
                                Toast.makeText(Add_address.this, "添加失败！", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = 1;
                    loginHandler.sendMessage(msg);

                }
            }

            Handler loginHandler = new Handler() {
                public void handleMessage(Message msg) {

                    switch (msg.what) {
                        case 1:
                            adddialog.dismiss();
                            Toast.makeText(Add_address.this, "连接服务器失败", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };
        }

        @Override
        public void onAddressSelected(Province province, City city, County county, Street street) {
            provinceCode = (province == null ? "" : province.code);
            cityCode = (city == null ? "" : city.code);
            countyCode = (county == null ? "" : county.code);
            streetCode = (street == null ? "" : street.code);
            LogUtil.d("数据", "省份id=" + provinceCode);
            LogUtil.d("数据", "城市id=" + cityCode);
            LogUtil.d("数据", "乡镇id=" + countyCode);
            LogUtil.d("数据", "街道id=" + streetCode);
            String s = (province == null ? "" : province.name) +"|"+ (city == null ? "" : city.name) +"|"+ (county == null ? "" : county.name) +" "+
                    (street == null ? "" : street.name);
            et_add_addresss_area.setText(s);
            if (dialog != null) {
                dialog.dismiss();
            }
//        getSelectedArea();
        }

        @Override
        public void dialogclose() {
            if(dialog!=null){
                dialog.dismiss();
            }
        }

        /**
         * 根据code 来显示选择过的地区
         */
    private void getSelectedArea(){
        String province = addressDictManager.getProvince(provinceCode);
        String city = addressDictManager.getCity(cityCode);
        String county = addressDictManager.getCounty(countyCode);
        String street = addressDictManager.getStreet(streetCode);
        et_add_addresss_area.setText(province+city+county+street);

    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        this.provincePosition = provincePosition;
        this.cityPosition = cityPosition;
        this.countyPosition = countyPosition;
        this.streetPosition = streetPosition;
        LogUtil.d("数据", "省份位置=" + provincePosition);
        LogUtil.d("数据", "城市位置=" + cityPosition);
        LogUtil.d("数据", "乡镇位置=" + countyPosition);
        LogUtil.d("数据", "街道位置=" + streetPosition);
    }
}
