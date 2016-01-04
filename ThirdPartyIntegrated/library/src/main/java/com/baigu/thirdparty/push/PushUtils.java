package com.baigu.thirdparty.push;

import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * Created by JIADONG on 2016/1/4.
 */
public class PushUtils {
    /**
     * 以apikey的方式绑定 push
     *
     * @param context
     */
    public static void startPushWork(Context context) {
        PushManager.startWork(context.getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(context, "api_key"));
    }

    /**
     * 解除push绑定
     *
     * @param context
     */
    public static void stopPushWork(Context context) {
        PushManager.stopWork(context.getApplicationContext());
    }

}
