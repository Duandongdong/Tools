
package com.bestgood.commons.network.http;

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

    @Override
    public void onRequestSuccess(RESULT result) {
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
    }

    @Override
    public void onRequestProgressUpdate(RequestProgress progress) {
    }

}
