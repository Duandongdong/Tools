package com.bestgood.commons.thirdparty.amap;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bestgood.commons.R;
import com.bestgood.commons.os.WeakHandler;
import com.bestgood.commons.util.log.Logger;

/**
 * 显示定位用户当前位置marker
 *
 * @author ddc
 * @date: Jun 14, 2014 11:07:59 PM
 */
public class MyLocationMarker implements SensorEventListener {
    private Context mContext;
    private AMap aMap;
    private MyLocationInfo mInfo;

    private float mBearing;
    private long mLastTime = 0;
    // Sensor改变刷新的最小时间间隔
    private final int TIME_SENSOR = 100;

    /**
     * 定位中心点 Marker
     */
    private Marker mMarker;
    /**
     * 精度范围 圆形阴影
     */
    private Circle mAccuracyCircle;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorHandler mSensorHandler;

    public MyLocationMarker(Context context, AMap aMap) {
        this.mContext = context;
        this.aMap = aMap;

        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_GAME);

        mSensorHandler = new SensorHandler(this);
    }

    /**
     * 更新定位信息
     *
     * @param info
     */
    public void update(MyLocationInfo info) {
        Logger.v("update(%s)", info);

        if (info == null || info.aMapLocation == null) {
            return;
        }
        if (info.equals(mInfo) && mMarker != null) {
            return;
        }

        mInfo = info;

        updateAccuracyCircle();
        updateCenterMarker();

        aMap.invalidate();
    }

    /**
     * 更新精度 圆圈
     */
    private void updateAccuracyCircle() {
        Logger.v("updateAccuracyCircle()");

        LatLng center = AMapUtil.getLatLng(mInfo.aMapLocation);
        float radius = mInfo.aMapLocation.getAccuracy();

        if (mAccuracyCircle == null) {
            mAccuracyCircle = aMap
                    .addCircle(new CircleOptions().center(center)
                            .radius(radius)
                            /** Color.BLUE = 0xFF0000FF **/
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.parseColor("#2F0000FF"))
                            .strokeWidth(0.5f));
        } else {
            mAccuracyCircle.setCenter(center);
            mAccuracyCircle.setRadius(radius);
        }
    }

    /**
     * 更新 中心点 Marker
     */
    private void updateCenterMarker() {
        Logger.v("updateCenterMarker()");

        LatLng latlng = AMapUtil.getLatLng(mInfo.aMapLocation);

        float bearing = mBearing;
        CameraPosition camera = aMap.getCameraPosition();
        if (camera != null) {
            bearing -= camera.bearing;// 地图旋转角度
        }

        if (mMarker == null) {
            mMarker = aMap
                    .addMarker(new MarkerOptions()
                            // .title(mInfo.title)
                            // .snippet(mInfo.snippet)
                            .anchor(0.5f, 0.5f)//
                            .position(latlng)//
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_mylocation_marker)));
        } else {
            // boolean isInfoWindowShown = mCenterMarker.isInfoWindowShown();
            // if (isInfoWindowShown) {
            // mCenterMarker.hideInfoWindow();
            // }
            mMarker.setRotateAngle(bearing);
            mMarker.setPosition(latlng);
            // if (isInfoWindowShown) {
            // mCenterMarker.showInfoWindow();
            // }

            // ----------------------------------------------------------------------
            // boolean isInfoWindowShown = mCenterMarker.isInfoWindowShown();
            // Marker marker = aMap.addMarker(new MarkerOptions().title(title)
            // .anchor(0.5f, 0.5f).snippet(snippet).position(latlng)
            // .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            // if (isInfoWindowShown) {
            // marker.showInfoWindow();
            // mCenterMarker.hideInfoWindow();
            // }
            // mCenterMarker.remove();
            // mCenterMarker = marker;
        }
        // mMarker.setObject(mInfo.aMapLocation);
        aMap.invalidate();
    }

    public void hideInfoWindow() {
        if (mMarker != null) {
            mMarker.hideInfoWindow();
        }
    }

    public Marker getCurrentMarker() {
        return mMarker;
    }

    public boolean isInfoWindowShown() {
        if (mMarker == null) {
            return false;
        }
        return mMarker.isInfoWindowShown();
    }

    public void destroy() {
        mSensorManager.unregisterListener(this);
        mMarker.remove();
        mMarker.destroy();
    }

    // android.hardware.SensorEventListener========================================================
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - mLastTime < TIME_SENSOR) {
            return;
        }
        float x = event.values[SensorManager.DATA_X];
        // float y = event.values[SensorManager.DATA_Y];
        // float z = event.values[SensorManager.DATA_Z];
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                if (mInfo != null && Math.abs(mBearing - x) > 1) {
                    mBearing = x;
                    mSensorHandler.removeMessages(0);
                    mSensorHandler.sendEmptyMessage(0);
                    mLastTime = System.currentTimeMillis();
                }
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private static class SensorHandler extends WeakHandler<MyLocationMarker> {
        public SensorHandler(MyLocationMarker owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            MyLocationMarker marker = getOwner();
            if (marker != null) {
                marker.updateCenterMarker();
            }
        }
    }
}
