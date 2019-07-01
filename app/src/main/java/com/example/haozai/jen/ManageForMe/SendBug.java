package com.example.haozai.jen.ManageForMe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.email.MailManager;
import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.ToastUtil;

public class SendBug extends AppCompatActivity implements View.OnClickListener{
    private EditText title,input;
    private TextView tip;
    private Button send;
    private ImageView sendbug_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sendbug);
        title = (EditText) findViewById(R.id.send_title);
        input = (EditText) findViewById(R.id.send_input);
        tip = (TextView) findViewById(R.id.in_tip);
        send = (Button) findViewById(R.id.send_bug_btn);
        sendbug_back = (ImageView) findViewById(R.id.sendbug_back) ;
        sendbug_back.setOnClickListener(this);
        send.setOnClickListener(this);
        lister();
    }

    private void lister() {
        input.addTextChangedListener(new TextWatcher() {
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
                int num = 500-input.length();
                tip.setText("还可以输入"+num+"字");
            }
        });
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_bug_btn:
                    sendEMail();
                    ToastUtil.showL(SendBug.this,"谢谢你的建议，我们会尽快处理！");
                    break;
                case R.id.sendbug_back:
                    Intent i = new Intent();
                    i.setClass(this, MainActivity.class);
                    //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                    i.putExtra("id", 3);
                    startActivity(i);
                    break;
            }
    }

    private void sendEMail() {
        MailManager.getInstance().sendMail(title.getText().toString(), input.getText().toString().trim());
    }

}
