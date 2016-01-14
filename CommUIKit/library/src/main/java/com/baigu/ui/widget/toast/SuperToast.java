
package com.baigu.ui.widget.toast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baigu.ui.R;


/**
 * SuperToasts are designed to replace stock Android Toasts. If you need to
 * display a SuperToast inside of an Activity please see
 * {@link SuperActivityToast}.
 */
public class SuperToast {
    private static final String TAG = "SuperToast";
    private static final String ERROR_CONTEXTNULL = " - You cannot use a null context.";
    private static final String ERROR_DURATIONTOOLONG = " - You should NEVER specify a duration greater than "
            + "four and a half seconds for a SuperToast.";

    /**
     * Durations for all types of SuperToasts.
     */
    public static class Duration {
        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);
    }

    private int mDuration = Duration.SHORT;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param context {@link Context}
     */
    public SuperToast(Context context) {
        if (context == null) {
            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);
        }
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mToastView = layoutInflater.inflate(R.layout.layout_toast, null);
        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mMessageTextView = (TextView) mToastView.findViewById(R.id.txt_toast);
    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than this
     * one will be added to a queue and shown when the previous {@value #TAG} is
     * dismissed.
     */
    public void show() {
        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = mWindowManager.getDefaultDisplay().getWidth() / 2;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = Gravity.CENTER;

        ManagerSuperToast.getInstance().add(this);
    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {
        mMessageTextView.setText(text);
    }

    /**
     * Returns the message text of the {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {
        return mMessageTextView.getText();
    }

    /**
     * Sets the duration that the {@value #TAG} will show.
     *
     * @param duration {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     */
    public void setDuration(int duration) {
        if (duration > Duration.EXTRA_LONG) {
            Log.e(TAG, TAG + ERROR_DURATIONTOOLONG);
            this.mDuration = Duration.EXTRA_LONG;
        } else {
            this.mDuration = duration;
        }
    }

    /**
     * Returns the duration of the {@value #TAG}.
     *
     * @return int
     */
    public int getDuration() {
        return this.mDuration;
    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {
        return mToastView != null && mToastView.isShown();
    }

    /**
     * Returns a standard {@value #TAG}.
     *
     * @param context {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger
     *            {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     * @return {@link SuperToast}
     */
    public static SuperToast create(Context context, CharSequence textCharSequence,
            int durationInteger) {
        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        return superToast;

    }

    /**
     * Dismisses and removes all showing/pending {@value #TAG}.
     */
    public static void cancelAllSuperToasts() {
        ManagerSuperToast.getInstance().cancelAllSuperToasts();
    }

    /**
     * Returns the window manager that the {@value #TAG} is attached to.
     *
     * @return {@link WindowManager}
     */
    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    /**
     * Returns the window manager layout params of the {@value #TAG}.
     *
     * @return {@link WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {
        return mWindowManagerParams;
    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link View}
     */
    public View getView() {
        return mToastView;
    }
}
