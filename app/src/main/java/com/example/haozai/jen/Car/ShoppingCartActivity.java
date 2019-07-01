package com.example.haozai.jen.Car;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.MyRunnable.Runn.BuyRunn.ChangeCar;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.Utils.WebServicePost;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jen on 2018/6/21.
 * 购物车界面
 *
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener
        , ShoppingCarAdapter.CheckInterface, ShoppingCarAdapter.ModifyCountInterface ,ChangeCar.Del{

    private static final String TAG = "ShoppingCartActivity";
   //全选
    CheckBox ckAll;
    //总额
    TextView tvShowPrice;
   //结算
    TextView tvSettlement;
   //编辑
    TextView btnEdit,carback;

    // 返回主线程更新数据
    String result,id;
    private static Handler handler = new Handler();
    ListView list_shopping_cart;
    private ShoppingCarAdapter shoppingCarAdapter;
    private ChangeCar changeCar;
    private boolean flag = false;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private boolean mSelect;
    private double price = 0.00;
    private double totalPrice;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    protected static final int ERROR = -1;
    protected static final int SUCCESS = 1;
    protected static final int FAIL = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.layout_shopcar);
        initView();
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        id = LocalSpUtil.getUserId(this);
        // 创建子线程
        new Thread(new MyThread()).start();
    }
    private void initView() {
        carback= (TextView) findViewById(R.id.carback);
        ckAll= (CheckBox) findViewById(R.id.ck_all);
        tvShowPrice= (TextView) findViewById(R.id.tv_show_price);
        tvSettlement= (TextView) findViewById(R.id.tv_settlement);
        btnEdit= (TextView) findViewById(R.id.bt_header_right);
        list_shopping_cart= (ListView) findViewById(R.id.list_shopping_cart);
        btnEdit.setOnClickListener(this);
        ckAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        carback.setOnClickListener(this);
        initData();
    }
    //初始化数据
   protected void initData() {
        shoppingCarAdapter = new ShoppingCarAdapter(this);
        shoppingCarAdapter.setCheckInterface(this);
        shoppingCarAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCarAdapter);
        shoppingCarAdapter.setShoppingCartBeanList(shoppingCartBeanList);
       changeCar = new ChangeCar();
       changeCar.inter(this);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            Message message = new Message();
            try {
                result = WebServicePost.ShopCar(id);
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                message.setData(bundle);
                findHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                message.what=ERROR;
               findHandler.sendMessage(message);

            }
        }
    }
     Handler findHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERROR:
                    Toast.makeText(ShoppingCartActivity.this, "服务器开小差", Toast.LENGTH_LONG).show();
                    break;
            }
            Bundle bundle;
            bundle = msg.getData();
            String result = bundle.getString("result");
            shoppingCartBeanList = all_json.jsonCarList(result);
            Log.i(TAG,shoppingCartBeanList+"");
            list_shopping_cart.setAdapter(shoppingCarAdapter);
            shoppingCarAdapter.setShoppingCartBeanList(shoppingCartBeanList);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {
                    if (ckAll.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                        }
                        shoppingCarAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCarAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.bt_header_right:
                flag = !flag;
                if (flag) {
                    btnEdit.setText("完成");
                    shoppingCarAdapter.isShow(false);
                } else {
                    btnEdit.setText("编辑");
                    shoppingCarAdapter.isShow(true);
                }
                break;
            case R.id.tv_settlement: //结算
                lementOnder();
                break;
            case R.id.carback:
               // finish();
                Intent i = new Intent();
                i.setClass(this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
                break;
        }
    }
    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartBeanList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);
        shoppingCarAdapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (ShoppingCartBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        price= 0.00;
        double vv=0 ;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                //totalCount++;
                price += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
                totalCount+=shoppingCartBean.getCount();
            }
        }
        //把得到的值保留两位小数四舍五入
        BigDecimal Price = new BigDecimal(price);
        totalPrice = Price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvShowPrice.setText("合计:" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
    }
    /**
     * 结算订单、支付
     */
    private void lementOnder() {
        ArrayList<ShoppingCartBean> list = new ArrayList<ShoppingCartBean>();
        //选中的需要提交的商品清单

        for (ShoppingCartBean bean:shoppingCartBeanList ){
            boolean choosed = bean.isChoosed();
            if (choosed){
                list.add(bean);
            }
        }
        //跳转到支付界面
        Intent intent = new Intent(ShoppingCartActivity.this, CarToBuy.class);
        intent.setAction("action");
        intent.putExtra("bean",list);
        intent.putExtra("totalPrice",totalPrice);
        intent.putExtra("count",totalCount);
        startActivity(intent);
    }
    /**
     * 增加
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked,String url) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
         if(currentCount>=0&&currentCount<=50) {
            currentCount++;
             changeCar.changgeData(id,url,"in");
        }
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCarAdapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked,String url) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        if (currentCount == 1) {
            ToastUtil.showS(this,"不能少于一件商品");
            return;
        }else if(currentCount>=0) {
            changeCar.changgeData(id,url,"de");
            currentCount--;
        }
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCarAdapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position,String url) {
        shoppingCartBeanList.remove(position);
        shoppingCarAdapter.notifyDataSetChanged();
        changeCar.settingData(id,url);
        statistics();
    }

    @Override
    public void result(int num) {
        if (num==-1)
            ToastUtil.showS(this,"连接服务器失败！");
    }
}
