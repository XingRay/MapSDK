package com.ray.lib_map.entity;

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

    private Anchor anchor;
    private int iconResId;
    private String title;
    private String subTitle;

    private Object rawMarker;

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

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public void setAnchor(float x, float y) {
        this.anchor = new Anchor(x, y);
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
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

    public Object getRawMarker() {
        return rawMarker;
    }

    public void setRawMarker(Object rawMarker) {
        this.rawMarker = rawMarker;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", anchor=" + anchor +
                ", iconResId=" + iconResId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", rawMarker=" + rawMarker +
                '}';
    }
}
