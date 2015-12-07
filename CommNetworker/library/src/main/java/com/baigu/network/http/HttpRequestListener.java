
package com.baigu.network.http;

import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgressListener;

/**
 * 请求服务器 监听
 *
 * @param <RESULT>
 * @author ddc
 * @date: Jun 15, 2014 1:03:08 AM
 */
public interface HttpRequestListener<RESULT extends HttpClientResponse> extends
        RequestListener<RESULT>, RequestProgressListener {
}
