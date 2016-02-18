
package com.bestgood.commons.network.http;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * RequestManager
 *
 * @author ddc
 * @date: Jun 15, 2014 1:49:20 AM
 */
public class RequestManager extends SpiceManager {

    public RequestManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
    }
}
