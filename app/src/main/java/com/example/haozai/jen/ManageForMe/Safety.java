package com.example.haozai.jen.ManageForMe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.MyRunnable.Runn.SafetyRunn.GetPhoneRunnable;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Utils.LocalSpUtil;

public class Safety extends AppCompatActivity implements View.OnClickListener,GetPhoneRunnable.GetP{
    private static final String TAG="Safety";
    private LinearLayout log_pswd_m,phone_setting,safety_pswd_mamger;
    private ImageView safety_back;
    private TextView sate_show_p,tv_pswd_ing;
    private GetPhoneRunnable getPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_safety);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_safety);
        initView();
        initData();
    }
    private void initView() {
        log_pswd_m = (LinearLayout)findViewById(R.id.log_pswd_m);
        phone_setting = (LinearLayout)findViewById(R.id.phone_setting);
        safety_pswd_mamger = (LinearLayout)findViewById(R.id. safety_pswd_mamger);
        sate_show_p = (TextView) findViewById(R.id.sate_show_p);
        tv_pswd_ing = (TextView) findViewById(R.id.tv_pswd_ing) ;
        safety_back = (ImageView) findViewById(R.id.safety_back) ;
        log_pswd_m.setOnClickListener(this);
        phone_setting.setOnClickListener(this);
        safety_pswd_mamger.setOnClickListener(this);
        safety_back.setOnClickListener(this);
    }
    private void initData() {
        getPhone = new GetPhoneRunnable();
        getPhone.setGetP(this);
        String id = LocalSpUtil.getUserId(Safety.this);
        getPhone.goGet(id);
        String pswd_tv = LocalSpUtil.getPswdExit(Safety.this);
        if(pswd_tv!=null&&pswd_tv.equals("exist")){
            tv_pswd_ing.setText("你已经设置密码，可以点此修改");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.log_pswd_m:
                startActivity(new Intent(Safety.this,Log_Pswd_Manage.class));
                break;
            case R.id.phone_setting:
                Intent intent = new Intent(Safety.this, Log_Pswd_Manage.class);
                intent.putExtra("phone", "phone");
                startActivity(intent);
                break;
            case R.id.safety_pswd_mamger:
                Intent intent1 = new Intent(Safety.this, Log_Pswd_Manage.class);
                intent1.putExtra("pswd", "pswd");
                startActivity(intent1);
                break;
            case R.id.safety_back:
                Intent i = new Intent();
                i.setClass(this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
                break;
        }
    }

    @Override
    public void result(String res) {
        if(res!="ERROR"&&res!=""&&res!=null) {
            String maskNumber = res.substring(0, 3) + "****" + res.substring(7, res.length());
            String num_end = "[" + maskNumber + "]";
            sate_show_p.setText(num_end);
        }else {
            Toast.makeText(Safety.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
        }
    }
}
