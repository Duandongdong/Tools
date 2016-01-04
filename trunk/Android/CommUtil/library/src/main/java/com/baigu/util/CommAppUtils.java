package com.baigu.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * AppUtils
 * <ul>
 * <li>{@link CommAppUtils#isNamedProcess(Context, String)}</li>
 * </ul>
 */
public class CommAppUtils {

    private CommAppUtils() {
        throw new AssertionError();
    }

    /**
     * whether this process is named with processName
     *
     * @param context
     * @param processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (ListUtils.isEmpty(processInfoList)) {
            return false;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null
                    && processInfo.pid == pid
                    && ObjectUtils.isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 接口请求失败信息
     *
     * @param e
     * @return
     */
    public static final String buildErrorMsg(Exception e) {
        return "请求失败!" + (e == null ? "" : e.getMessage());
    }

    /**
     * 判断是否有对应的intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 判断是否安装对应程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 拨号
     *
     * @param context
     * @param mobile
     */
    public static void call(Context context, String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return;
        }
        // 叫出拨号程序
        // Uri uri = Uri.parse("tel:" + mobile);
        // Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        // context.startActivity(intent);

        // 直接打电话出去 需要添加打电话权限：
        // <uses-permission android:name="android.permission.CALL_PHONE" />
        Uri uri = Uri.parse("tel:" + mobile);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(intent);
    }


}
