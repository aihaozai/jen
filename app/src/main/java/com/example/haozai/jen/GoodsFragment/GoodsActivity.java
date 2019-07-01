package com.example.haozai.jen.GoodsFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;

import java.util.ArrayList;


public class GoodsActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView all_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去除工具栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_goods);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        initViews();
        initData();

    }

    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        all_back = (ImageView) findViewById(R.id.all_back);
        all_back.setOnClickListener(this);
    }

    private void initData() {
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待发货");
        titleList.add("待收货");

        //填充数据
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ALLFragment());
        fragmentList.add(new AFragment());
        fragmentList.add(new BFragment());
        fragmentList.add(new CFragment());

        Adapter adapter = new Adapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.all_back:
                        Intent i = new Intent();
                        i.setClass(this, MainActivity.class);
                        //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                        i.putExtra("id", 3);
                        startActivity(i);
                        break;
                }
    }
    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            Fragment A = new AFragment();
            FragmentManager Amanger= getSupportFragmentManager();
            FragmentTransaction transaction = Amanger.beginTransaction();
            transaction.replace(R.id.viewpager, A);
            transaction.commit();
            mViewPager.setCurrentItem(1);//
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(GoodsActivity.this,AFragment.class);
            i.putExtra("id",1);
        }
        if (id == 2) {
            Fragment B = new BFragment();
            FragmentManager Bmanger= getSupportFragmentManager();
            FragmentTransaction transaction = Bmanger.beginTransaction();
            transaction.replace(R.id.viewpager, B);
            transaction.commit();
            mViewPager.setCurrentItem(2);//
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(GoodsActivity.this,BFragment.class);
            i.putExtra("id",2);
        }
        if (id == 3) {
            Fragment C = new BFragment();
            FragmentManager Cmanger= getSupportFragmentManager();
            FragmentTransaction transaction = Cmanger.beginTransaction();
            transaction.replace(R.id.viewpager, C);
            transaction.commit();
            mViewPager.setCurrentItem(3);//
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(GoodsActivity.this,BFragment.class);
            i.putExtra("id",3);
        }
        super.onResume();
    }
}