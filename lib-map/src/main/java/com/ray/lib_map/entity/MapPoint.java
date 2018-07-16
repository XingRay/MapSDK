package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ray.lib_map.extern.CoordinateConverter;
import com.ray.lib_map.extern.CoordinateType;
import com.ray.lib_map.extern.MapType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 地图上的点
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class MapPoint implements Parcelable {
    private final Map<CoordinateType, Coordinate> coordinates;
    private CoordinateType type;
    private Coordinate coordinate;

    public MapPoint(double latitude, double longitude, CoordinateType type) {
        this.type = type;
        this.coordinate = new Coordinate(latitude, longitude);

        coordinates = new HashMap<>(CoordinateType.values().length);
        coordinates.put(this.type, this.coordinate);
    }

    protected MapPoint(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CoordinateType.values()[tmpType];
        this.coordinate = in.readParcelable(Coordinate.class.getClassLoader());
        int coordinatesSize = in.readInt();
        this.coordinates = new HashMap<>(coordinatesSize);
        for (int i = 0; i < coordinatesSize; i++) {
            int tmpKey = in.readInt();
            CoordinateType key = tmpKey == -1 ? null : CoordinateType.values()[tmpKey];
            Coordinate value = in.readParcelable(Coordinate.class.getClassLoader());
            this.coordinates.put(key, value);
        }
    }

    public double getLatitude() {
        return this.coordinate.getLatitude();
    }

    public double getLongitude() {
        return this.coordinate.getLongitude();
    }

    public CoordinateType getType() {
        return type;
    }

    public MapPoint asDefault() {
        return as(CoordinateType.WGS84);
    }

    public MapPoint as(CoordinateType type) {
        if (type != this.type) {
            this.coordinate = coordinates.get(type);
            if (this.coordinate == null) {
                this.coordinate = CoordinateConverter.convert(coordinates.get(this.type), this.type, type);
                coordinates.put(type, this.coordinate);
            }
            this.type = type;
        }
        return this;
    }

    public MapPoint copy() {
        MapPoint mapPoint = new MapPoint(coordinate.getLatitude(), coordinate.getLongitude(), type);
        for (Map.Entry<CoordinateType, Coordinate> entry : coordinates.entrySet()) {
            mapPoint.coordinates.put(entry.getKey(), entry.getValue());
        }
        return mapPoint;
    }

    public MapPoint copy(CoordinateType type) {
        return copy().as(type);
    }

    public MapPoint copy(MapType mapType) {
        return copy(mapType.getCoordinateType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPoint mapPoint = (MapPoint) o;

        return type == mapPoint.type && coordinate.equals(mapPoint.coordinate);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + coordinate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "type=" + type +
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
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.coordinate, flags);
        dest.writeInt(this.coordinates.size());
        for (Map.Entry<CoordinateType, Coordinate> entry : this.coordinates.entrySet()) {
            dest.writeInt(entry.getKey() == null ? -1 : entry.getKey().ordinal());
            dest.writeParcelable(entry.getValue(), flags);
        }
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