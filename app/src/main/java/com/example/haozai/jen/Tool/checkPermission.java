package com.example.haozai.jen.Tool;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by haozai on 2018-11-17.
 */

public class checkPermission {
    /**
     * 从fragment检查权限，如果传入的fragment是null，则后面请求权限时会调用ActivityCompat
     * 的方法
     *
     * 检查6.0及以上版本时，应用是否拥有某个权限，拥有则返回true，未拥有则再判断上次
     * 用户是否拒绝过该权限的申请（拒绝过则shouldShowRequestPermissionRationale返回
     * true——这里有些手机如红米(红米 pro)永远返回 false
     * 这里的处理是弹一个对话框引导用户去应用的设置界面打开权限，返回false时这里执行
     * requestPermissions方法，此方法会显示系统默认的一个权限授权提示对话框，并在
     * Activity或Fragment的onRequestPermissionsResult得到回调，注意方法中的requestCode
     * 要与此处相同）
     *
     * @param fragment：如果fragment不为null则调用fragment的方法申请权限（因为有些手机
     * 上在Fragment调用ActivityCompat的 方法申请权限得不到回调，例如红米手机）
     * @param activity：用于弹出提示窗和获取权限
     * @param permission：对应的权限名称，如：Manifest.permission.CAMERA
     * @param hint：引导用户进入设置界面对话框的提示文字
     * @param requestCode：请求码，对应Activity或Fragment的onRequestPermissionsResult
     * 方法的requestCode
     * @return：true-拥有对应的权限 false：未拥有对应的权限
     */
    public static boolean checkPermission(Fragment fragment, final Activity activity, String permission,
                                          String hint, int requestCode) {
        //检查权限
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //显示我们自定义的一个窗口引导用户开启权限
                //showPermissionSettingDialog(activity, hint);
            } else {
                //申请权限
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{permission},
                            requestCode);
                } else {
                    fragment.requestPermissions(
                            new String[]{permission},
                            requestCode);
                }
            }
            return false;
        } else {  //已经拥有权限
            return true;
        }
    }
}
