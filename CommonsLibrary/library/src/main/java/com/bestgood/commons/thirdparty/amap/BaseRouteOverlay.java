package com.bestgood.commons.thirdparty.amap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.bestgood.commons.util.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengdingchun on 16/3/27.
 */
public class BaseRouteOverlay {
    protected List<Marker> stationMarkers = new ArrayList<>();
    protected List<Polyline> allPolyLines = new ArrayList<>();
    private Marker mStartMarker;
    private Marker mEndMarker;
    protected LatLng startPoint;
    protected LatLng endPoint;
    protected AMap mAMap;
    private Context mContext;
    private Bitmap mStartBitmap;
    private Bitmap mEndBitmap;
    private Bitmap mBusBitmap;
    private Bitmap mManBitmap;
    private Bitmap mCarBitmap;
    private AssetManager mAssetManager;
    protected boolean mNodeIconVisible = true;

    public BaseRouteOverlay(Context context) {
        this.mContext = context;
        this.mAssetManager = this.mContext.getResources().getAssets();
    }

    public void removeFromMap() {
        if (this.mStartMarker != null) {
            this.mStartMarker.remove();
        }

        if (this.mEndMarker != null) {
            this.mEndMarker.remove();
        }

        for (Marker marker : this.stationMarkers) {
            marker.remove();
        }

        for (Polyline polyline : this.allPolyLines) {
            polyline.remove();
        }

        this.recycle();
    }

    public void setNodeIconVisibility(boolean nodeIconVisible) {
        this.mNodeIconVisible = nodeIconVisible;
        for (Marker marker : this.stationMarkers) {
            marker.setVisible(nodeIconVisible);
        }
        this.mAMap.postInvalidate();
    }

    private void recycle() {
        if (this.mStartBitmap != null) {
            this.mStartBitmap.recycle();
            this.mStartBitmap = null;
        }

        if (this.mEndBitmap != null) {
            this.mEndBitmap.recycle();
            this.mEndBitmap = null;
        }

        if (this.mBusBitmap != null) {
            this.mBusBitmap.recycle();
            this.mBusBitmap = null;
        }

        if (this.mManBitmap != null) {
            this.mManBitmap.recycle();
            this.mManBitmap = null;
        }

        if (this.mCarBitmap != null) {
            this.mCarBitmap.recycle();
            this.mCarBitmap = null;
        }

    }

    protected BitmapDescriptor getStartBitmapDescriptor() {
        return this.getBitDes(this.mStartBitmap, "amap_start.png");
    }

    protected BitmapDescriptor getEndBitmapDescriptor() {
        return this.getBitDes(this.mEndBitmap, "amap_end.png");
    }

    protected BitmapDescriptor getBusBitmapDescriptor() {
        return this.getBitDes(this.mBusBitmap, "amap_bus.png");
    }

    protected BitmapDescriptor getWalkBitmapDescriptor() {
        return this.getBitDes(this.mManBitmap, "amap_man.png");
    }

    protected BitmapDescriptor getDriveBitmapDescriptor() {
        return this.getBitDes(this.mCarBitmap, "amap_car.png");
    }

    protected BitmapDescriptor getBitDes(Bitmap bitmap, String iconStr) {
        String des = "getBitDes";
        try {
            InputStream is = this.mAssetManager.open(iconStr);
            bitmap = BitmapFactory.decodeStream(is);
            //bitmap = mStartMarker.a(bitmap, 0.9F);
            is.close();
        } catch (IOException e) {
            Logger.e(e, "RouteOverlay");
        } catch (Exception e) {
            Logger.e(e, "RouteOverlay", des);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    protected void addStartAndEndMarker() {
        this.mStartMarker = this.mAMap.addMarker((new MarkerOptions()).position(this.startPoint).icon(this.getStartBitmapDescriptor()).title("起点"));
        this.mEndMarker = this.mAMap.addMarker((new MarkerOptions()).position(this.endPoint).icon(this.getEndBitmapDescriptor()).title("终点"));
    }

    public void zoomToSpan() {
        if (this.startPoint != null && this.endPoint != null) {
            if (this.mAMap == null) {
                return;
            }
            LatLngBounds latLngBounds = this.getLatLngBounds();
            this.mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        }
    }

    protected LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(new LatLng(this.startPoint.latitude, this.startPoint.longitude));
        builder.include(new LatLng(this.endPoint.latitude, this.endPoint.longitude));
        return builder.build();
    }

    protected int getWalkColor() {
        return Color.parseColor("#6db74d");
    }

    protected int getBusColor() {
        return Color.parseColor("#537edc");
    }


    protected float getDriveLineWidth() {
        return 18.0F;
    }


}
