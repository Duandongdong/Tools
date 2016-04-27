
package com.bestgood.commons.network.http;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * RequestManager
 *
 * @author ddc
 * @date: Jun 15, 2014 1:49:20 AM
 */
public class HttpRequestManager extends SpiceManager {

    public HttpRequestManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
    }
}
