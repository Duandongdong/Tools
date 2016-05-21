package com.bestgood.commons.thirdparty.amap;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.LatLng;

/**
 * 地图上显示一个Marker需要的信息
 * 
 * @author ddc
 * @date: Jun 18, 2014 1:29:44 AM
 */
public interface MarkerInfoInterface<T> {
	public int getIndex();

	/**
	 * 返回POI的经纬度坐标
	 * 
	 * @return
	 */
	public LatLng getPosition();

	/**
	 * 返回POI的名称。
	 * 
	 * @return
	 */
	public java.lang.String getTitle();

	/**
	 * 返回POI的地址。
	 * 
	 * @return
	 */
	public java.lang.String getSnippet();

	/**
	 * 获取Marker选未获取焦点时的BitmapDescriptor
	 * 
	 * @return
	 */
	public BitmapDescriptor getIconNormal();

	/**
	 * 获取Marker选中获取焦点时的BitmapDescriptor
	 * 
	 * @return
	 */
	public BitmapDescriptor getIconFocused();

	/**
	 * Marker的附加对象。
	 * 
	 * @return
	 */
	public T getItem();
}
