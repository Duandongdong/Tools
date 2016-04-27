
package com.bestgood.commons.network.http;

import java.util.Set;

import android.content.Intent;
import android.os.IBinder;

import com.bestgood.commons.util.log.Logger;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * @author ddc
 * @date Apr 20, 2014 7:43:29 PM
 */
public abstract class AbsHttpClientService extends SpiceService {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v("onCreate()");
    }

    @Override
    public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {
        SpiceRequest<?> spiceRequest = request.getSpiceRequest();
        if (spiceRequest instanceof AbsHttpRequest) {
            AbsHttpRequest<?> req = (AbsHttpRequest<?>) spiceRequest;
            req.setContext(this);
            //req.setHttpRequestFactory(createRequestFactory());
        }
        super.addRequest(request, listRequestListener);
    }

    // =============================================================================================================

    @Override
    public int getThreadCount() {
        return 3;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.t(getClass().getSimpleName()).d(intent, "onStart(intent,%d)", startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.t(getClass().getSimpleName()).d(intent, "onStartCommand(intent,%d,%d", flags, startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.t(getClass().getSimpleName()).v("onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onBind(intent)");
        return super.onBind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onRebind(intent)");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onUnbind(intent)");
        return super.onUnbind(intent);
    }


}