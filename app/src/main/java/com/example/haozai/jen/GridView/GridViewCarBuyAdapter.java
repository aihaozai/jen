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
import com.example.haozai.jen.Car.ShoppingCartBean;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.ImageFileCache;

import java.util.List;

/**
 * Created by haozai on 2018-11-19.
 */

    public class GridViewCarBuyAdapter extends BaseAdapter {
        private RequestQueue queue;
        private ImageLoader imageLoader;
        private List<ShoppingCartBean> itemlist;
        private Context context;
        String imgUrl;
        public GridViewCarBuyAdapter(Context context){
            this.context = context;
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new ImageFileCache());
        }
        public void setCarView(List<ShoppingCartBean> itemlist){
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
                convertView = LayoutInflater.from(context).inflate(R.layout.carbuy_view, parent, false);
                holder = new ViewHolder();
                holder.carimg = (NetworkImageView) convertView.findViewById(R.id.carbuy_img) ;
                holder.count = (TextView) convertView.findViewById(R.id.buycar_count) ;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShoppingCartBean lists = itemlist.get(position);
            holder.count.setText(lists.getCount()+"");
            if (lists.getImageUrl() != null && !lists.getImageUrl().equals("")) {
                holder.carimg.setDefaultImageResId(R.drawable.loading);
                //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
                holder.carimg.setImageUrl(lists.getImageUrl(), imageLoader);
               /* Glide.with(context).
                        load( imgUrl).into(holder.killimg);*/
            }
            return convertView;
        }
        private static class ViewHolder {
            NetworkImageView carimg;
            TextView count;
        }
    }


