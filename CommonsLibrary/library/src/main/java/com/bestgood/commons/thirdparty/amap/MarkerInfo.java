package com.bestgood.commons.thirdparty.amap;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;

/**
 * Marker 的信息
 *
 * @author ddc
 * @date: Jun 15, 2014 3:09:18 PM
 */
public class MarkerInfo<T> {
    private final MarkerOptions mMarkerOptions;

    /**
     * Marker选中获取焦点时的BitmapDescriptor
     **/
    private BitmapDescriptor mIconFocused;

    private int mIndex;
    private T mItem;

    public MarkerInfo(MarkerInfoInterface<T> infos) {
        mIndex = infos.getIndex();
        mItem = infos.getItem();

        mMarkerOptions = new MarkerOptions();
        position(infos.getPosition());
        title(infos.getTitle());
        snippet(infos.getSnippet());
        icon(infos.getIconNormal());
        iconFocused(infos.getIconFocused());
        anchor(0.5f, 0.5f);
    }

    public MarkerOptions buildMarkerOptions(boolean isFocus) {
        return mMarkerOptions.icon(isFocus ? (getIconFocused() == null ? getIcon() : getIconFocused()) : getIcon());
    }

    /**
     * 定义marker 图标的锚点。
     *
     * @param u
     * @param v
     * @return
     */
    public MarkerInfo<T> anchor(float u, float v) {
        mMarkerOptions.anchor(u, v);
        return this;
    }

    ;

    /**
     * 设置标记是否可拖动
     *
     * @param draggable
     * @return
     */
    public MarkerInfo<T> draggable(boolean draggable) {
        mMarkerOptions.draggable(draggable);
        return this;
    }

    ;

    /**
     * 返回锚点在水平范围的比例，是一个0 到1 之间的float 数值。
     *
     * @return
     */
    public float getAnchorU() {
        return mMarkerOptions.getAnchorU();
    }

    ;

    /**
     * 返回锚点垂直范围的比例，是一个0 到1 之间的float 数值。
     *
     * @return
     */
    public float getAnchorV() {
        return mMarkerOptions.getAnchorV();
    }

    ;

    /**
     * 返回MMarkerOptions 对象的自定义图标。
     *
     * @return
     */
    public BitmapDescriptor getIcon() {
        return mMarkerOptions.getIcon();
    }

    ;

    /**
     * 返回当前MMarkerOptions 对象所设置的经纬度。
     *
     * @return
     */
    public LatLng getPosition() {
        return mMarkerOptions.getPosition();
    }

    ;

    /**
     * 返回当前MMarkerOptions 对象所设置的文字片段。
     *
     * @return
     */
    public java.lang.String getSnippet() {
        return mMarkerOptions.getSnippet();
    }

    ;

    /**
     * 返回当前MMarkerOptions对象所设置的标题
     *
     * @return
     */
    public java.lang.String getTitle() {
        return mMarkerOptions.getTitle();
    }

    ;

    public MarkerInfo<T> icon(BitmapDescriptor icon) {
        mMarkerOptions.icon(icon);
        return this;
    }

    ;

    /**
     * 获取MMarkerOptions对象的拖拽状态。
     *
     * @return
     */
    public boolean isDraggable() {
        return mMarkerOptions.isDraggable();
    }

    ;

    /**
     * 返回当前MMarkerOptions的可视属性设置
     *
     * @return
     */
    public boolean isVisible() {
        return mMarkerOptions.isVisible();
    }

    ;

    public MarkerInfo<T> position(LatLng position) {
        mMarkerOptions.position(position);
        return this;
    }

    ;

    /**
     * 设置 Marker 上的 snippet
     *
     * @param snippet
     * @return
     */
    public MarkerInfo<T> snippet(java.lang.String snippet) {
        mMarkerOptions.snippet(snippet);
        return this;
    }

    ;

    /**
     * 设置 Marker 的标题
     *
     * @param title
     * @return
     */
    public MarkerInfo<T> title(java.lang.String title) {
        mMarkerOptions.title(title);
        return this;
    }

    ;

    /**
     * 设置Marker是否可见
     *
     * @param visible
     * @return
     */
    public MarkerInfo<T> visible(boolean visible) {
        mMarkerOptions.visible(visible);
        return this;
    }

    /**
     * 获取Marker选中获取焦点时的BitmapDescriptor
     *
     * @return
     */
    public BitmapDescriptor getIconFocused() {
        return mIconFocused;
    }

    /**
     * 设置Marker选中获取焦点时的BitmapDescriptor
     *
     * @param iconFocused
     */
    public MarkerInfo<T> iconFocused(BitmapDescriptor iconFocused) {
        this.mIconFocused = iconFocused;
        return this;
    }

    public T getItem() {
        return mItem;
    }

    public int getIndex() {
        return mIndex;
    }
}
