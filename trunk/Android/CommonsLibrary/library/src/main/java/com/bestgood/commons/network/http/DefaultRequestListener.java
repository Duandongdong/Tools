
package com.bestgood.commons.network.http;

import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestProgress;

/**
 * HttpRequestListener 接口空实现，api请求只需覆盖实现 需要处理的方法
 *
 * @param <RESULT>
 * @author ddc
 * @date: Jun 15, 2014 1:15:59 AM
 */
public class DefaultRequestListener<RESULT extends AbsHttpResponse> implements HttpRequestListener<RESULT> {
    /*package*/ Context mContext;

    /**
     * 取消请求时，如果需要取消后台网络请求，需要设置{@link #mManager} 和 {@link #mRequest}
     **/
    private HttpRequestManager mManager;
    private AbsHttpRequest mRequest;

    @Override
    public void onRequestSuccess(RESULT result) {
        if (result.isAccessTokenExpire()) {
            mManager.refreshAccessToken(mRequest, this);
        } else if (result.isAccessTokenInvalid() || result.isRefreshTokenExpire() || result.isRefreshTokenInvalid()) {
            mManager.reLogin(mRequest);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
    }

    @Override
    public void onRequestProgressUpdate(RequestProgress progress) {
    }

    public HttpRequestManager getManager() {
        return mManager;
    }

    public void setManager(HttpRequestManager manager) {
        this.mManager = manager;
    }

    public AbsHttpRequest getRequest() {
        return mRequest;
    }

    public void setRequest(AbsHttpRequest request) {
        this.mRequest = request;
    }
}
