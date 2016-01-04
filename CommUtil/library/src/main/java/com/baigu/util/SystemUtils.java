package com.baigu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.baigu.util.log.Logger;

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
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
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
        String IMEI = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return IMEI;
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
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        if (number == null) {
            return "";
        }
        return number;
    }
    /**
     * 获取软件当前的版本号-
     *
     * @param context
     * @return
     */
    public static String getSoftver(Context context) {
        String ver = "0";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            String version = info.versionName;
            @SuppressWarnings("unused")
            String versioncode = String.format(Locale.CHINA, "%03d",
                    info.versionCode);
            // 是否需要 versionName 和 versionCode 两个值组合
            ver = version /* + "." + versioncode */;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e, "get getSoftver() exception :");
        } catch (Exception e) {
            Logger.e(e, "get getSoftver() exception :");
        }
        return ver;
    }

}
