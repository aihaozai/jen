package com.example.haozai.jen.ManageForMe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.R;

public class AboutInfo extends AppCompatActivity {
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about_info);
        back = (ImageView) findViewById(R.id.about_back);
        backlister();
    }

    private void backlister() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(AboutInfo.this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
            }
        });
    }
}
