package com.example.haozai.jen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.Fragment.FouthFragment;
import com.example.haozai.jen.Fragment.OneFragment;
import com.example.haozai.jen.Fragment.ThreeFragment;
import com.example.haozai.jen.Fragment.TwoFragment;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView  title,item_home, item_book, item_faxian, item_me;
    private ImageView home_v,allbook_v,find_v,penple_v;
    private LinearLayout home_lin,allbook_lin,find_lin,me_lin;
    private ViewPager vp;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FouthFragment fouthFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    String[] titles = new String[]{"首页", "书城", "发现", "我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去除工具栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
//当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        initViews();
        //适配器
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(1);//ViewPager的缓存为1帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_home.setTextColor(Color.parseColor("#f84157"));
        home_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.home_us));
        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                changeTextColor(position);
                title.setText(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        title = (TextView) findViewById(R.id.title) ;
        item_home = (TextView) findViewById(R.id.item_home);
        item_book = (TextView) findViewById(R.id.item_book);
        item_faxian = (TextView) findViewById(R.id.item_faxian);
        item_me = (TextView) findViewById(R.id.item_me);
        home_v = (ImageView) findViewById(R.id.home_view) ;
        allbook_v = (ImageView) findViewById(R.id.allbook_view) ;
        find_v = (ImageView) findViewById(R.id.find_view) ;
        penple_v = (ImageView) findViewById(R.id.pen_view);
        home_lin = (LinearLayout )findViewById(R.id.home_lin);
        allbook_lin = (LinearLayout )findViewById(R.id.allbook_lin);
        find_lin = (LinearLayout )findViewById(R.id.find_lin);
        me_lin = (LinearLayout )findViewById(R.id.me_lin);

        item_home.setOnClickListener(this);
        item_book.setOnClickListener(this);
        item_faxian.setOnClickListener(this);
        item_me.setOnClickListener(this);
        home_lin.setOnClickListener(this);
        allbook_lin.setOnClickListener(this);
        find_lin.setOnClickListener(this);
        me_lin.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);

        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        fouthFragment = new FouthFragment();
        //给FragmentList添加数据
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);
        mFragmentList.add(threeFragment);
        mFragmentList.add(fouthFragment);
       // checkNet();
    }
    private  boolean checkNet() {
        if (!NetUtil.isWifiConnected(this)&&!NetUtil.isMobileConnected(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_lin:
                vp.setCurrentItem(0, true);
                break;
            case R.id.allbook_lin:
                vp.setCurrentItem(1, true);
                break;
            case R.id.find_lin:
                vp.setCurrentItem(2, true);
                break;
            case R.id.me_lin:
                vp.setCurrentItem(3, true);
                break;
        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            item_home.setTextColor(Color.parseColor("#f84157"));
            item_book.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
            item_me.setTextColor(Color.parseColor("#000000"));
            home_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.home_us));
            allbook_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.allbook_u));
            find_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.find_u));
            penple_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.pen_u));
        } else if (position == 1) {
            item_book.setTextColor(Color.parseColor("#f84157"));
            item_home.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
            item_me.setTextColor(Color.parseColor("#000000"));
            home_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.home_u));
            allbook_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.allbook_us));
            find_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.find_u));
            penple_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.pen_u));
        } else if (position == 2) {
            item_faxian.setTextColor(Color.parseColor("#f84157"));
            item_home.setTextColor(Color.parseColor("#000000"));
            item_book.setTextColor(Color.parseColor("#000000"));
            item_me.setTextColor(Color.parseColor("#000000"));
            home_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.home_u));
            allbook_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.allbook_u));
            find_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.find_us));
            penple_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.pen_u));
        } else if (position == 3) {
            item_me.setTextColor(Color.parseColor("#f84157"));
            item_home.setTextColor(Color.parseColor("#000000"));
            item_book.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
            home_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.home_u));
            allbook_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.allbook_u));
            find_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.find_u));
            penple_v.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.pen_us));
        }
    }
    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 3) {
            Fragment f = new FouthFragment();
            FragmentManager Cmanger= getSupportFragmentManager();
            FragmentTransaction transaction = Cmanger.beginTransaction();
            transaction.replace(R.id.mainViewPager, f);
            transaction.commit();
            vp.setCurrentItem(3);//
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(MainActivity.this,FouthFragment.class);
            i.putExtra("id",3);
        }
        super.onResume();
    }
}