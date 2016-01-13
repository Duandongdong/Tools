package com.baigu.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baigu.ui.R;


/**
 * 自定义加载数据 ProgressDialog
 *
 * @author ddc
 * @date: Jul 3, 2014 12:08:49 AM
 */
public class LoadingDialog extends Dialog {
    private String mMessage;
    private TextView mMessageTextView;

    public LoadingDialog(Context context) {
        super(context);
        init();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_dialog_loading);

        mMessageTextView = (TextView) findViewById(R.id.txt_message);

        if (!TextUtils.isEmpty(mMessage)) {
            mMessageTextView.setText(mMessage);
        }

        View closeView = findViewById(R.id.img_close);
        closeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }


    public void setMessage(String message) {
        this.mMessage = message;
        if (!TextUtils.isEmpty(mMessage) && mMessageTextView != null) {
            mMessageTextView.setText(mMessage);
        }
    }

    public static LoadingDialog createDialog(Context context) {
        LoadingDialog d = new LoadingDialog(context, R.style.LoadingDialog);
        d.getWindow().getAttributes().gravity = Gravity.CENTER;
        return d;
    }

    public static LoadingDialog show(Context context) {
        LoadingDialog d = new LoadingDialog(context, R.style.AlertDialog);
        d.getWindow().getAttributes().gravity = Gravity.CENTER;
        d.show();
        return d;
    }
}
