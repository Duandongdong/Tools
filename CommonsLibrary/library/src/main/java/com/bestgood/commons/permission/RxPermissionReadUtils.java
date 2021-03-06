package com.bestgood.commons.permission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bestgood.commons.util.log.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.CountDownLatch;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by JIADONG on 2016/4/22.
 */
public class RxPermissionReadUtils {

    /**
     * 读取用户手机号码，同步返回（弹框向用户获取权限/耗时）
     *
     * @param context
     * @return
     */
    public static String readPhoneNumber(final Context context) {
        final ReadPermissionValueWrap wrap = new ReadPermissionValueWrap();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        RxPermissions
                .getInstance(context)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(
                        new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                //Logger.d("onNext:%s", granted);
                                if (granted) {
                                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                    wrap.phoneNumber = tm.getLine1Number();
                                    //Logger.i("wrap.phoneNumber = %s", wrap.phoneNumber);
                                }
                                countDownLatch.countDown();
                            }
                        }
                );

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Logger.e(e, "countDownLatch.await()");
        }

        return wrap.phoneNumber;
    }


    /**
     * @param context
     * @return
     */
    public static String readIMEI(final Context context) {
        final ReadPermissionValueWrap wrap = new ReadPermissionValueWrap();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        RxPermissions
                .getInstance(context)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(
                        new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                //Logger.d("onNext:%s", granted);
                                if (granted) {
                                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                    wrap.imei = tm.getDeviceId();
                                    //Logger.i("wrap.imei = %s", wrap.imei);
                                }
                                countDownLatch.countDown();
                            }
                        }
//                        , new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable throwable) {
//                                Logger.e(throwable, "onError");
//                                countDownLatch.countDown();
//                            }
//                        }, new Action0() {
//                            @Override
//                            public void call() {
//                                Logger.d("completed");
//                                countDownLatch.countDown();
//                            }
//                        }
                );

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Logger.e(e, "countDownLatch.await()");
        }

        return wrap.imei;
    }

    public static void requestPermissionsDialog(final Context context, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(message) ? "当前应用缺少必要权限。" : message);
        sb.append("\n");
        sb.append("请点击“设置”-“权限”-打开所需权限。");
        sb.append("\n");
        sb.append("最后点击两次后退按钮，即可返回。");

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(sb.toString())
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    // 启动应用的设置
    private static void startAppSettings(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }
}
