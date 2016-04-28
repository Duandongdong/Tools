package com.bestgood.commons.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bestgood.commons.permission.RxPermissionReadUtils;
import com.bestgood.commons.util.log.Logger;

import java.util.Locale;

/**
 * SystemUtils
 */
public class SystemUtils {

    /**
     * recommend default thread pool size according to system available processors, {@link #getDefaultThreadPoolSize()}
     **/
    public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

    private SystemUtils() {
        throw new AssertionError();
    }

    /**
     * get recommend default thread pool size
     *
     * @return if 2 * availableProcessors + 1 less than 8, return it, else return 8;
     * @see {@link #getDefaultThreadPoolSize(int)} max is 8
     */
    public static int getDefaultThreadPoolSize() {
        return getDefaultThreadPoolSize(8);
    }

    /**
     * get recommend default thread pool size
     *
     * @param max
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }


    /**
     * 获取IMSI号
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            imsi = "000000000000000";
        }
        return imsi;
    }

    /**
     * 获取 IMEI 号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        return RxPermissionReadUtils.readIMEI(context);
    }

    public static int getSDKVer() {
        return Build.VERSION.SDK_INT;
    }

    public static float getBuildVersion() {
        return Float.parseFloat(Build.VERSION.RELEASE);
    }

    /**
     * 获取电话号码
     *
     * @param context
     * @return
     */
    public static final String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        if (number == null) {
            return "";
        }
        return number;
    }


}
