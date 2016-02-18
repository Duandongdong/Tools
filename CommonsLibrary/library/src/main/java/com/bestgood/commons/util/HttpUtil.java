package com.bestgood.commons.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtil {
    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean IsNetWorkAvailble(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null;
    }
}
