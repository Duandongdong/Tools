package com.baigu.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dengdingchun on 15/12/8.
 * Service
 */
public class ServiceUtils {

    /**
     * 判断服务是否在运行
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 停止
     * @param context
     * @param serviceClass
     */
    public static void stopServiceRunning(Context context, Class<?> serviceClass) {
        context.stopService(new Intent(context, serviceClass));
    }
}
