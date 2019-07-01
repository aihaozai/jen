package com.example.haozai.jen.GoodsFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.MyRunnable.Runn.OrderRunn.OrderList;
import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.CheckPswdUtil;
import com.example.haozai.jen.PassWordView.PassWordUtil;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.Utils.WebServicePost;

import java.util.List;

public class ALLFragment extends BaseFragment implements AllAdapter.CancelOrderInterface ,AllAdapter.Pay,OrderList.Check,CheckPswdUtil.CheckPswd,PassWordUtil.Pw{
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private List<OrderInfo> goodslist;
    private ListView allview;
    private AllAdapter allAdapter;
    private PassWordUtil passWordUtil;
    private OrderList orderListinter;
    private CheckPswdUtil checkPswdUtil;
    String readstring,id,pswd,orderresult,newsid,pays;
    int num;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all, container, false);
        initView(view);
        if (!isPrepared) {
            viewSwiper = false;
            initData();
            orderListinter.getalllist(id);
        } else{
            viewSwiper = true;
        }
        return view;
    }
    @Override
    protected void lazyLoad() {
        isPrepared = true;
        if (!isPrepared || !isVisible) {
        }else {
            isPrepared = false;
            if (viewSwiper){
                initData();
                orderListinter.getalllist(id);
                viewSwiper = false;
            }
        }
    }

    private void initView(View view) {
        allview = (ListView) view.findViewById(R.id.all_view);
    }

    /**
     * 接口
     */
    private void initData() {
        id = LocalSpUtil.getUserId(getActivity());
        allAdapter = new AllAdapter(getActivity());
        allAdapter.setCancelOrderInterface(this);
        allAdapter.setPay(this);
        orderListinter = new OrderList();
        orderListinter.setCheckInterface(this);
        checkPswdUtil = new CheckPswdUtil();
        checkPswdUtil.setCheckPInterface(this);
        passWordUtil = new PassWordUtil();
        passWordUtil.inter(this);

    }
    /**
     * 检查登陆
     */
    private void checkLogin() {
        readstring = LocalSpUtil.getLogining(getActivity());
        if (!readstring.equals("success")) {
            Toast.makeText(getActivity(), "请登录...", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 取消订单移除商品
     */
    @Override
    public void orderDelete(int position) {
        goodslist.remove(position);
        allAdapter.notifyDataSetChanged();
    }

    /**
     * 点击立即付款弹出支付框
     */
    @Override
    public void gopay(String sid,String paymoney,int position) {
        newsid = sid;
        pays = paymoney;
        num = position;
        passWordUtil.showView(getActivity());
    }
    /**
    * 支付成功移除商品
    */
    public void ordersuccess(int number) {
        goodslist.remove(number);
        allAdapter.notifyDataSetChanged();
    }
    /**
     * 输入密码回调结果
     */
    @Override
    public void pwresult(String res) {
        pswd = res;
        checkPswdUtil.check(id,res);
    }

    /**
     * 得到订单信息
     */
    @Override
    public void resultList(List<OrderInfo> list) {
        goodslist = list;
        allview.setAdapter(allAdapter);
        allAdapter.setOrderlists(list);
    }

    /**
     * m没有订单信息回调结果
     */
    @Override
    public void ortherResult(String orther) {
        if (orther=="ERROE"){
            ToastUtil.showS(getActivity(), "服务器连接失败!");
        }else if(orther=="six"){
            ToastUtil.showS(getActivity(),"没有订单喔!");
        }
    }

    /**
     * 检查密码正确性回调结果
     */
    @Override
    public void p_result(String result) {
        if (result=="success"){
            ToastUtil.showS(getActivity(), "密码正确!");
            passWordUtil.dis();
            new Thread(new buy()).start();
        }else {
            ToastUtil.showS(getActivity(),"密码错误，清再次输入！");
        }
    }

    /**
     * 立即付款线程
     */
    public class buy implements Runnable {
        @Override
        public void run() {
            try {
                Message msg = Message.obtain();
                String jieguo = WebServicePost.BuyPay(id,pswd,pays);
                orderresult = WebServicePost.ChangeOrder(newsid,"accepting");
                if (jieguo == null||orderresult == null) {
                    Toast.makeText(getActivity(), orderresult, Toast.LENGTH_SHORT).show();
                }if (orderresult.equals("success")&&jieguo.equals("success")) {
                    msg.what = 2;
                }
                if (orderresult.equals("fail")&&jieguo.equals("fail")) {
                    msg.what = 3;
                }
                checkhandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = -1;
                checkhandler.sendMessage(msg);
            }
        }
    }

    Handler checkhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    Toast.makeText(getActivity(), "服务器出错", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_LONG).show();
                    ordersuccess(num);
                    orderListinter.getalllist(id);
                    break;
                case 3:
                    Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        int id = getActivity().getIntent().getIntExtra("id", 0);
        if(id==1){
            //vp.setCurrentItem(2);
        }
        super.onResume();
    }
}