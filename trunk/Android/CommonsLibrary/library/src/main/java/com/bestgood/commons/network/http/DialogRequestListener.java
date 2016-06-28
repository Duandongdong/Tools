package com.bestgood.commons.network.http;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;

import com.bestgood.commons.R;
import com.bestgood.commons.ui.widget.LoadingDialog;
import com.bestgood.commons.ui.widget.toast.ToastHelper;
import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestProgress;

/**
 * DefaultRequestListener 子类，请求服务器时显示 一个NoFrameProgressDialog
 *
 * @author ddc
 * @date: Jun 15, 2014 1:31:23 AM
 */
public class DialogRequestListener<RESULT extends AbsHttpResponse> extends DefaultRequestListener<RESULT> implements OnCancelListener, OnDismissListener {

    private LoadingDialog mDialog;

    /**
     * 是否取消
     **/
    public boolean isCanceled = false;

    /**
     * 是否显示加载进度框
     **/
    public boolean isShowLoadingDialog = true;

    private String mDialogMessage;

    public DialogRequestListener(Context context) {
        this(context, true);
    }

    public DialogRequestListener(Context context, boolean isShowLoadingDialog) {
        this(context, isShowLoadingDialog, "");
    }

    public DialogRequestListener(Context context, boolean isShowLoadingDialog, String message) {
        this.mContext = context;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.mDialogMessage = message;
    }

    @Override
    public void onRequestSuccess(RESULT result) {
        super.onRequestSuccess(result);
        dismissDialog();
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        super.onRequestFailure(spiceException);
        dismissDialog();

        if (spiceException instanceof NoNetworkException) {
            ToastHelper.show(mContext, R.string.exception_no_network);
        } else if (spiceException instanceof NetworkException) {
            ToastHelper.show(mContext, R.string.exception_network);
        } else {
            ToastHelper.show(mContext, R.string.exception_server);
        }
    }

    @Override
    public void onRequestProgressUpdate(RequestProgress progress) {
        super.onRequestProgressUpdate(progress);
        if (isShowLoadingDialog && !isCanceled && (mDialog == null || !mDialog.isShowing())) {
            mDialog = LoadingDialog.show(mContext);
            mDialog.setOnCancelListener(this);
            mDialog.setOnDismissListener(this);

            if (!TextUtils.isEmpty(mDialogMessage)) {
                mDialog.setMessage(mDialogMessage);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        isCanceled = true;
        if (getManager() != null && getRequest() != null) {
            getManager().cancel(getRequest());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isCanceled = true;
    }


    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
