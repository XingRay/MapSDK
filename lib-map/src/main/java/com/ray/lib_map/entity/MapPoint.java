package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ray.lib_map.coordinate.Coordinate;
import com.ray.lib_map.extern.MapConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 地图上的点
 * todo Parcelable
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class MapPoint implements Parcelable {
    private static String STANDARD_TYPE = "wgs84";

    public static void setStandardType(String type) {
        STANDARD_TYPE = type;
    }

    public static String getStandardType() {
        return STANDARD_TYPE;
    }

    private final Map<String, Coordinate> coordinates;
    private String coordinateType;
    private Coordinate coordinate;

    public MapPoint(double latitude, double longitude, String coordinateType) {
        this.coordinateType = coordinateType;
        this.coordinate = new Coordinate(latitude, longitude);

        coordinates = new HashMap<>();
        coordinates.put(this.coordinateType, this.coordinate);
    }

    public double getLatitude() {
        return this.coordinate.getLatitude();
    }

    public double getLongitude() {
        return this.coordinate.getLongitude();
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public MapPoint as(String type) {
//        if (type != this.coordinateType) {
//            this.coordinate = coordinates.get(type);
//            if (this.coordinate == null) {
//                this.coordinate = CoordinateConvertUtil.convert(coordinates.get(this.coordinateType), this.coordinateType, type);
//                coordinates.put(type, this.coordinate);
//            }
//            this.coordinateType = type;
//        }
        return this;
    }

    public MapPoint copy() {
        //        for (Map.Entry<CoordinateType, Coordinate> entry : coordinates.entrySet()) {
//            mapPoint.coordinates.put(entry.getKey(), entry.getValue());
//        }
        return new MapPoint(coordinate.getLatitude(), coordinate.getLongitude(), coordinateType);
    }

    public MapPoint copy(String type) {
        return copy().as(type);
    }

    public MapPoint copy(MapConfig mapConfig) {
        return copy(mapConfig.getCoordinateType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MapPoint mapPoint = (MapPoint) o;

        return coordinateType.equals(mapPoint.coordinateType) && coordinate.equals(mapPoint.coordinate);
    }

    @Override
    public int hashCode() {
        int result = coordinateType.hashCode();
        result = 31 * result + coordinate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "coordinateType=" + coordinateType +
                ", coordinate=" + coordinate +
                ", coordinates=" + coordinates +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.coordinates.size());
        for (Map.Entry<String, Coordinate> entry : this.coordinates.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.coordinateType);
        dest.writeParcelable(this.coordinate, flags);
    }

    protected MapPoint(Parcel in) {
        int coordinatesSize = in.readInt();
        this.coordinates = new HashMap<String, Coordinate>(coordinatesSize);
        for (int i = 0; i < coordinatesSize; i++) {
            String key = in.readString();
            Coordinate value = in.readParcelable(Coordinate.class.getClassLoader());
            this.coordinates.put(key, value);
        }
        this.coordinateType = in.readString();
        this.coordinate = in.readParcelable(Coordinate.class.getClassLoader());
    }

    public static final Parcelable.Creator<MapPoint> CREATOR = new Parcelable.Creator<MapPoint>() {
        @Override
        public MapPoint createFromParcel(Parcel source) {
            return new MapPoint(source);
        }

        @Override
        public MapPoint[] newArray(int size) {
            return new MapPoint[size];
        }
    };
}