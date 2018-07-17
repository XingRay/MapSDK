package com.ray.lib_map.entity;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 点标记
 * 点标记用来在地图上标记任何位置，例如用户位置、车辆位置、店铺位置等一切带有位置属性的事物。
 * 地图 SDK 提供的点标记功能包含两大部分，一部分是点（俗称 Marker）、另一部分是浮于点上方的信息窗体（俗称 InfoWindow）。
 */

@SuppressWarnings("unused")
public class MapMarker {
    /**
     * sdk中的标记对象
     */
    private final Map<String, Object> rawMarkers;
    /**
     * 地图点的位置
     */
    private MapPoint mapPoint;

    /**
     * 图标
     */
    private Bitmap icon;

    /**
     * 锚点X坐标
     */
    private float anchorX;
    /**
     * 锚点Y坐标
     */
    private float anchorY;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 垂直轴
     */
    private int zIndex;

    /**
     * 是否可点击
     */
    private boolean clickable;

    /**
     * tag
     */
    private Object tag;

    /**
     * infoWindow是否可见
     */
    private boolean infoWindowVisible;

    public MapMarker() {
        rawMarkers = new HashMap<>();
        this.anchorX = 0.5f;
        this.anchorY = 1.0f;
    }

    public MapMarker(MapPoint mapPoint, Bitmap icon) {
        this();
        this.mapPoint = mapPoint;
        this.icon = icon;
    }

    public MapPoint getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(float anchorX) {
        this.anchorX = anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(float anchorY) {
        this.anchorY = anchorY;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getRawMarker(String mapType) {
        return rawMarkers.get(mapType);
    }

    public void setRawMarker(String mapType, Object rawMarker) {
        rawMarkers.put(mapType, rawMarker);
    }

    public Object removeRawMarker(String mapType) {
        return rawMarkers.remove(mapType);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isInfoWindowVisible() {
        return infoWindowVisible;
    }

    public void setInfoWindowVisible(boolean infoWindowVisible) {
        this.infoWindowVisible = infoWindowVisible;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "rawMarkers=" + rawMarkers +
                ", mapPoint=" + mapPoint +
                ", icon=" + icon +
                ", anchorX=" + anchorX +
                ", anchorY=" + anchorY +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", zIndex=" + zIndex +
                ", clickable=" + clickable +
                ", tag=" + tag +
                ", infoWindowVisible=" + infoWindowVisible +
                '}';
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapMarker mapMarker = (MapMarker) o;

        if (Float.compare(mapMarker.anchorX, anchorX) != 0) return false;
        if (Float.compare(mapMarker.anchorY, anchorY) != 0) return false;
        if (zIndex != mapMarker.zIndex) return false;
        if (clickable != mapMarker.clickable) return false;
        if (infoWindowVisible != mapMarker.infoWindowVisible) return false;
        if (!mapPoint.equals(mapMarker.mapPoint)) return false;
        if (!icon.equals(mapMarker.icon)) return false;
        if (title != null ? !title.equals(mapMarker.title) : mapMarker.title != null) return false;
        return content != null ? content.equals(mapMarker.content) : mapMarker.content == null;

    }

    @Override
    public int hashCode() {
        int result = mapPoint.hashCode();
        result = 31 * result + icon.hashCode();
        result = 31 * result + (anchorX != +0.0f ? Float.floatToIntBits(anchorX) : 0);
        result = 31 * result + (anchorY != +0.0f ? Float.floatToIntBits(anchorY) : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + zIndex;
        result = 31 * result + (clickable ? 1 : 0);
        result = 31 * result + (infoWindowVisible ? 1 : 0);
        return result;
    }

    public void clearRawMarker() {
        rawMarkers.clear();
    }
}
