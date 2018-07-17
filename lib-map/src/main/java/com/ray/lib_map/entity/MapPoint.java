package com.ray.lib_map.entity;

import com.ray.lib_map.coordinate.Coordinate;
import com.ray.lib_map.extern.MapType;

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
public class MapPoint {
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

    public MapPoint copy(MapType mapType) {
        return copy(mapType.getCoordinateType());
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
}