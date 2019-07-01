package com.example.haozai.jen.Address;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.haozai.jen.MainActivity;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.StatusBarUtil;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity  implements View.OnClickListener,OneExpandAdapter.DeleteInterface {
    private OneExpandAdapter oneAdapter;
    private ImageView back_address;
    String result;
    private static Handler handler = new Handler();
    private List<UserAddress> userAddressList = new ArrayList<>();
    ListView address_listview;
    private String readstring;
    private Button add_go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_address);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        initview();
        // 创建子线程
        new Thread(new MyThread()).start();

    }

    private void initview() {
        back_address = (ImageView) findViewById(R.id.back_address);
        address_listview= (ListView) findViewById(R.id.address_listview);
        add_go= (Button) findViewById(R.id.btn_add_Address);
        add_go.setOnClickListener(this);
        oneAdapter = new OneExpandAdapter(this);
        address_listview.setAdapter(oneAdapter);
        oneAdapter.setDeleteInterface(this);
        oneAdapter.setAddressList(userAddressList);
        back_address.setOnClickListener(this);
        readstring = LocalSpUtil.getUserId(this);

    }

    @Override
    public void childDelete(int position) {
        userAddressList.remove(position);
        oneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_Address:
                Intent intent = new Intent(this,Add_address.class);
                startActivity(intent);
                break;
            case R.id.back_address:
                Intent i = new Intent();
                i.setClass(this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
                break;
            }
    }

    public class MyThread implements Runnable {

        @Override
        public void run() {
            result = WebService.addresssManage(readstring);
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // test.setText(result);
                        Bundle bundle = new Bundle();
                        bundle.putString("result",result);
                        Message message = new Message();
                        message.what=0;
                        message.setData(bundle);
                        findHandler.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
    Handler findHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            bundle = msg.getData();
            String result = bundle.getString("result");
            userAddressList = jsonList(result);
            address_listview.setAdapter(oneAdapter);
            oneAdapter.setAddressList(userAddressList);

        }
    };
    public List<UserAddress> jsonList(String jsonString) {
        List<UserAddress> useraddress = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                UserAddress uas = new UserAddress();
                uas.setAddress(jsonObject.optString("address"));
                uas.setDetail(jsonObject.optString("detail"));
                uas.setMing(jsonObject.optString("ming"));
                uas.setPnumber(jsonObject.optString("pnumber"));
                uas.setKeynum(jsonObject.optInt("keynum"));
                uas.setSid(jsonObject.optInt("sid"));
                useraddress.add(uas);
            }
            return useraddress;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
