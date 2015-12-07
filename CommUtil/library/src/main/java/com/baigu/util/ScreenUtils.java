package com.baigu.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.baigu.util.log.Logger;

/**
 * ScreenUtils
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * <li>{@link ScreenUtils#dpToPx(Context, float)}</li>
 * <li>{@link ScreenUtils#pxToDp(Context, float)}</li>
 * </ul>
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }


    /**
     * 获得屏幕的像素
     *
     * @param context
     * @return
     */
    public static int[] getScreenPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        int screenWidth = dm.widthPixels;
        // 窗口高度
        int screenHeight = dm.heightPixels;

        int pixels[] = new int[2];
        pixels[0] = screenWidth;
        pixels[1] = screenHeight;

//        Logger.d("screenWidth = %s", screenWidth);
//        Logger.d("screenHeight = %s", screenHeight);

        return pixels;
    }


    /**
     * 获取 屏幕 宽度 像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidthPixels(Context context) {
        int pixels[] = getScreenPixels(context);
        return pixels[0];
    }

    /**
     * 获取屏幕 高度 像素
     *
     * @param activity
     * @return
     */
    public static int getScreenHeightPixels(Activity activity) {
        int pixels[] = getScreenPixels(activity);
        return pixels[1];
    }


    /**
     * 获取ActionBar 高度
     * http://stackoverflow.com/questions/12301510/how-to-get-the-
     * actionbar-height
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
        }
//        else if (context.getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
//        }
        return actionBarHeight;
    }


    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    public static float pxToDpCeilInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    /**
     * 将dp值转换为对应的px值
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2px(Context context, int dpValue) {
        // 计算公式:1dp*像素密度/160 = 实际像素数
        // pixs =dips * (densityDpi/160).
        // dips=(pixs*160)/densityDpi

        float scale = context.getResources().getDisplayMetrics().densityDpi;

        return dpValue * scale / 160;
    }
}
