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

import static com.example.haozai.jen.R.id.kill_img;

/**
 * Created by haozai on 2018-10-24.
 */

    public class GridViewAdapter extends BaseAdapter {
        private static String IP = IpAddress.IP;
        private RequestQueue queue;
        private ImageLoader imageLoader;
        private List<SecondKill> itemlist;
        private Context context;
        String imgUrl;

        public GridViewAdapter(Context context){
            this.context = context;
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new ImageFileCache());
        }
        public void setSecondKill(List<SecondKill> itemlist){
            this.itemlist = itemlist;
        }
        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int position) {
            return itemlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.kill_view, parent, false);
                holder = new ViewHolder();
                holder.killimg = (NetworkImageView) convertView.findViewById(kill_img) ;
                holder.kill_name = (TextView) convertView.findViewById(R.id.kill_name);
                holder.kill_price = (TextView) convertView.findViewById(R.id.kill_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SecondKill lists = itemlist.get(position);
            holder.kill_name.setText(lists.getName());
            holder.kill_price.setText(lists.getItem_price());
            imgUrl = "http://" + IP + "/haozai/img/"+lists.getItem_num()+".jpg";
            if (imgUrl != null && !imgUrl.equals("")) {
                holder.killimg.setDefaultImageResId(R.drawable.loading);
                //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
                holder.killimg.setImageUrl(imgUrl, imageLoader);
               /* Glide.with(context).
                        load( imgUrl).into(holder.killimg);*/
            }
            return convertView;
        }
        private static class ViewHolder {
            NetworkImageView killimg;
            private TextView kill_name;
            private TextView kill_price;
        }
    }


