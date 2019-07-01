package com.example.haozai.jen.PassWordView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.haozai.jen.R;

/**
 * Created by haozai on 2018-11-13.
 */

public class PasswordType extends RelativeLayout{
    private Context context;
    private EditText tvPassword;
    private static final String TAG="PasswordType";
    private String last_password = "";
    public PasswordType(Context context) {
        super(context);
        this.context = context;
    }
    public PasswordType(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.password_type, null);
        tvPassword=(EditText) view.findViewById(R.id.last_password);
        addView(view);
    }
    public void setOnFinishInput(final CheckPasswordInputFinish pass) {
        tvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    String temp=s.toString();
                    String tem=temp.substring(temp.length()-1,temp.length());
                    char[]temC=tem.toCharArray();
                    int mid=temC[0];
                    if(temp.length()<10){
                    if(mid>=48&&mid<=57){//数字
                        return;
                    }
                    if(mid>=65&&mid<=90){//大写字母
                        return;
                    }
                    if(mid>97&&mid<=122){//小写字母
                        return;
                    }
                    }
                    s.delete(temp.length()-1,temp.length());
                }catch(Exception e){

                }
                last_password=tvPassword.getText().toString().trim();
                Log.i(TAG,last_password);
                pass.inputFinish();
            }
        });

    }
    public String getLastPassword(){
        return last_password;
    }
}
