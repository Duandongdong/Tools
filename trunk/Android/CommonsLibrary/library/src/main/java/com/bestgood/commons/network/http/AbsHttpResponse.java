
package com.bestgood.commons.network.http;

import android.content.Context;

import java.net.HttpURLConnection;

/**
 * @author ddc
 * @date Jun 2, 2014 4:24:09 PM
 */
public abstract class AbsHttpResponse {

    /**
     * http 请求 Status code.
     */
    protected int mStatusCode;

    /**
     * http 请求 Status message or {@code null}.
     */
    protected String mStatusMessage;

    /**
     * 服务器返回的字符串格式，在不需要将返回内容简析为对象时，会以字符串格式存储于mResultStr
     **/
    private String mResultStr;

    protected void setStatusCode(int statusCode) {
        this.mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    protected void setStatusMessage(String statusMessage) {
        this.mStatusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }

    public String getResultStr() {
        return mResultStr;
    }

    public void setResultStr(String resultStr) {
        this.mResultStr = resultStr;
    }

    /**
     * @return Returns whether the given HTTP response status code is a success code >= 200 and < 300.
     */
    public boolean isSuccessStatusCode() {
        return mStatusCode >= HttpURLConnection.HTTP_OK && mStatusCode < HttpURLConnection.HTTP_MULT_CHOICE;

    }

    public abstract boolean isSuccess();

    /**
     * 获取服务端返回的响应信息
     *
     * @return 服务端返回的响应信息
     */
    public abstract String getMessage();
    //==============================================================================================

    /**
     * access_token过期,重新获取
     *
     * @return
     */
    protected abstract boolean isAccessTokenExpire();

    /**
     * access_token失效,重新登录
     *
     * @return
     */
    protected abstract boolean isAccessTokenInvalid();

    /**
     * refresh_token过期,重新登录
     *
     * @return
     */
    protected abstract boolean isRefreshTokenExpire();

    /**
     * refresh_token失效,重新登录
     *
     * @return
     */
    protected abstract boolean isRefreshTokenInvalid();

    /**
     * 保存token
     *
     * @param context
     */
    public abstract void saveToken(Context context);
}
