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
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

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

    /**
     * sdk中的标记对象
     */
    private Map<MapType, Object> rawMarkers = new HashMap<>();

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", anchorX=" + anchorX +
                ", anchorY=" + anchorY +
                ", icon=" + icon +
                ", title='" + title + '\'' +
                ", rawMarkers=" + rawMarkers +
                '}';
    }
}
