package com.example.haozai.jen.Start;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.haozai.jen.R;

import java.util.ArrayList;

public class daohang extends Activity {
    ViewPager viewPage;
    TextView tvStart;
    TextView[] textColor=new TextView[3];
    private boolean isFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_daohang);
        viewPage = (ViewPager) findViewById(R.id.vp_page);
        tvStart = (TextView) findViewById(R.id.tv_start);

        LayoutInflater infl = LayoutInflater.from(this);
        final ArrayList<View> views = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            String name = "what" + i;
            views.add(infl.inflate(getResources().getIdentifier(name, "layout", getPackageName()), null));
        }
        //初始化下面的三个圆点  
        textColor[0] = (TextView) findViewById(R.id.tv_paget1);
        textColor[1] = (TextView) findViewById(R.id.tv_paget2);
        textColor[2] = (TextView) findViewById(R.id.tv_paget3);


        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub  
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub  
                return views.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        };

        viewPage.setAdapter(adapter);
        viewPage.addOnPageChangeListener(new MyonPageChangeListener());
        viewPage.setOffscreenPageLimit(3);
        tvStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(daohang.this,start.class);
                startActivity(intent);
            }

        });

    }


    class MyonPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub  

        }

        @Override
        public void onPageScrolled(int narg0, float arg1, int arg2) {
            // TODO Auto-generated method stub  

        }

        @Override
        public void onPageSelected(int position) {
            //滑动到哪一个页面就把对应的圆点颜色改变  
            textColor[position].setBackgroundColor(Color.RED);
            //如果不是第一页就把上一页对应的圆点颜色还原  
            if (position != 0) {
                textColor[position - 1].setBackgroundColor(Color.WHITE);
            }
            //如果不是最后一页就把下一页对应的圆点颜色还原  
            if (position != 2) {
                textColor[position + 1].setBackgroundColor(Color.WHITE);
                //隐藏启动按钮  
                tvStart.setVisibility(View.GONE);
            }
            //如果是最后一页就显示启动按钮  
            if (position == 2) {
                tvStart.setVisibility(View.VISIBLE);
            }
        }

    }

}