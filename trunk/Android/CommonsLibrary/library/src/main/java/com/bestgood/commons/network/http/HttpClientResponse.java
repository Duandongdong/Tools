
package com.bestgood.commons.network.http;

import com.google.api.client.http.HttpStatusCodes;

/**
 * @author ddc
 * @date Jun 2, 2014 4:24:09 PM
 */
public abstract class HttpClientResponse {

    /** http 请求 Status code. */
    protected int mStatusCode;

    /** http 请求 Status message or {@code null}. */
    protected String mStatusMessage;

    /** 服务器返回的字符串格式，在不需要将返回内容简析为对象时，会以字符串格式存储于mResultStr **/
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

    public boolean isSuccessStatusCode() {
        return HttpStatusCodes.isSuccess(mStatusCode);
    }
}
