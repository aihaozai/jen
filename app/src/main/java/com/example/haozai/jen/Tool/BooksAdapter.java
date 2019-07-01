package com.example.haozai.jen.Tool;

/**
 * Created by haozai on 2018-06-26.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.ImageFileCache;
import java.util.List;


/**
 * Created by haozai on 2018-06-25.
 */


public class BooksAdapter extends BaseAdapter {
    private Context context;
    private List<Books> bookslists;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private static String IP = IpAddress.IP;
    //private static String IP = "116.255.170.88:8080";
    String imgUrl;
    public BooksAdapter(Context context ){
        this.context = context;
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue,new ImageFileCache());
    }
    public void setBooksList(List<Books> bookslists) {
        this.bookslists = bookslists;
    }
    @Override
    public int getCount() {
        return bookslists == null ? 0 : bookslists.size();
    }

    @Override
    public Object getItem(int position) {
        return bookslists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bookslist, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img = (NetworkImageView)  convertView.findViewById(R.id.bookimg);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.author = (TextView) convertView.findViewById(R.id.author);
        holder.press = (TextView) convertView.findViewById(R.id.press);
        holder.price = (TextView) convertView.findViewById(R.id.price);

        final Books bs =bookslists.get(position);

        holder.name.setText(bs.getName());
        holder.press.setText(bs.getPress());
        holder.author.setText(bs.getAuthor());
        holder.price.setText(bs.getPrice());
        holder.img.setTag(position);
        String path = "http://" + IP + "/haozai/img/";
        int pos=(int)holder.img.getTag();

        if(pos==position){
             imgUrl = "http://" + IP + "/haozai/img/"+bs.getBooknum()+".jpg";

        }else {

        }
        if (imgUrl != null && !imgUrl.equals("")) {
            holder.img.setDefaultImageResId(R.drawable.loading);
          //  holder.img.setErrorImageResId(R.drawable.ic_launcher);
            holder.img.setImageUrl(imgUrl, imageLoader);

        }
        return convertView;
    }
    /**
     * 释放资源
     */
    public void releaseResource() {
        //ViewHandler.removeCallbacksAndMessages(null);
        context = null;
    }
    class ViewHolder {
        NetworkImageView img;
        private TextView name;
        private TextView author;
        private TextView press;
        private TextView price;
    }
}
