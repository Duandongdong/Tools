package com.bestgood.commons.sample.ui.ptr.refresh.app;

import android.app.Application;
import android.os.Environment;

import com.bestgood.commons.sample.BuildConfig;
import com.bestgood.commons.sample.ui.ptr.loadmore.request.DemoRequestProxy;
import com.bestgood.commons.sample.ui.ptr.loadmore.utils.DemoEnv;
import com.bestgood.commons.sample.ui.ptr.refresh.image.DemoDuiTangImageReSizer;
import com.bestgood.commons.ui.ptr.PtrFrameLayout;

import java.io.File;

import in.srain.cube.Cube;
import in.srain.cube.cache.CacheManagerFactory;
import in.srain.cube.diskcache.lru.SimpleDiskLruCache;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.request.RequestManager;
import in.srain.cube.util.CLog;
import in.srain.cube.util.CubeDebug;

public class PtrDemoApplication extends Application {

    public static PtrDemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        boolean isProd = !BuildConfig.DEBUG;
        DemoEnv.setIsProd(isProd);

        // init log level
        if (DemoEnv.isProd()) {
            CLog.setLogLevel(CLog.LEVEL_ERROR);
        } else {
            // development
            CLog.setLogLevel(CLog.LEVEL_VERBOSE);
        }


        SimpleDiskLruCache.DEBUG = true;
        CubeDebug.DEBUG_LIFE_CYCLE = true;
        CubeDebug.DEBUG_CACHE = true;
        CubeDebug.DEBUG_IMAGE = true;
        CubeDebug.DEBUG_REQUEST = true;
        PtrFrameLayout.DEBUG = true;


        Cube.onCreate(this);

        initImageLoader();

        initRequestCache();

        RequestManager.getInstance().setRequestProxyFactory(DemoRequestProxy.getInstance());
        // init local cache, just use default
        CacheManagerFactory.initDefaultCache(this, null, -1, -1);
    }


    private void initImageLoader() {

        File path1 = Environment.getExternalStoragePublicDirectory("cube/test1/a/b/c");
        ImageLoaderFactory.customizeCache(
                this,
                // memory size
                1024 * 10,
                // disk cache directory
                // path1.getAbsolutePath(),
                null,
                // disk cache size
                ImageLoaderFactory.DEFAULT_FILE_CACHE_SIZE_IN_KB
        );

        DefaultImageLoadHandler handler = new DefaultImageLoadHandler(this);
        // handler.setLoadingImageColor("#999999");

        ImageLoaderFactory.setDefaultImageLoadHandler(handler);
        ImageLoaderFactory.setDefaultImageReSizer(DemoDuiTangImageReSizer.getInstance());


//        ImageLoaderFactory.setDefaultImageLoadHandler(new PtrImageLoadHandler());
//        ImageLoaderFactory.setDefaultImageReSizer(DemoDuiTangImageReSizer.getInstance());
    }

    private void initRequestCache() {
        String dir = "request-cache";
        RequestCacheManager.init(this, dir, 1024 * 10, 1024 * 10);
    }

}