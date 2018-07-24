package com.ray.lib_map.util;

import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.graph.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-09-30
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapUtil {
    private static final int EARTH_RADIUS = 6371000;
    private static double GAUSS_SPHERE = 6378137.0;

    public static double getDistance(MapPoint point1, MapPoint point2) {
        if (point1 == null || point2 == null) {
            throw new IllegalArgumentException("point can not be null");
        }

        MapPoint p1 = point1.copy(MapPoint.getStandardType());
        MapPoint p2 = point2.copy(MapPoint.getStandardType());
        return distance(p1.getLongitude(), p1.getLatitude(), p2.getLongitude(), p2.getLatitude());

    }

    /**
     * @param longitude1 经度
     * @param latitude1  纬度
     * @param longitude2 经度
     * @param latitude2  纬度
     * @return 两点距离 单位 米
     */
    // TODO: 2017-10-24 考虑超过边界的情况
    // TODO: 2017-10-24 通过弧度计算距离
    private static double distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double a = radLat1 - radLat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        return Math.round(s * GAUSS_SPHERE * 10000) / (10000);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static List<MapPoint> getBounds(Circle circle) {
        List<MapPoint> mapPoints = new ArrayList<>();

        MapPoint center = circle.getCenter();
        double radius = circle.getRadius();
        String coordinateType = center.getCoordinateType();

        double deltaAngle = getDeltaAngleInEarth(radius);

        // TODO: 2017-10-24 考虑超过边界的情况
        mapPoints.add(new MapPoint(center.getLatitude() + deltaAngle, center.getLongitude() - deltaAngle, coordinateType));
        mapPoints.add(new MapPoint(center.getLatitude() + deltaAngle, center.getLongitude() + deltaAngle, coordinateType));
        mapPoints.add(new MapPoint(center.getLatitude() - deltaAngle, center.getLongitude() - deltaAngle, coordinateType));
        mapPoints.add(new MapPoint(center.getLatitude() - deltaAngle, center.getLongitude() + deltaAngle, coordinateType));

        return mapPoints;
    }

    private static double getDeltaAngleInEarth(double radius) {
        return 360 * radius / (2 * Math.PI * EARTH_RADIUS);
    }
}
