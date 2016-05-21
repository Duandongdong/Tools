package com.bestgood.commons.thirdparty.amap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;

/**
 * 管理 Marker
 * 
 * @author ddc
 * @date: Jun 15, 2014 3:09:29 PM
 */
public class MarkerManager<T> {
	// private PoiOverlay mPoiOverlay;

	private AMap aMap;

	/** 保存增加到map中的Marker **/
	private Set<Marker> mMarkerSet;
	/** 当前获取焦点，显示InfoWindow 的 Marker **/
	public  Marker mFocusMarker;

	public MarkerManager(AMap amap) {
		this.aMap = amap;
		this.mMarkerSet = new HashSet<Marker>();
	}

	private void addMarker(MarkerInfo<T> markerInfo) {
		if (markerInfo == null) {
			return;
		}
		addMarker2Map(markerInfo, false);
	}

	public void addMarker(List<MarkerInfo<T>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			MarkerInfo<T> info = list.get(i);
			if (info == null) {
				continue;
			}
			addMarker(info);
		}
		zoomToSpan();
	}

	public void addMarkerNoSpan(List<MarkerInfo<T>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			MarkerInfo<T> info = list.get(i);
			if (info == null) {
				continue;
			}
			addMarker(info);
		}
	}

	/**
	 * 去掉PoiOverlay上所有的Marker。
	 * 
	 * @param markerInfo
	 */
	public void remove(MarkerInfo<T> markerInfo) {
		Marker marker = findMarker(markerInfo);
		if (marker != null) {
			mMarkerSet.remove(marker);
			marker.remove();
		}
	}

	/**
     *
     */
	public void removeFromMap() {
	
		for (Marker marker : mMarkerSet) {
			if (marker == null) {
				continue;
			}
			marker.remove();
		}
		mMarkerSet.clear();
	}
	
	public Marker showInfoWindow(MarkerInfo<T> markerInfo,
			boolean isShowInfoWindow) {
		Marker marker = findMarker(markerInfo);
		showInfoWindow(marker, isShowInfoWindow);
		return marker;
	}

	public boolean showInfoWindow(Marker marker, boolean isShowInfoWindow) {
		if (marker == null) {
			return true;
		}
		if (marker.equals(mFocusMarker)) {
			return true;
		}
		hideInfoWindow(isShowInfoWindow);

		mFocusMarker = marker;
		if (isShowInfoWindow) {
			mFocusMarker.showInfoWindow();
		}

		try {
			@SuppressWarnings("unchecked")
			BitmapDescriptor bitmapDescriptor = ((MarkerInfo<T>) mFocusMarker
					.getObject()).getIconFocused();
			mFocusMarker.setIcon(bitmapDescriptor);
		} catch (Exception e) {
		}

		CameraPosition position = new CameraPosition.Builder()
				.target(mFocusMarker.getPosition())
				.zoom(aMap.getCameraPosition().zoom)
				.bearing(aMap.getCameraPosition().bearing)
				.tilt(aMap.getCameraPosition().tilt).build();
		aMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
		return true;
	}

	@SuppressWarnings("unchecked")
	public void hideInfoWindow(boolean isShowInfoWindow) {
		if (mFocusMarker == null) {
			return;
		}
		if (isShowInfoWindow) {
			mFocusMarker.hideInfoWindow();
		}
		mFocusMarker.setIcon(((MarkerInfo<T>) mFocusMarker.getObject())
				.getIcon());
		mFocusMarker = null;
		aMap.invalidate();
	}

	/**
	 * 添加一个Options 对应 的 Marker 到 map
	 * 
	 * @param infos
	 * @param isFocus
	 * @return
	 */
	private Marker addMarker2Map(MarkerInfo<T> markerInfo, boolean isFocus) {
		if (markerInfo == null) {
			return null;
		}
		Marker marker = aMap.addMarker(markerInfo.buildMarkerOptions(isFocus));
		marker.setObject(markerInfo);
		mMarkerSet.add(marker);
		return marker;
	}

	/**
	 * @param info
	 * @return
	 */
	public Marker findMarker(MarkerInfo<T> markerInfo) {
		if (markerInfo == null) {
			return null;
		}
		for (Marker marker : mMarkerSet) {
			if (marker == null) {
				continue;
			}
			@SuppressWarnings("unchecked")
			MarkerInfo<T> info = (MarkerInfo<T>) marker.getObject();
			if (markerInfo.equals(info)) {
				return marker;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public MarkerInfo<T> getFocusdItem() {
		if (mFocusMarker == null) {
			return null;
		}
		return (MarkerInfo<T>) mFocusMarker.getObject();
	}

	/**
	 * 设置所有maker 和 Polyline 显示在View中, 移动镜头到当前的视角。
	 */
	public boolean zoomToSpan() {
		List<LatLng> latLngBounds = new ArrayList<LatLng>();
		for (Marker marker : mMarkerSet) {
			if (marker == null) {
				continue;
			}
			latLngBounds.add(marker.getPosition());
		}
		CameraUpdate cameraUpdate = AMapUtil.newCameraUpdate(latLngBounds);
		if (cameraUpdate == null) {
			return false;
		}
		aMap.moveCamera(cameraUpdate);
		return true;
	}
}
