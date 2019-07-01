package com.example.haozai.jen.buy;

/**
 * Created by haozai on 2018-06-13.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.MyRunnable.Runn.BooksRunn.BooksinfoRunn;
import com.example.haozai.jen.MyRunnable.Runn.BuyRunn.AddCar;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BadgeView;
import com.example.haozai.jen.Tool.IpAddress;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Tool.ToastRunn;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.NetUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.conn.Login;
import java.io.Serializable;
import java.util.List;

public class Buyinfo extends AppCompatActivity implements View.OnClickListener,BooksinfoRunn.Info,BottomBuyView.setDialog,AddCar.Add {
    private static final String TAG="BuyInfo";
    private TextView bkname, press,qian,authors;
    private ImageView bookinfo;
    private TextView addcar;
    private TextView buynow;
    private ImageView goincar;//购物车图标
    private ImageView buyImg;// 界面上跑的小图片
    private BadgeView buyNumView;//购物车上的数量标签
    private AniManager mAniManager;
    private int num;
    private List<Books> list;
    private BooksinfoRunn booksinfoRunnInter;
    private BottomBuyView bottombuy;
    private AddCar addCar;
    private ToastRunn toastRunn;
    String readstring,id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        setContentView(R.layout.buyinfo);
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
        initViews();
        interfaceData();
        mAniManager = new AniManager();//动画实例
}

    private void initViews() {
        bkname = (TextView) findViewById(R.id.bkname);
        press = (TextView) findViewById(R.id.press);
        qian = (TextView) findViewById(R.id.qian);
        authors = (TextView) findViewById(R.id.authors);
        addcar = (TextView) findViewById(R.id.gocar);
        bookinfo = (ImageView) findViewById(R.id.bookinfo);
        goincar = (ImageView) findViewById(R.id.goincar);
        buynow = (TextView) findViewById(R.id.buynow);
        addcar.setOnClickListener(this);
        buynow.setOnClickListener(this);
        buyNumView = new BadgeView(this, goincar);//把这个数字标签放在购物车图标上
        buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);//放在图标中心
        buyNumView.setTextColor(Color.WHITE);//数字颜色
        buyNumView.setBadgeBackgroundColor(Color.RED);//背景颜色
        buyNumView.setTextSize(9);//数字大小
    }

    private void interfaceData() {
        booksinfoRunnInter = new BooksinfoRunn();
        booksinfoRunnInter.inter(this);
        bottombuy = new BottomBuyView();
        bottombuy.setInter(this);
        addCar = new AddCar();
        addCar.inter(this);
        toastRunn = new ToastRunn();
        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        booksinfoRunnInter.settingData(str);
        id = LocalSpUtil.getUserId(Buyinfo.this);
        readstring = LocalSpUtil.getLogining(Buyinfo.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gocar:
                if(readstring.equals("success")&&checkNet()) {
                    startAnim(v);
                }else {
                    Toast.makeText(Buyinfo.this,"请登录！",Toast.LENGTH_LONG);
                    Intent intent = new Intent(this,Login.class);
                    startActivity(intent);
                }
                break;
            case R.id.buynow:
                if(readstring.equals("success")&&checkNet()) {
                    bottombuy.showBooksView(this,list.get(0).getPrice(),list.get(0).getBooknum());
                }else {
                    ToastUtil.showS(Buyinfo.this,"请登录！");
                    Intent intent = new Intent(this,Login.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void getInfo(List<Books> lists) {
        list = lists;
        bkname.setText(list.get(0).getName());
        press.setText(list.get(0).getPress());
        authors.setText(list.get(0).getAuthor());
        qian.setText(list.get(0).getPrice());
        String imageip = IpAddress.imgIP;
        Glide.with(this).load(imageip+lists.get(0).getBooknum()+".jpg").into(bookinfo);

    }
    @Override
    public void result(String res) {
        checkNet();
        Intent intent = new Intent(this,SendBuyinfo.class);
        intent.setAction("buyinfo");
        intent.putExtra("bean",(Serializable)list);
        intent.putExtra("count",res);
        startActivity(intent);
    }
    @Override
    public void failinfo(String str) {
        if (str=="ERROR")
            ToastUtil.showS(this,"服务器开小差");
    }

    //启动动画
    public void startAnim(View v) {
        int[] end_location = new int[2];
        int[] start_location = new int[2];
        v.getLocationInWindow(start_location);// 获取购买按钮的在屏幕的X、Y坐标（动画开始的坐标）
        goincar.getLocationInWindow(end_location);// 这是用来存储动画结束位置，也就是购物车图标的X、Y坐标
        buyImg = new ImageView(this);// buyImg是动画的图片
        buyImg.setImageResource(R.mipmap.addbookicon);// 设置buyImg的图片
        //        mAniManager.setTime(5500);//自定义时间
        mAniManager.setAnim(this, buyImg, start_location, end_location);// 开始执行动画
        mAniManager.setOnAnimListener(new AniManager.AnimListener() {
            @Override
            public void setAnimBegin(AniManager a) {
                //动画开始时的监听
            }
            @Override
            public void setAnimEnd(AniManager a) {
                //动画结束后的监听
                num += 1;
                buyNumView.setText(num + "");
                buyNumView.show();
                addCar.settingData(id,list);
            }
        });
    }

    @Override
    public void resultnum(int num) {
        toastRunn.addcarToast(this,num);
    }
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            ToastUtil.showL(this,"当前网络不可用");
            return false;
        }else {
            return true;
        }
    }
}
