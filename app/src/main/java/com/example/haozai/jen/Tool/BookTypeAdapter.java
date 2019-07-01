package com.example.haozai.jen.Tool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.haozai.jen.Book.Booktype;
import com.example.haozai.jen.R;

import java.util.List;

/**
 * Created by haozai on 2018-06-25.
 */


public class BookTypeAdapter extends ArrayAdapter<Booktype> {
    int resource;
    private List<BookTypeAdapter> books;

    public BookTypeAdapter(Context context, int resource, List<Booktype> books) {
        super(context, resource, books);
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        TextView bookname = (TextView) view.findViewById(R.id.bookname);
        Booktype bk = getItem(position);
        bookname.setText(bk.getType());
        return view;
    }
}