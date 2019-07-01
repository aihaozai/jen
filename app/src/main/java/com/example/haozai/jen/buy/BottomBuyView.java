package com.example.haozai.jen.buy;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.IpAddress;

/**
 * Created by haozai on 24/11/2018.
 */

public class BottomBuyView implements View.OnClickListener{
    private  Dialog bottomDialog;
    private ImageView dialog_book_name;
    private TextView dialog_money;
    private TextView tv_mount;
    private TextView goods_cun;
    private AmountView mAmountView;
    private Button dialog_send;
    private ImageView guanbi;
    private setDialog dialog;
    public void setInter(setDialog dialog){
        this.dialog = dialog;
    }
    public void showBooksView(Context context,String str ,String urls){
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        guanbi =(ImageView)  contentView.findViewById(R.id.guanbi);
        dialog_money = (TextView)  contentView.findViewById(R.id.dialog_money);
        tv_mount= (TextView)  contentView.findViewById(R.id.much);
        goods_cun= (TextView)  contentView.findViewById(R.id.goods_cun);
        dialog_send =(Button)  contentView.findViewById(R.id.dialog_send);
        dialog_book_name = (ImageView) contentView.findViewById(R.id.dialog_book_name);
        guanbi.setOnClickListener(this);
        dialog_send.setOnClickListener(this);
        mAmountView = (AmountView)contentView.findViewById(R.id.amount_view);
        goods_cun.setText(50+"");
        mAmountView.setGoods_storage(50);
        tv_mount.setText(1+"");
        dialog_money.setText(str);
        String imageip = IpAddress.imgIP;
        Glide.with(context).load(imageip+urls+".jpg").into(dialog_book_name);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                tv_mount.setText(amount+"");
            }
        });
        bottomDialog.show();
    }

    public void dis(){
        bottomDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_send:
                dialog.result(tv_mount.getText().toString().trim());
                bottomDialog.dismiss();
                break;
            case R.id.guanbi:
                bottomDialog.dismiss();
                break;
        }
    }
    public interface setDialog{
        void result(String res);
    }
}
