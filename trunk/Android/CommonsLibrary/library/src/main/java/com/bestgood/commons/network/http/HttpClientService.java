package com.bestgood.commons.network.http;

import android.app.Application;

import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
//import com.octo.android.robospice.persistence.googlehttpclient.json.GsonObjectPersisterFactory;
//import com.octo.android.robospice.persistence.googlehttpclient.json.Jackson2ObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

/**
 * Http client service
 *
 * @author ddc
 * @date: Apr 20, 2014 7:34:21 PM
 */
public class HttpClientService extends AbsHttpClientService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // init
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);
        InFileBitmapObjectPersister inFileBitmapObjectPersister = new InFileBitmapObjectPersister(application);

        cacheManager.addPersister(inFileStringObjectPersister);
        cacheManager.addPersister(inFileBitmapObjectPersister);

//        cacheManager.addPersister(new GsonObjectPersisterFactory(application));
        return cacheManager;
    }

    @Override
    public int getThreadCount() {
        return super.getThreadCount();
    }
}
