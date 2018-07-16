package com.ray.lib_map.entity;

/**
 * @author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapOverlay {
    private MapPoint mapPoint;

    private double x;
    private double y;

    private String title;
    private String subTitle;

    private boolean isClickable;

    public MapPoint getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Override
    public String toString() {
        return "MapOverlay{" +
                "mapPoint=" + mapPoint +
                ", x=" + x +
                ", y=" + y +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", isClickable=" + isClickable +
                '}';
    }
}
