package com.bestgood.commons.thirdparty.amap;

import com.bestgood.commons.util.log.Logger;

import java.util.Observable;

/**
 * 位置变化Observable
 *
 * @author ddc
 * @date: Jun 14, 2014 2:11:12 PM
 */
public class LocationObservable extends Observable {

    private MyLocationInfo myLocationInfo;

    public LocationObservable() {
    }

    public void notifyLocationChanged(MyLocationInfo location) {
        Logger.v("isChange() = %s", isChanged(location));
        Logger.v("countObservers() = %s", countObservers());

        if (!isChanged(location)) {
            return;
        }

        myLocationInfo = location;
        setChanged();
        notifyObservers(myLocationInfo);
    }

    private boolean isChanged(MyLocationInfo location) {
        if (location == null) {// 当前监听到的位置信息变成null，当作没有变化
            return false;
        }
        return !location.equals(myLocationInfo);
    }
}
