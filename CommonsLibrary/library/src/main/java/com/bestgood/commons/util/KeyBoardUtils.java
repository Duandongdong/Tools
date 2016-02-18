package com.bestgood.commons.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 *
 * @author hebiwen
 */
public class KeyBoardUtils {

    private KeyBoardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param token
     */
    public static void hideSoftPad(Context context, IBinder token) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param token
     */
    public static void hideSoftInput(Context context, IBinder token) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     * @return
     */
    public static final boolean hideSoftPad(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            return ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

}
