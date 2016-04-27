package com.bestgood.commons.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

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
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        //Logger.d("onNext:%s", aBoolean);
                        if (aBoolean) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                wrap.phoneNumber = tm.getLine1Number();
                                //Logger.i("wrap.phoneNumber = %s", wrap.phoneNumber);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //Logger.e(throwable, "onError");
                        countDownLatch.countDown();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        //Logger.d("completed");
                        countDownLatch.countDown();
                    }
                });

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
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Logger.d("onNext:%s", aBoolean);
                        if (aBoolean) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                wrap.imei = tm.getDeviceId();
                                Logger.i("wrap.imei = %s", wrap.imei);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(throwable, "onError");
                        countDownLatch.countDown();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Logger.d("completed");
                        countDownLatch.countDown();
                    }
                });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Logger.e(e, "countDownLatch.await()");
        }

        return wrap.imei;
    }
}
