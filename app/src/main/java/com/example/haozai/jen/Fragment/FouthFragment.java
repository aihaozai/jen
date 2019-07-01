package com.example.haozai.jen.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.haozai.jen.Address.AddressActivity;
import com.example.haozai.jen.Car.ShoppingCartActivity;
import com.example.haozai.jen.GoodsFragment.GoodsActivity;
import com.example.haozai.jen.ManageForMe.AboutInfo;
import com.example.haozai.jen.ManageForMe.Cash;
import com.example.haozai.jen.ManageForMe.Safety;
import com.example.haozai.jen.ManageForMe.SendBug;
import com.example.haozai.jen.MyRunnable.Runn.FreagmentNum.FouthRunn;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;
import com.example.haozai.jen.Tool.IpAddress;
import com.example.haozai.jen.Tool.UploadUtil;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.PhotoUtils;
import com.example.haozai.jen.Utils.ToastUtil;
import com.example.haozai.jen.conn.Login;
import com.example.haozai.jen.conn.User;
import com.jauker.widget.BadgeView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class FouthFragment extends BaseFragment implements View.OnClickListener, UploadUtil.OnUploadProcessListener,FouthRunn.GetNum {
    private static final String TAG = "FouthFragment";
    // 标志位，标志已经初始化完成。
    private boolean isPrepared,viewSwiper;
    private List<User> list;
    private ImageView car, daipay, daifa, daishou,can_login,yue;
    private CircleImageView mImageView;
    private TextView username,retou;
    private SharedPreferences sp;
    private LinearLayout myorder,address_manage, safety_manage_go,send_bug, about;
    private BadgeView badgeView, badge_dai, badge_accept;
    private FouthRunn fouthRunn;
    private SweetAlertDialog pDialog;
    int t, a, b, c;
    String  tou;
    String id;
    /**
     * 判断的标识
     */
    private static final int PHOTO_REQUEST = 1;
    private static final int UPLOAD_INIT_PROCESS = 4;//上传初始化
    protected static final int UPLOAD_FILE_DONE = 2;//上传中
    private static final int UPLOAD_IN_PROCESS = 5;//上传文件响应
    protected static final int TAKE_PICTURE = 0;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri;
    private File fileCropUri;
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.me, container, false);
        initView(view);
        if (isPrepared == false) {
            viewSwiper = false;
            Messagedata();
        } else {
            viewSwiper = true;
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        isPrepared = true;
        if (!isPrepared || !isVisible) {
        } else {
            isPrepared = false;
            if (viewSwiper) {
                Messagedata();
                viewSwiper = false;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        //控件
        mImageView = (CircleImageView) view.findViewById(R.id.tou);
        car = (ImageView) view.findViewById(R.id.car);
        daipay = (ImageView) view.findViewById(R.id.daipay);
        daifa = (ImageView) view.findViewById(R.id.daifa);
        daishou = (ImageView) view.findViewById(R.id.daishou);
        username = (TextView) view.findViewById(R.id.username);
        yue = (ImageView) view.findViewById(R.id.yue);
        can_login = (ImageView) view.findViewById(R.id.can_login);
        retou = (TextView) view.findViewById(R.id.retou);
        myorder = (LinearLayout) view.findViewById(R.id.myorder);
        address_manage = (LinearLayout) view.findViewById(R.id.address_manage);
        safety_manage_go = (LinearLayout) view.findViewById(R.id.safety_manage_go);
        about = (LinearLayout) view.findViewById(R.id.about);
        send_bug = (LinearLayout) view.findViewById(R.id.send_bug) ;
        myorder.setOnClickListener(this);
        address_manage.setOnClickListener(this);
        can_login.setOnClickListener(this);
        retou.setOnClickListener(this);
        daishou.setOnClickListener(this);
        car.setOnClickListener(this);
        daipay.setOnClickListener(this);
        daifa.setOnClickListener(this);
        safety_manage_go.setOnClickListener(this);
        send_bug.setOnClickListener(this);
        about.setOnClickListener(this);
        yue.setOnClickListener(this);
    }

    private void Messagedata() {
        badgeView = new com.jauker.widget.BadgeView(getActivity());
        badge_dai = new com.jauker.widget.BadgeView(getActivity());
        badge_accept = new com.jauker.widget.BadgeView(getActivity());
        badgeView.setTargetView(car);
        badge_dai.setTargetView(daipay);
        badge_accept.setTargetView(daishou);
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badge_dai.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badge_accept.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        //从本地获取登录信息
        tou = LocalSpUtil.getTou(getActivity());
        id = LocalSpUtil.getUserId(getActivity());
        String readstring = LocalSpUtil.getLogining(getActivity());
        if (!readstring.equals("success")) {
            startActivity(new Intent(getActivity(), Login.class));
        } else {
            fouthRunn = new FouthRunn();
            fouthRunn.inter(this);
            fouthRunn.settingData(id);
        }
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.can_login:
                LocalSpUtil.RemoveAll();
                startActivity(new Intent(getActivity(), Login.class));
                break;
            case R.id.retou:
                showChoosePicDialog();
                break;
            case R.id.car:
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                break;
            case R.id.myorder:
                startActivity(new Intent(getActivity(), GoodsActivity.class));
                break;
            case R.id.address_manage:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.yue:
                startActivity(new Intent(getActivity(), Cash.class));
                break;
            case R.id.daipay:
                Intent in = new Intent();
                in.setClass(getActivity(), GoodsActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                in.putExtra("id", 1);
                startActivity(in);
                break;
            case R.id.daifa:
                Intent i = new Intent();
                i.setClass(getActivity(), GoodsActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                i.putExtra("id", 2);
                startActivity(i);
                break;
            case R.id.daishou:
                Intent c = new Intent();
                c.setClass(getActivity(), GoodsActivity.class);
                //一定要指定是第几个pager，因为要跳到BFragment，这里填写2
                c.putExtra("id", 3);
                startActivity(c);
                break;
            case R.id.safety_manage_go:
                startActivity(new Intent(getActivity(), Safety.class));
                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutInfo.class));
                break;
            case R.id.send_bug:
                startActivity(new Intent(getActivity(), SendBug.class));
                break;
        }
    }
    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加图片");
        String[] items = {"拍照", "选择本地照片",};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        autoObtainCameraPermission(); // 拍照
                        break;
                    case PHOTO_REQUEST: // 选择本地照片
                        autoObtainStoragePermission();
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * 申请访问相机权限
     */
    private void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtil.showS(getActivity(), "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    imageUri = Uri.fromFile(fileUri);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.haozai.fileprovider", fileUri);
                    }
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtil.showS(getActivity(), "设备没有SD卡！");
                }
            }
        }
    }

    /**
     * 动态申请sdcard读写权限
     */
    private void autoObtainStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
            }
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.haozai.fileprovider", fileUri);
                        }
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showS(getActivity(), "设备没有SD卡！");
                    }
                } else {
                    ToastUtil.showS(getActivity(), "请允许打开相机！！");
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.showS(getActivity(), "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                cropImageUri = Uri.fromFile(fileCropUri);
                PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:
                if (hasSdcard()) {
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(getActivity(), "com.haozai.fileprovider", new File(newUri.getPath()));
                    }
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                } else {
                    ToastUtil.showS(getActivity(), "设备没有SD卡！");
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, getActivity());
                    //上传照片
                if (bitmap!=null) {
                    toUploadFile();
                    mImageView.setImageBitmap(bitmap);
                    //showImages(bitmap);
                }
                break;
            default:
        }
    }

    /**
     * 上传图片到服务器
     */
    private void toUploadFile() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在上传....");
        pDialog.setCancelable(false);
        pDialog.show();
        String fileKey = "avatarFile";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this); //设置监听器监听上传状态
        Map<String, String> params = new HashMap<String, String>();//上传map对象
        params.put("userId", "");
        String photoip = IpAddress.photoIP;
        uploadUtil.uploadFile(fileCropUri, fileKey, photoip, params);
        ToastUtil.showS(getActivity(), "上传成功");
    }

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        //上传完成响应
        pDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        //上传中
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
    }

    @Override
    public void initUpload(int fileSize) {
        //准备上传
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void result(String res, String aaa, String ccc, String bbb) {
        if (res != "ERROR") {
            try {
                a = Integer.parseInt(aaa);
                b = Integer.parseInt(bbb);
                c = Integer.parseInt(ccc);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            badgeView.setBadgeCount(c);
            badge_dai.setBadgeCount(a);
            badge_accept.setBadgeCount(b);

            list = all_json.jsonUserList(res);
            username.setText(list.get(0).getUsername());
                //记录
                try {
                    SharedPreferences.Editor editor = sp.edit();//获取编辑对象ID
                    String touxiang = String.valueOf(list.get(0).getSid());
                    editor.putString("keytou", touxiang);
                    editor.commit();//提交保存修改
                } catch (Exception e) {
                }
                t = list.get(0).getSid();

            fileUri = new File(Environment.getExternalStorageDirectory(), "/" + t + ".jpg");
            fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + t + ".jpg");
            String updateTime = String.valueOf(System.currentTimeMillis());
            String pho = IpAddress.setphotoIP;
           // Glide.with(getActivity()).load(pho+"/"+t+".jpg") .signature(new StringSignature(updateTime)).into(mImageView);
            Glide.with(this)
                    .load(pho+"/"+t+".jpg")
                    .signature(new StringSignature(UUID.randomUUID().toString()))  // 重点在这行
                    .error(R.mipmap.ic_launcher)
                    .into(mImageView);

        } else {
        ToastUtil.showS(getActivity(),"连接服务器失败！");
        }
    }
}





