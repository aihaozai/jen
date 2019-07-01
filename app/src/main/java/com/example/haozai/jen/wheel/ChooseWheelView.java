package com.example.haozai.jen.wheel;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.haozai.jen.R;

import java.util.Arrays;

/**
 * Created by haozai on 22/11/2018.
 */

public class ChooseWheelView {
    private Context context ;
    private Dialog  paybottomDialog;
    private static final String TAG = ChooseWheelView.class.getSimpleName();
    private static final String[] PAY = new String[]{"余额","支付宝", "微信", "现金", "POSS机"};
    private static final String[] BILL= new String[]{"不开发票", "发票(图书）", "发票(资料)", "发票(明细)"};
    private boolean flag;
    private setWheel setWheel;
    public void setInter(setWheel setWheel){
        this.setWheel=setWheel;
    }
    public void setData(boolean flags){
       flag=flags;
    }
    public void showPayView(Context context){
        View billView = LayoutInflater.from(context).inflate(R.layout.dialog_select_bill, null);
        final WheelView wvbill = (WheelView) billView.findViewById(R.id.wvbill);
        if (flag) {
            wvbill.setItems(Arrays.asList(PAY), 0);//init selected position is 0 初始选中位置为0
        }else {
            wvbill.setItems(Arrays.asList(BILL), 0);//init selected position is 0 初始选中位置为0
        }
        wvbill.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                Log.d("ddd", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
        TextView bill_ok = (TextView) billView.findViewById(R.id.bill_ok);
        TextView bill_cancel = (TextView) billView.findViewById(R.id.bill_cancel);
        //点击确定
        //防止弹出两个窗口
        if (paybottomDialog != null && paybottomDialog.isShowing()) {
            return;
        }
        paybottomDialog = new BottomDialog(context, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        paybottomDialog.setContentView(billView);
        bill_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paybottomDialog.dismiss();
                setWheel.wheelresult(wvbill.getSelectedItem(),flag);
            }
        });
        //点击取消
        bill_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paybottomDialog.dismiss();
            }
        });
        paybottomDialog.show();
    }
    public void dis(){
        paybottomDialog.dismiss();
    }
    public interface setWheel{
        void wheelresult(String res,boolean flag);
    }
}
