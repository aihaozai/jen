package com.example.haozai.jen.GridView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.haozai.jen.buy.AniManager;
import com.example.haozai.jen.buy.BottomBuyView;
import com.example.haozai.jen.buy.SendBuyinfo;
import com.example.haozai.jen.conn.Login;

import java.io.Serializable;
import java.util.List;


public class GVBooks extends AppCompatActivity implements View.OnClickListener, BooksinfoRunn.Info,AddCar.Add,BottomBuyView.setDialog{
    private TextView gvbkname,gvauthors,gvpress,gvmoney;
    private TextView gvgocar,gvbuynow,gvclassify;
    private ImageView gvincar;//购物车图标
    private ImageView gvbuyImg;// 界面上跑的小图片
    private BadgeView buyNumView;//购物车上的数量标签
    private AniManager mAniManager;
    private ImageView image;
    private BooksinfoRunn runn;
    private BottomBuyView bottomBuyView;
    private AddCar addcar;
    private List<Books> list ;
    private ToastRunn toastrunn;
    boolean flag = false;
    String mon;
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gvbooks);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        initView();
        BadgeViewData();
        interfaceData();
        mAniManager = new AniManager();
    }

    private void initView() {
        gvbkname = (TextView) findViewById(R.id.gvbkname);
        gvauthors = (TextView) findViewById(R.id.gvauthors);
        gvpress = (TextView) findViewById(R.id.gvpress);
        gvmoney = (TextView) findViewById(R.id.gvmoney);
        image = (ImageView) findViewById(R.id.gvbookinfo);
        gvgocar = (TextView) findViewById(R.id.gvgocar);
        gvbuynow  = (TextView) findViewById(R.id.gvbuynow);
        gvclassify = (TextView) findViewById(R.id.gvclassify);
        gvgocar.setOnClickListener(this);
        gvbuynow.setOnClickListener(this);
    }
    private void BadgeViewData() {
        gvincar = (ImageView) findViewById(R.id.gvincar);
        buyNumView = new BadgeView(this, gvincar);//把这个数字标签放在购物车图标上
        buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);//放在图标中心
        buyNumView.setTextColor(Color.WHITE);//数字颜色
        buyNumView.setBadgeBackgroundColor(Color.RED);//背景颜色
        buyNumView.setTextSize(9);//数字大小
    }
    private void interfaceData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mon = intent.getStringExtra("money");
        String title = intent.getStringExtra("title");
        if (title.equals("每周推荐"))
            flag = !flag;
        runn = new BooksinfoRunn();
        runn.inter(this);
        runn.settingData(url);
        gvmoney.setText(mon);
        gvclassify.setText(title);
        addcar = new AddCar();
        addcar.inter(this);
        toastrunn = new ToastRunn();
        bottomBuyView = new BottomBuyView();
        bottomBuyView.setInter(this);
    }

    @Override
    public void getInfo(List<Books> lists) {
        list = lists;
        gvbkname.setText(lists.get(0).getName());
        gvauthors.setText(lists.get(0).getAuthor());
        gvpress.setText(lists.get(0).getPress());
        String imageip = IpAddress.imgIP;
        Glide.with(this).load(imageip+lists.get(0).getBooknum()+".jpg").into(image);
    }

    @Override
    public void failinfo(String str) {
        if (str=="ERROR")
            ToastUtil.showS(this,"服务器开小差");
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.gvgocar:
                    if (!flag) {
                        ToastUtil.showS(this, "秒杀商品不能加入购物车");
                    }else {
                        startAnim(v);
                    }
                    break;
                case R.id.gvbuynow:
                    String readstring = LocalSpUtil.getLogining(this);
                    if(readstring.equals("success")) {
                        checkNet();
                        if(!flag){
                            Intent intent = new Intent(this, SendBuyinfo.class);
                            intent.setAction("kill");
                            intent.putExtra("bean",(Serializable)list);
                            intent.putExtra("price",mon);
                            intent.putExtra("count","1");
                            startActivity(intent);
                        }else {
                        bottomBuyView.showBooksView(this,list.get(0).getPrice(),list.get(0).getBooknum());}
                    }else {
                        ToastUtil.showS(this,"请登录！");
                        startActivity(new Intent(this,Login.class));
                    }
                    break;
            }
    }
    public void startAnim(View v) {
        int[] end_location = new int[2];
        int[] start_location = new int[2];
        v.getLocationInWindow(start_location);// 获取购买按钮的在屏幕的X、Y坐标（动画开始的坐标）
        gvincar.getLocationInWindow(end_location);// 这是用来存储动画结束位置，也就是购物车图标的X、Y坐标
        gvbuyImg = new ImageView(this);// buyImg是动画的图片
        gvbuyImg.setImageResource(R.mipmap.addbookicon);// 设置buyImg的图片
        //        mAniManager.setTime(5500);//自定义时间
        mAniManager.setAnim(this, gvbuyImg, start_location, end_location);// 开始执行动画
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
                String id = LocalSpUtil.getUserId(GVBooks.this);
                addcar.settingData(id,list);
            }
        });
    }

    @Override
    public void resultnum(int num) {
        toastrunn.addcarToast(this,num);
    }

    @Override
    public void result(String res) {
        Intent intent = new Intent(this,SendBuyinfo.class);
        intent.setAction("buyinfo");
        intent.putExtra("bean",(Serializable)list);
        intent.putExtra("count",res);
        startActivity(intent);
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
