package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图上的点
 */

public class MapPoint implements Parcelable {
    /**
     * 纬度
     */
    private final double latitude;
    /**
     * 经度
     */
    private final double longitude;

    public MapPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPoint mapPoint = (MapPoint) o;

        if (Double.compare(mapPoint.latitude, latitude) != 0) return false;
        return Double.compare(mapPoint.longitude, longitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected MapPoint(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<MapPoint> CREATOR = new Creator<MapPoint>() {
        @Override
        public MapPoint createFromParcel(Parcel source) {
            return new MapPoint(source);
        }

        @Override
        public MapPoint[] newArray(int size) {
            return new MapPoint[size];
        }
    };

    public MapPoint copy() {
        return new MapPoint(latitude, longitude);
    }
}
