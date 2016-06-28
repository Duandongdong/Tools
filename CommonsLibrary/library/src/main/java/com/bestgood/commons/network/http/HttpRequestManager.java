
package com.bestgood.commons.network.http;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;

import java.lang.ref.WeakReference;

/**
 * RequestManager
 *
 * @author ddc
 * @date Jun 15, 2014 1:49:20 AM
 */
public class HttpRequestManager /*extends SpiceManager*/ {


    /**
     * The contextWeakReference used to bind to the service from.
     */
    private WeakReference<Context> contextWeakReference;

    private SpiceManager mSpiceManager;

    public HttpRequestManager() {
        mSpiceManager = new SpiceManager(HttpClientService.class);
    }

    public synchronized void start(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
        mSpiceManager.start(getContextReference());
    }

    public synchronized void shouldStop() {
        mSpiceManager.shouldStop();
        this.contextWeakReference.clear();
    }

    public void cancel(AbsHttpRequest request) {
        mSpiceManager.cancel(request);
    }

    public void execute(AbsHttpRequest request, DefaultRequestListener requestListener) {
        requestListener.setManager(this);
        requestListener.setRequest(request);
        if (checkAccessToken(request)) {
            mSpiceManager.execute(request, requestListener);
        } else {
            refreshAccessToken(request, requestListener);
        }
    }

    private Context getContextReference() {
        return contextWeakReference.get();
    }

    /**
     * 检测本地保存的access_token是否有效
     *
     * @return
     */
    private boolean checkAccessToken(AbsHttpRequest request) {
        return request.checkAccessToken(getContextReference());
    }

    /**
     * 检测本地保存的refresh_token是否有效
     *
     * @return
     */
    private boolean checkRefreshToken(AbsHttpRequest request) {
        return request.checkRefreshToken(getContextReference());
    }

    /**
     * 重新获取access_token
     *
     * @param request
     */
    /*package*/ void refreshAccessToken(AbsHttpRequest request, final DefaultRequestListener requestListener) {
        if (checkRefreshToken(request)) {//重新获取access_token和refresh_token
            request.refreshToken(getContextReference(), this, request, requestListener);
        } else {//重新登录
            reLogin(request);
        }
    }

    /*package*/ void reLogin(AbsHttpRequest request) {
        request.reLogin(getContextReference());
    }
}