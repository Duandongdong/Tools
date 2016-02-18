package com.bestgood.commons.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestgood.commons.R;


/**
 * 通用信息提示框
 *
 * @author ddc
 * @date: Aug 17, 2014 12:04:09 AM
 */
public class AlertDialog extends Dialog {
    public CharSequence mMessage;
    public CharSequence mSubMessage;

    public CharSequence mPositiveButtonText;
    public OnClickListener mPositiveButtonListener;

    public CharSequence mNegativeButtonText;
    public OnClickListener mNegativeButtonListener;

    public AlertDialog(Context context) {
        this(context, R.style.AlertDialog);
    }


    public AlertDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_dialog_alert);

        initView();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    private void initView() {
        TextView messageView = (TextView) findViewById(R.id.txt_message);


        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        messageView.setLayoutParams(new LinearLayout.LayoutParams((int) (dm.widthPixels * 0.8), LinearLayout
                .LayoutParams.MATCH_PARENT));

        if (!TextUtils.isEmpty(mMessage)) {
            messageView.setText(mMessage);
        }
        if (!TextUtils.isEmpty(mSubMessage)) {
            TextView subMessageView = (TextView) findViewById(R.id.txt_sub_message);
            subMessageView.setText(mSubMessage);
            subMessageView.setVisibility(View.VISIBLE);
        }

        Button positive = (Button) findViewById(R.id.btn_positive);
        if (!TextUtils.isEmpty(mPositiveButtonText)) {
            positive.setText(mPositiveButtonText);
        }
        if (mPositiveButtonListener != null) {
            positive.setVisibility(View.VISIBLE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPositiveButtonListener.onClick(AlertDialog.this,
                            DialogInterface.BUTTON_POSITIVE);
                    dismiss();
                }
            });
        }

        Button negative = (Button) findViewById(R.id.btn_negative);
        if (!TextUtils.isEmpty(mNegativeButtonText)) {
            negative.setText(mNegativeButtonText);
        }
        if (mNegativeButtonListener != null) {
            negative.setVisibility(View.VISIBLE);
            negative.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mNegativeButtonListener.onClick(AlertDialog.this,
                            DialogInterface.BUTTON_NEGATIVE);
                    dismiss();
                }
            });
        }
        View divide = findViewById(R.id.divide_line);
        divide.setVisibility(positive.getVisibility() == View.VISIBLE
                && negative.getVisibility() == View.VISIBLE ? View.VISIBLE
                : View.GONE);
    }

    public AlertDialog setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public AlertDialog setSubMessage(String message) {
        this.mSubMessage = message;
        return this;
    }

    public AlertDialog setPositiveButton(OnClickListener listener) {
        setPositiveButton("", listener);
        return this;
    }

    public AlertDialog setPositiveButton(CharSequence text, OnClickListener listener) {
        this.mPositiveButtonText = text;
        this.mPositiveButtonListener = listener;
        return this;
    }

    public AlertDialog setNegativeButton(OnClickListener listener) {
        setNegativeButton("", listener);
        return this;
    }

    public AlertDialog setNegativeButton(CharSequence text, OnClickListener listener) {
        this.mNegativeButtonText = text;
        this.mNegativeButtonListener = listener;
        return this;
    }

    public static AlertDialog createDialog(Context context) {
        return new AlertDialog(context, R.style.AlertDialog);
    }
}
