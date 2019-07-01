package com.example.haozai.jen.ManageForMe;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haozai.jen.R;

/**
 * Created by haozai on 2018-11-13.
 */

public class DialogUtil implements View.OnClickListener{
    private Dialog dialog;
    private View contentView;
    private ImageView choose_p_img,choose_pswd_img,choose_guanbi;
    private LinearLayout c_p_lin,c_pswd_lin;
    private TextView choose_p_tv,choose_pswd_tv;
    private boolean flag=true;
    private Actiongo actiongo;
    public void setActionInterface(Actiongo actiongo) {
        this.actiongo = actiongo;
    }
    public void showRightDialog(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_right, null);
        dialog = new Dialog(context, R.style.DialogRight);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels*3/4;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels;
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.RIGHT);
        choose_p_img = (ImageView)  contentView.findViewById(R.id.choose_p_img);
        choose_pswd_img = (ImageView)  contentView.findViewById(R.id.choose_pswd_img);
        choose_guanbi = (ImageView)  contentView.findViewById(R.id.choose_guanbi);
        c_p_lin = (LinearLayout)  contentView.findViewById(R.id.c_p_lin);
        c_pswd_lin = (LinearLayout)  contentView.findViewById(R.id.c_pswd_lin);
        choose_p_tv = (TextView)  contentView.findViewById(R.id.choose_p_tv);
        choose_pswd_tv = (TextView) contentView.findViewById(R.id.p_safety_tv);
        choose_p_img.setImageResource(R.drawable.select);
        viewListener();
    }
    private void viewListener() {
        choose_guanbi.setOnClickListener(this);
        c_p_lin.setOnClickListener(this);
        c_p_lin.setOnClickListener(this);
        c_pswd_lin.setOnClickListener(this);
    }
    //关闭dialog时调用
    public void close() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    public void show() {
        dialog.show();
        }
    public void setPhone(String num) {
        choose_p_tv.setText(num);
    }
    public void setViewVisibilty() {
        c_pswd_lin.setVisibility(View.GONE);
    }
    public void showoldPswd(String str) {
        choose_pswd_tv.setText(str);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.c_p_lin:
                choose_p_img.setImageResource(R.drawable.select);
                choose_pswd_img.setImageResource(R.drawable.select_no);
                dialog.dismiss();
                actiongo.setflag(true);
                break;
            case R.id.c_pswd_lin:
                choose_p_img.setImageResource(R.drawable.select_no);
                choose_pswd_img.setImageResource(R.drawable.select);
                actiongo.setflag(false);
                dialog.dismiss();
                break;
        }
    }


    public interface Actiongo {
        void setflag(boolean flag);
    }
}
