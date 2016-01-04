package com.baigu.util;

import android.content.Context;

/**
 * SizeUtils
 */
public class SizeUtils {

    /**
     * gb to byte
     **/
    public static final long GB_2_BYTE = 1073741824;
    /**
     * mb to byte
     **/
    public static final long MB_2_BYTE = 1048576;
    /**
     * kb to byte
     **/
    public static final long KB_2_BYTE = 1024;

    private SizeUtils() {
        throw new AssertionError();
    }
    /**
     * dp、sp、px之间转化的工具
     *
     * @author molaith
     *
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }
}
