package com.example.haozai.jen.PassWordView;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.ToastUtil;

/**
 * Created by haozai on 26/11/2018.
 */

public class PassWordUtil {
    private Dialog PWbottomDialog;
    private PasswordView pwdView;
    private String userid;
    private static Pw pw;
    public void inter(Pw pw){
        this.pw=pw;
    }
    public void showView(final Context context){
        PWbottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.pw_bottom_dialog, null);
        PWbottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        PWbottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        PWbottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        pwdView = (PasswordView) contentView.findViewById(R.id.pwd_view);
        PWbottomDialog.show();
        // 添加回调接口
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                // 输入完成后,输入的密码验证正确
                String pswd = pwdView.getStrPassword();
                pw.pwresult(pswd);
            }
            @Override
            public void outfo() {
                //关闭支付页面
                PWbottomDialog.dismiss();
            }
            //忘记密码回调事件
            @Override
            public void forgetPwd() {
                ToastUtil.showS(context,"忘记密码!");
            }
        });
    }
    public void dis(){
        PWbottomDialog.dismiss();
    }
    public interface Pw{
        void pwresult(String res);
    }
}
