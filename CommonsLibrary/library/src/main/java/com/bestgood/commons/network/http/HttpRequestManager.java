
package com.bestgood.commons.network.http;

import android.content.Context;

import com.bestgood.commons.util.ListUtils;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestProgress;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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

    private List<RequestCache> mRequestCacheList;

    public HttpRequestManager() {
        mSpiceManager = new SpiceManager(HttpClientService.class);
    }

    public synchronized void start(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        mSpiceManager.start(getContextReference());
    }

    public synchronized void shouldStop() {
        mSpiceManager.shouldStop();
        this.contextWeakReference.clear();
    }

    public void cancel(AbsHttpRequest request) {
        mSpiceManager.cancel(request);
    }

    public <RESULT extends AbsHttpResponse> void execute(AbsHttpRequest<RESULT> request, DefaultRequestListener<RESULT> requestListener) {
        if (getContextReference() != null) {
            if (checkAccessToken(request)) {
                mSpiceManager.execute(request, new RequestManagerListener<>(request, requestListener));
            } else {
                refreshAccessToken(request, requestListener);
            }
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
     * @param requestListener
     */
    private synchronized <RESULT extends AbsHttpResponse> void refreshAccessToken(AbsHttpRequest<RESULT> request, final DefaultRequestListener<RESULT> requestListener) {
        if (checkRefreshToken(request)) {//重新获取access_token和refresh_token
            if (mRequestCacheList == null) {
                mRequestCacheList = new ArrayList<>();
            }
            mRequestCacheList.add(new RequestCache(request, requestListener));

            if (mRequestCacheList.size() == 1) {
                execute(request.createRefreshTokenRequest(), new DefaultRequestListener<RESULT>() {
                    @Override
                    public void onRequestSuccess(RESULT result) {
                        super.onRequestSuccess(result);
                        if (result.isSuccess()) {
                            if (getContextReference() != null) {
                                result.saveToken(getContextReference());
                                refreshTokenSuccess();
                            }
                        } else {
                            refreshTokenFailure(new SpiceException(result.getMessage()));
                        }
                    }

                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        super.onRequestFailure(spiceException);
                        refreshTokenFailure(spiceException);
                    }
                });
            }
        } else {//重新登录
            reLogin(request);
        }
    }

    private synchronized void refreshTokenSuccess() {
        if (!ListUtils.isEmpty(mRequestCacheList)) {
            for (RequestCache request : mRequestCacheList) {
                if (request == null) {
                    continue;
                }
                execute(request.request, request.listener);
            }
            mRequestCacheList = null;
        }
    }

    private synchronized void refreshTokenFailure(SpiceException spiceException) {
        if (!ListUtils.isEmpty(mRequestCacheList)) {
            for (RequestCache request : mRequestCacheList) {
                if (request == null) {
                    continue;
                }
                if (request.listener != null) {
                    request.listener.onRequestFailure(spiceException);
                }
            }
            mRequestCacheList = null;
        }
    }

    private void reLogin(AbsHttpRequest request) {
        if (mRequestCacheList != null) {
            mRequestCacheList.clear();
            mRequestCacheList = null;
        }
        if (getContextReference() != null) {
            request.reLogin(getContextReference());
        }
    }

    private final class RequestManagerListener<RESULT extends AbsHttpResponse> extends DefaultRequestListener<RESULT> {
        private AbsHttpRequest<RESULT> mRequest;
        private DefaultRequestListener<RESULT> mListener;

        public RequestManagerListener(AbsHttpRequest<RESULT> request, DefaultRequestListener<RESULT> listener) {
            this.mRequest = request;
            this.mListener = listener;
        }

        @Override
        public void onRequestSuccess(RESULT result) {
            super.onRequestSuccess(result);
            if (result.isAccessTokenExpire()) {
                refreshAccessToken(mRequest, mListener);
            } else if (result.isAccessTokenInvalid()) {
                if (getContextReference() != null) {
                    //if (mRequest.isHeadersAccessTokenDifferent(getContextReference())) {
                    //execute(mRequest, mListener);
                    //} else {
                    if (mListener instanceof DialogRequestListener) {
                        ((DialogRequestListener) mListener).dismissDialog();
                    }
                    reLogin(mRequest);
                    //}
                }
            } else if (result.isRefreshTokenExpire() || result.isRefreshTokenInvalid()) {
                if (mListener instanceof DialogRequestListener) {
                    ((DialogRequestListener) mListener).dismissDialog();
                }
                reLogin(mRequest);
            } else {
                if (mListener != null) {
                    mListener.onRequestSuccess(result);
                }
            }
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            super.onRequestFailure(spiceException);
            if (mListener != null) {
                mListener.onRequestFailure(spiceException);
            }
        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
            super.onRequestProgressUpdate(progress);
            if (mListener != null) {
                mListener.onRequestProgressUpdate(progress);
            }
        }
    }

    private final class RequestCache {
        AbsHttpRequest request;
        DefaultRequestListener listener;

        public RequestCache(AbsHttpRequest request, DefaultRequestListener listener) {
            this.request = request;
            this.listener = listener;
        }
    }
}

