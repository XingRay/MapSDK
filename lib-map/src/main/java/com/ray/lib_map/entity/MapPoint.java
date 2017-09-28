package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ray.lib_map.extern.CoordinateConverter;
import com.ray.lib_map.extern.CoordinateType;

import java.util.HashMap;
import java.util.Map;

/**
 * Author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图上的点
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class MapPoint implements Parcelable {
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
    private final Map<CoordinateType, Coordinate> coordinates;
    /**
     * 坐标系类型
     */
    private CoordinateType type;

    public MapPoint(double latitude, double longitude, CoordinateType type) {
        coordinates = new HashMap<>();
        this.type = type;
        coordinates.put(this.type, new Coordinate(latitude, longitude));
    }

    protected MapPoint(Parcel in) {
        int coordinatesSize = in.readInt();
        this.coordinates = new HashMap<>(coordinatesSize);
        for (int i = 0; i < coordinatesSize; i++) {
            int tmpKey = in.readInt();
            CoordinateType key = tmpKey == -1 ? null : CoordinateType.values()[tmpKey];
            Coordinate value = in.readParcelable(Coordinate.class.getClassLoader());
            this.coordinates.put(key, value);
        }
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CoordinateType.values()[tmpType];
    }

    public double getLatitude() {
        return coordinates.get(type).getLatitude();
    }

    public double getLongitude() {
        return coordinates.get(type).getLongitude();
    }

    public CoordinateType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPoint mapPoint = (MapPoint) o;

        return coordinates.equals(mapPoint.coordinates) && type == mapPoint.type;
    }

    @Override
    public int hashCode() {
        int result = coordinates.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public MapPoint asDefault() {
        return as(CoordinateType.WGS84);
    }

    public MapPoint as(CoordinateType type) {
        if (type != this.type) {
            Coordinate coordinate = coordinates.get(type);
            if (coordinate == null) {
                coordinate = CoordinateConverter.convert(coordinates.get(this.type), this.type, type);
                coordinates.put(type, coordinate);
            }
            this.type = type;
        }
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.coordinates.size());
        for (Map.Entry<CoordinateType, Coordinate> entry : this.coordinates.entrySet()) {
            dest.writeInt(entry.getKey() == null ? -1 : entry.getKey().ordinal());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }
}
