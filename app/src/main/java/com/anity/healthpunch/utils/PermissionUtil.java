package com.anity.healthpunch.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public static Boolean checkPermission(Activity activity, String[] permissions, int requestCode) {
        //如果Android版本大于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (String permission : permissions) {
                int check = ContextCompat.checkSelfPermission(activity, permission);
                //未开启权限则请求系统弹窗，让用户选择是否立即开启弹窗
                if (check != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    return false;
                }
            }
        }
        return true;
    }

    //检查权限结果数组，返回true代表都获得权限，false代表不都获取权限
    public static boolean checkGrant(int[] grantResults) {
        if (grantResults != null) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
            return true;

        }

        return false;
    }
}
