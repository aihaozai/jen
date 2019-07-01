package com.example.haozai.jen.GoodsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;

import java.util.List;

public class BFragment extends BaseFragment {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private List<OrderInfo> goodslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_b, container, false);
        initView(view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }

    }

    private void initView(View view) {

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        int id = getActivity().getIntent().getIntExtra("id", 0);
        if(id==2){
            //vp.setCurrentItem(2);
        }
        super.onResume();
    }
}