package com.example.haozai.jen.Tool;

import android.content.Context;
import android.widget.Toast;

import com.example.haozai.jen.Utils.ToastUtil;

/**
 * Created by haozai on 24/11/2018.
 */

public class ToastRunn {
    private boolean flag=true;
    public void showBuyToast(Context context,int number) {
        switch (number) {
            case -1:
                ToastUtil.showS(context, "连接服务器失败");
                break;
            case 1:
                ToastUtil.showS(context, "密码错误，清再次输入！");
                break;
            case 2:
                ToastUtil.showS(context,"取消支付,请前往支付！");
                break;
            case 4:
                ToastUtil.showS(context,"支付失败！");
                break;
            case 5:
                ToastUtil.showS(context,"购买成功！");
                break;
        }
    }
    public boolean showPayToast(Context context,String pay,String bill,String pswdexit) {
        if (pay.equals("请选择")){
            ToastUtil.showS(context, "请选择支付方式");
            return false;
        }
        if (bill.equals("请选择")) {
            ToastUtil.showS(context, "是否打印发票");
            return false;
        }
        if (pay!="余额") {
            ToastUtil.showS(context, "目前只支持余额付款");
            return false;
        }
        if(pay=="余额"&&!bill.equals("请选择")){
            if (pswdexit!=null||pswdexit!=""&&pswdexit.equals("exist")) {
                return true;
            } else {
                flag = false;
                ToastUtil.showL(context, "请设置密码！");
                return false;
            }
        }else {
            ToastUtil.showS(context, "是否打印发票");
            return false;
        }
    }
    public boolean goset(){
        return flag;
    }

    public void addcarToast(Context context,int number) {
        switch (number) {
            case -1:
                Toast.makeText(context, "服务器开小差", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Toast.makeText(context, "加入购物车成功", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context, "加入购物车失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
