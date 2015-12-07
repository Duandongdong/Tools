package com.baigu.ui.sample.ptr.loadmore.utils;

public class DemoEnv {

    private static boolean sIsProd = true;

    public static boolean isProd() {
        return sIsProd;
    }

    public static void setIsProd(boolean isProd) {
        sIsProd = isProd;
    }
}
