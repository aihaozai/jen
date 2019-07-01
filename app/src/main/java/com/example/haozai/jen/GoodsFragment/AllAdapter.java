package com.example.haozai.jen.GoodsFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BitmapCache;
import com.example.haozai.jen.Tool.IpAddress;
import com.example.haozai.jen.Utils.WebServicePost;

import java.util.List;

/**
 * Created by haozai on 2018-10-18.
 */

public class AllAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> orderlists;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private CancelOrderInterface cancelOrderInterface;
    private Pay pay;
    private static String IP = IpAddress.IP;
    String imgUrl;

        public AllAdapter(Context context ){
            this.context = context;
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new BitmapCache());
        }
    public void setOrderlists(List<OrderInfo> orderlists) {
        this.orderlists = orderlists;
        notifyDataSetChanged();
    }
    public void setCancelOrderInterface(CancelOrderInterface cancelOrderInterface){
        this.cancelOrderInterface = cancelOrderInterface;
        notifyDataSetChanged();
    }
    public void setPay(Pay pay){
        this.pay = pay;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return orderlists == null ? 0 : orderlists.size();
    }

    @Override
    public Object getItem(int position) {
        return orderlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.allgoodslist, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodsimg = (NetworkImageView)  convertView.findViewById(R.id.allgoods_img);
        holder.paying = (TextView) convertView.findViewById(R.id.paying);
        holder.all_author = (TextView) convertView.findViewById(R.id.all_author);
        holder.all_money = (TextView) convertView.findViewById(R.id.all_money);
        holder.all_press = (TextView) convertView.findViewById(R.id.all_press);
        holder.all_mount = (TextView) convertView.findViewById(R.id.all_mount);
        holder.mount_end_all = (TextView) convertView.findViewById(R.id.mount_end_all);
        holder.all_money_end = (TextView) convertView.findViewById(R.id.all_money_end);
        holder.allyunfei = (TextView) convertView.findViewById(R.id.allyunfei);
        holder.allcanel_order = (TextView) convertView.findViewById(R.id.allcancel_order);
        holder.go_all = (TextView) convertView.findViewById(R.id.go_all);
        final OrderInfo ors =orderlists.get(position);
            if (ors.getOrdering()!=null||ors.getOrdering()!="") {
                if (ors.getOrdering().equals("daiing")) {
                    holder.paying.setText("待付款");
                    holder.allcanel_order.setText("取消订单");
                    holder.go_all.setText("立即付款");
                } else if (ors.getOrdering().equals("accepting")) {
                    holder.paying.setText("待收货");
                    holder.allcanel_order.setText("查看物流");
                    holder.go_all.setText("确认收货");
                }
        }
        holder.all_author.setText(ors.getOrderinfo_author());
        holder.all_money.setText(ors.getOrderinfo_price());
        holder.all_press.setText(ors.getOrderinfo_press());
        holder.all_mount.setText(ors.getOrderinfo_mount());
        holder.allyunfei.setText(ors.getOrderinfo_youfei());
        holder.mount_end_all.setText("共"+ors.getOrderinfo_mount()+"件商品");
        holder.all_money_end.setText(ors.getOrderinfo_zonge());
        holder.goodsimg.setTag(position);
        if(holder.allcanel_order.getText().toString().trim().equals("取消订单")) {
            holder.allcanel_order.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alert = new AlertDialog.Builder(context).create();
                            alert.setTitle("订单提示");
                            alert.setMessage("您确定要取消该订单吗？");
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread() {

                                                public void run() {
                                                    Message msg = Message.obtain();
                                                    try {
                                                        String end_result = WebServicePost.CancelOrder(ors.getSid()+"");
                                                        if (end_result.equals("success")) {
                                                            msg.what = 0;
                                                            cancelOrderInterface.orderDelete(position);
                                                        } else if (end_result.equals("fail")) {
                                                            msg.what = 1;
                                                        }
                                                        Handler.sendMessage(msg);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                        msg.what = -1;
                                                        Handler.sendMessage(msg);
                                                    }
                                                }

                                                ;
                                            }.start();
                                        }
                                    });
                            alert.show();

                            notifyDataSetChanged();
                        }
                    }
            );
        }
        if(holder.go_all.getText().toString().trim().equals("立即付款")) {
            holder.go_all.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pay.gopay(ors.getSid()+"", ors.getOrderinfo_zonge(),position);
                        }
                    }
            );
        }
        int pos=(int)holder.goodsimg.getTag();

        if(pos==position){
            imgUrl = "http://" + IP + "/haozai/img/"+ors.getOrderinfo_img()+".jpg";
        }else {

        }
        if (imgUrl != null && !imgUrl.equals("")) {
            holder.goodsimg.setDefaultImageResId(R.drawable.loading);
            //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
            holder.goodsimg.setImageUrl(imgUrl, imageLoader);
        }
        return convertView;
    }

    class ViewHolder {
        NetworkImageView goodsimg;
        private TextView all_author;
        private TextView paying;
        private TextView all_money;
        private TextView all_press,all_mount,allyunfei;
        private TextView mount_end_all,all_money_end;
        private TextView allcanel_order,go_all;
    }
    public interface CancelOrderInterface {

        void orderDelete(int position);
    }
    public interface Pay {
        void gopay(String sid, String money,int position);

    }
    android.os.Handler Handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    Toast.makeText(context, "服务器连接失败!", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(context, "取消订单成功!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "取消订单失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void refresh(List<OrderInfo> orderlists) {
        notifyDataSetChanged();
    }

}
