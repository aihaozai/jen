package com.example.haozai.jen.GoodsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.haozai.jen.Book.OrderInfo;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BitmapCache;
import com.example.haozai.jen.Tool.IpAddress;

import java.util.List;

/**
 * Created by haozai on 2018-10-18.
 */

public class CAdapter extends BaseAdapter {

    private Context context;
    private List<OrderInfo> orderlists;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private static String IP = IpAddress.IP;
    //private static String IP = "10.23.32.100:8080";
    String imgUrl;

        public CAdapter(Context context ){
            this.context = context;
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new BitmapCache());
        }
    public void setOrderlists(List<OrderInfo> orderlists) {
        this.orderlists = orderlists;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.acceptlist, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.accept_img = (NetworkImageView)  convertView.findViewById(R.id.accept_img);
        holder.accept_author = (TextView) convertView.findViewById(R.id.accept_author);
        holder.accept_money = (TextView) convertView.findViewById(R.id.accept_money);
        holder.accept_press = (TextView) convertView.findViewById(R.id.accept_press);
        holder.accept_mount = (TextView) convertView.findViewById(R.id.accept_mount);
        holder.accepts_mount_end = (TextView) convertView.findViewById(R.id.accepts_mount_end);
        holder.accept_money_end = (TextView) convertView.findViewById(R.id.accept__money_end);
        holder.accept_yunfei = (TextView) convertView.findViewById(R.id.accept_yunfei);
        final OrderInfo ors =orderlists.get(position);

        holder.accept_author.setText(ors.getOrderinfo_author());
        holder.accept_money.setText(ors.getOrderinfo_price());
        holder.accept_press.setText(ors.getOrderinfo_press());
        holder.accept_mount.setText(ors.getOrderinfo_mount());
        holder.accept_yunfei.setText(ors.getOrderinfo_youfei());
        holder.accepts_mount_end.setText("共"+ors.getOrderinfo_mount()+"件商品");
        holder.accept_money_end.setText(ors.getOrderinfo_zonge());
        holder.accept_img.setTag(position);
        String path = "http://" + IP + "/haozai/img/";
        int pos=(int)holder.accept_img.getTag();

        if(pos==position){
            imgUrl = "http://" + IP + "/haozai/img/"+ors.getOrderinfo_img()+".jpg";

        }else {

        }
        if (imgUrl != null && !imgUrl.equals("")) {
            holder.accept_img.setDefaultImageResId(R.drawable.loading);
            //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
            holder.accept_img.setImageUrl(imgUrl, imageLoader);

        }
        return convertView;
    }

    class ViewHolder {
        NetworkImageView accept_img;
        private TextView accept_author;
        private TextView accept_money;
        private TextView accept_press,accept_mount,accept_yunfei;
        private TextView accepts_mount_end,accept_money_end;
    }

}
