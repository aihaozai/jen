package com.example.haozai.jen.GoodsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.MyRunnable.Runn.OrderRunn.OrderList;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.ToastUtil;

import java.util.List;

public class CFragment extends BaseFragment implements OrderList.Check {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared,viewSwiper;
    private List<OrderInfo> goodslist;
    private ListView accept_view;
    private CAdapter Cadpter;
    String readstring,id;
    private OrderList orderlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_c, container, false);
        initView(view);
        if(!isPrepared){
            viewSwiper = false;
            interfaceData();
            orderlist.getgoodlist(id,"accepting");
        }else {
            viewSwiper = true;
        }
        return view;
    }

    private void interfaceData() {
        orderlist = new OrderList();
        orderlist.setCheckInterface(this);
    }

    @Override
    protected void lazyLoad() {
        isPrepared = true;
        if (!isPrepared || !isVisible) {
        }else {
            isPrepared = false;
            if (viewSwiper){
                interfaceData();
                orderlist.getgoodlist(id,"accepting");
                viewSwiper = false;
            }
        }
    }
    private void initView(View view) {
        Cadpter = new CAdapter(getActivity());
        accept_view = (ListView) view.findViewById(R.id.accept_view);
        readstring = LocalSpUtil.getLogining(getActivity());
        id = LocalSpUtil.getUserId(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (readstring.equals("success")) {
        } else {
            Toast.makeText(getActivity(), "请登录...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void resultList(List<OrderInfo> list) {
        goodslist = list;
        accept_view.setAdapter(Cadpter);
        Cadpter.setOrderlists(goodslist);
    }

    @Override
    public void ortherResult(String orther) {
        if (orther=="ERROE"){
            ToastUtil.showS(getActivity(), "服务器连接失败!");
        }else if(orther=="six"){
            ToastUtil.showS(getActivity(),"没有待付款订单喔!");
        }
    }
    @Override
    public void onResume() {
        int id = getActivity().getIntent().getIntExtra("id", 0);
        if(id==3){
            //vp.setCurrentItem(2);
        }
        super.onResume();
    }
}