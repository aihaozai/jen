package com.example.haozai.jen.Tool;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.haozai.jen.R;
import com.example.haozai.jen.conn.User;

import java.util.List;

/**
 * Created by acer on 2018-06-13.
 */

public class UserAdapter extends ArrayAdapter<User> {
    int resource;
    private List<User> bookList;
    public UserAdapter(Context context, int resource, List<User> bookList) {
        super(context, resource, bookList);
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        else {
            view = convertView;
        }

        TextView uname = (TextView) view.findViewById(R.id.uname);
        TextView upassword = (TextView) view.findViewById(R.id.upassword);
        User user= getItem(position);

        uname.setText(user.getUsername());
        upassword.setText(user.getPassword());
        return view;
    }
}
