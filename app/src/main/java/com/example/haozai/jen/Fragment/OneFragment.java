package com.example.haozai.jen.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.haozai.jen.GridView.GVBooks;
import com.example.haozai.jen.GridView.GridViewAdapter;
import com.example.haozai.jen.GridView.GridViewForScrollView;
import com.example.haozai.jen.GridView.RecommendBean;
import com.example.haozai.jen.GridView.SecondKill;
import com.example.haozai.jen.GridView.ThreeGridViewAdapter;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.WebService;
import com.example.haozai.jen.Utils.WebServicePost;
import com.example.haozai.jen.View.ImageSlideshow;
import com.example.haozai.jen.View.ImageTitleBean;
import com.example.haozai.jen.View.RushBuyCountDownTimerView;
import com.example.haozai.jen.View.TextSwitchView;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class OneFragment extends BaseFragment implements RushBuyCountDownTimerView.Next{
    private static final String TAG = "OneFragment";
    // 标志位，标志已经初始化完成。
    private boolean isPrepared ;
    private List<SecondKill> sklists;
    private List<RecommendBean>recommendBeanList;
    private List<ImageTitleBean> imageTitlelists;
    private GridView gridviews;
    private GridViewForScrollView re_grid_view;
    private GridViewAdapter gvadapter;
    private ThreeGridViewAdapter threeGvAdapter;
    private ImageSlideshow imageSlideshow;
    private TextSwitchView textSwitchView;
    private RushBuyCountDownTimerView timerView;
    String viewpager_data, horiview_result,re_result;
    private static Handler handler = new Handler();
    int result_time = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initView(view);
        return view;
    }

    @Override
    protected void lazyLoad() {
        isPrepared = true;

        if (!isPrepared || !isVisible) {
        }else {
            new Thread(new MyThread()).start();
        }
    }

    private void initView(View view) {
        imageSlideshow = (ImageSlideshow) view.findViewById(R.id.is_gallery);
        gridviews = (GridView) view.findViewById(R.id.grid_view);
        textSwitchView = (TextSwitchView) view.findViewById(R.id.switchtext);
        timerView = (RushBuyCountDownTimerView) view.findViewById(R.id.timeView);
        re_grid_view = (GridViewForScrollView) view.findViewById(R.id.re_grid_view);
        // 为ImageSlideshow设置数据
        gvadapter = new GridViewAdapter(getActivity());
        threeGvAdapter = new ThreeGridViewAdapter(getActivity());
        timerView.setNextTime(this);
}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new MyRunnable()).start();
    }

    private void InitData() {
        String[] autoRes = {"甄甄书城正式上线啦", "注册即购书优惠券", "各类图书特价，大便卖", "满减活动还送图书"};
        textSwitchView.setResources(autoRes);
        textSwitchView.setTextStillTime(2000);

    }

    @Override
    public void nextTime(boolean flag) {
        Log.i(TAG, flag+"");
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                viewpager_data = WebService.Viewpager();
                horiview_result = WebService.SecondKill();
                re_result = WebServicePost.Recommond();
                if (horiview_result.equals("") || viewpager_data == "") {
                    Toast.makeText(getActivity(), horiview_result + viewpager_data, Toast.LENGTH_SHORT).show();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putString("viewpager_result", viewpager_data);
                            bundle.putString("horiview_result", horiview_result);
                            bundle.putString("re_result",re_result);
                            Message message = new Message();
                            message.setData(bundle);
                            controlHandler.sendMessage(message);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = 1;
                controlHandler.sendMessage(msg);
            }
        }
    }

    Handler controlHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    Toast.makeText(getActivity(), "服务器连接失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
            Bundle bundle;
            bundle = msg.getData();
            String view_result = bundle.getString("viewpager_result");
            if( view_result!=null) {
                imageTitlelists = all_json.jsonimageview(view_result);
            }
            String kills = bundle.getString("horiview_result");
            String relist = bundle.getString("re_result");
            if (relist!=null) {
                recommendBeanList = all_json.jsonREList(relist);
                setThreeGridViewdata();
            }
           if (kills!=null) {
               sklists = all_json.jsonkill(kills);
               setGridViewdata();
           }
            setimageTitledata();
            InitData();
        }
        private void setimageTitledata() {
            imageSlideshow.setImageTitleBeanList(imageTitlelists);
            imageSlideshow.setDotSpace(12);
            imageSlideshow.setDotSize(12);
            imageSlideshow.setDelay(3000);
            imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (position) {

                    }
                }
            });
            imageSlideshow.commit();
        }

        private void setGridViewdata() {
                int listSize = sklists.size();
                // 得到像素密度
                DisplayMetrics outMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                float density = outMetrics.density; // 像素密度
                // 根据item的数目，动态设定gridview的宽度,现假定每个item的宽度和高度均为170dp，列间距为10dp
                ViewGroup.LayoutParams par_gridViews = gridviews.getLayoutParams();
                int itemWidth_jieduan = (int) (70 * density);//每个item宽度
                int spacingWidth = (int) (10 * density);
                par_gridViews.width = itemWidth_jieduan * listSize + (listSize - 1) * spacingWidth;
                gridviews.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式
                gridviews.setNumColumns(listSize);
                gridviews.setHorizontalSpacing(spacingWidth);
                gridviews.setColumnWidth(itemWidth_jieduan);
                gridviews.setLayoutParams(par_gridViews);
                gvadapter.setSecondKill(sklists);
                gridviews.setAdapter(gvadapter);
                 gridviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), GVBooks.class);
                    intent.putExtra("url",sklists.get(position).getItem_num());
                    intent.putExtra("money",sklists.get(position).getItem_price());
                    intent.putExtra("title","限时秒杀");
                    startActivity(intent);
                }
            });

        }
    };
    private void setThreeGridViewdata() {
        threeGvAdapter.setThreeGridView(recommendBeanList);
        re_grid_view.setAdapter(threeGvAdapter);
        re_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GVBooks.class);
                intent.putExtra("url",recommendBeanList.get(position).getNum());
                intent.putExtra("money",recommendBeanList.get(position).getPrice());
                intent.putExtra("title","每周推荐");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroy() {
        // 释放资源
       imageSlideshow.releaseResource();
        super.onDestroy();
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            URL url = null;//取得资源对象
            try {
                String killtime = WebService.end_kill();
                int end_time = Integer.parseInt(killtime);
                url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                URLConnection uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                long ld = uc.getDate(); //取得网站日期时间
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ld);
                final String format = formatter.format(calendar.getTime());
                String lasttime = String.valueOf(formatter.parse(format).getTime() / 1000);
                int start_time = Integer.parseInt(lasttime);
                result_time = end_time - start_time;
                if(start_time!=0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("time", result_time);
                    Message message = new Message();
                    message.setData(bundle);
                    timeHandler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    Toast.makeText(getActivity(), "服务器连接失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
            Bundle bundle;
            bundle = msg.getData();
            int timetime = bundle.getInt("time");
            // 设置时间(day,hour,min,sec)
            // timerView.setTime(0, 0, 0, 5);
            //int sum = timetime;
            // 把秒数传到倒计时方法中。。
            if(timetime>=0) {
                timerView.addTime(timetime);
                // 开始倒计时
                timerView.start();
            }
        }
    };
}








