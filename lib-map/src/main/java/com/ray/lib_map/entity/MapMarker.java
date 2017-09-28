package com.ray.lib_map.entity;

import android.graphics.Bitmap;

import com.ray.lib_map.extern.MapType;

import java.util.HashMap;
import java.util.Map;

/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 点标记
 * 点标记用来在地图上标记任何位置，例如用户位置、车辆位置、店铺位置等一切带有位置属性的事物。
 * 地图 SDK 提供的点标记功能包含两大部分，一部分是点（俗称 Marker）、另一部分是浮于点上方的信息窗体（俗称 InfoWindow）。
 */

public class MapMarker {
    /**
     * sdk中的标记对象
     */
    private final Map<MapType, Object> rawMarkers;
    /**
     * 地图点的位置
     */
    private MapPoint mapPoint;
    /**
     * 锚点X坐标
     */
    private float anchorX;
    /**
     * 锚点Y坐标
     */
    private float anchorY;
    /**
     * 图标
     */
    private Bitmap icon;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subTitle;

    public MapMarker() {
        rawMarkers = new HashMap<>();
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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Object getRawMarker(MapType mapType) {
        return rawMarkers.get(mapType);
    }

    public Object setRawMarker(MapType mapType, Object rawMarker) {
        return rawMarkers.put(mapType, rawMarker);
    }

    public Object removeRawMarker(MapType mapType) {
        return rawMarkers.remove(mapType);
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "mapPoint=" + mapPoint +
                ", anchorX=" + anchorX +
                ", anchorY=" + anchorY +
                ", icon=" + icon +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", rawMarkers=" + rawMarkers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapMarker mapMarker = (MapMarker) o;

        if (Float.compare(mapMarker.anchorX, anchorX) != 0) return false;
        if (Float.compare(mapMarker.anchorY, anchorY) != 0) return false;
        if (mapPoint != null ? !mapPoint.equals(mapMarker.mapPoint) : mapMarker.mapPoint != null)
            return false;
        if (icon != null ? !icon.equals(mapMarker.icon) : mapMarker.icon != null) return false;
        if (title != null ? !title.equals(mapMarker.title) : mapMarker.title != null) return false;
        return subTitle != null ? subTitle.equals(mapMarker.subTitle) : mapMarker.subTitle == null;

    }

    @Override
    public int hashCode() {
        int result = mapPoint != null ? mapPoint.hashCode() : 0;
        result = 31 * result + (anchorX != +0.0f ? Float.floatToIntBits(anchorX) : 0);
        result = 31 * result + (anchorY != +0.0f ? Float.floatToIntBits(anchorY) : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (subTitle != null ? subTitle.hashCode() : 0);
        return result;
    }
}
