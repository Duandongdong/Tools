package com.bestgood.commons.util.config;


import com.bestgood.commons.BuildConfig;

/**
 * Created by dengdingchun on 15/11/25.
 * 应用配置信息
 */
public class AppConfig {
    public static final int APPLICATION_SAMPLE_UTIL = 100;
    public static final int APPLICATION_SAMPLE_UIKIT = 200;
    public static final int APPLICATION_SAMPLE_NETWORKER = 300;
    public static final int APPLICATION_SAMPLE_DATA_PERSIST = 400;
    public static final int APPLICATION_SAMPLE_BASE = 500;
    public static final int APPLICATION_SAMPLE_THIRD_PARTY_INTEGRATED = 600;

    public static final int APPLICATION_CZGJ = 1;
    public static final int APPLICATION_CZGJ_BIZ = 2;
    public static final int APPLICATION_PARK = 3;
    public static final int APPLICATION_PARK_CONSUMER = 4;

    public static boolean isCzgj() {
        return BuildConfig.applicationCode == APPLICATION_CZGJ;
    }

    public static boolean isCzgjBiz() {
        return BuildConfig.applicationCode == APPLICATION_CZGJ_BIZ;
    }


    public static boolean isPark() {
        return BuildConfig.applicationCode == APPLICATION_PARK;
    }

    public static boolean isParkConsumer() {
        return BuildConfig.applicationCode == APPLICATION_PARK_CONSUMER;
    }

    public static boolean isRequestEncrypt() {
        return false;
    }

    public static boolean isResponseDecrypt() {
        return isCzgj() || isCzgjBiz();
    }
}
