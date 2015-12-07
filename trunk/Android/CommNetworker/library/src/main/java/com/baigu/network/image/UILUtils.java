package com.baigu.network.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.baigu.util.BuildConfig;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by dengdingchun on 15/10/24.
 * <p>
 * https://github.com/nostra13/Android-Universal-Image-Loader
 * <p>
 * 初始化/配置工具类
 */
public class UILUtils {

    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(320, 480) // default = device screen dimensions
                .diskCacheExtraOptions(320, 480, null)
//                .taskExecutor(...)
//                .taskExecutorForCachedImages(...)
//                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
//                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)// 50 MiB
                .diskCacheFileCount(100)
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .defaultDisplayImageOptions(defaultDisplayImageOptions().build());

        if (BuildConfig.DEBUG) {
            config.writeDebugLogs();
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions.Builder defaultDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .decodingOptions(...)
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()); // default
    }
}
