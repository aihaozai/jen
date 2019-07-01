package com.example.haozai.jen.GridView;

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
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.IpAddress;
import com.example.haozai.jen.Utils.ImageFileCache;

import java.util.List;

/**
 * Created by haozai on 2018-11-10.
 */

public class ThreeGridViewAdapter extends BaseAdapter {
    private static String IP = IpAddress.IP;
    //private static String IP = "10.23.32.100:8080";
    private List<RecommendBean> rebean;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private Context context;
    String imgUrl;
    public ThreeGridViewAdapter(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue,new ImageFileCache());
    }
    public void setThreeGridView(List<RecommendBean> rebean){
        this.rebean = rebean;
    }
    @Override
    public int getCount() {
        return rebean.size();
    }

    @Override
    public Object getItem(int position) {
        return rebean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.recom_view, parent, false);
            holder = new ViewHolder();
            holder.gvimg = (NetworkImageView) convertView.findViewById( R.id.gvimg) ;
            holder.rename = (TextView) convertView.findViewById(R.id.tv_re_name);
            holder.reauthor = (TextView) convertView.findViewById(R.id.tv_re_author);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendBean list = rebean.get(position);
        holder.rename.setText(list.getName());
        holder.reauthor.setText(list.getAuthor());
        imgUrl = "http://" + IP + "/haozai/img/"+list.getNum()+".jpg";
        if (list.getNum() != null && !list.getNum().equals("")) {
            holder.gvimg.setDefaultImageResId(R.drawable.loading);
            //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
            holder.gvimg.setImageUrl(imgUrl, imageLoader);

        }
        return convertView;
    }
    private static class ViewHolder{
        NetworkImageView gvimg;
        private TextView rename;
        private TextView reauthor;
    }
}
