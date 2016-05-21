package com.bestgood.commons.thirdparty.amap;

import com.amap.api.location.AMapLocation;

/**
 * 用户定位信息
 *
 * @author ddc
 * @date: Jun 14, 2014 8:15:18 PM
 */
public class MyLocationInfo {
    public AMapLocation aMapLocation;
    public String title;
    public String snippet;

    public MyLocationInfo(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public MyLocationInfo(AMapLocation aMapLocation, String title, String snippet) {
        this.aMapLocation = aMapLocation;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "MyLocationInfo [aMapLocation=" + aMapLocation + ", title=" + title + ", snippet=" + snippet + "]";
    }
}
