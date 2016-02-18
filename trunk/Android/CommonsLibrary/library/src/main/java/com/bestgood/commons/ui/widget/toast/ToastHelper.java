package com.bestgood.commons.ui.widget.toast;

import android.content.Context;


/**
 * Toast 提示
 *
 * @author ddc
 * @date: Jul 2, 2014 10:12:19 PM
 */
public class ToastHelper {
    public static void show(Context context, CharSequence str) {
        SuperToast.cancelAllSuperToasts();

        SuperToast toast = new SuperToast(context);
        toast.setDuration(SuperToast.Duration.VERY_SHORT);
        toast.setText(str);
        toast.show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId));
    }
}
