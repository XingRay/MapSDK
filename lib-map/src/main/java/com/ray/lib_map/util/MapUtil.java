package com.ray.lib_map.util;

import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.CoordinateType;

/**
 * Author      : leixing
 * Date        : 2017-09-30
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapUtil {
    private static double GAUSS_SPHERE = 6378137.0;

    public static double getDistance(MapPoint point1, MapPoint point2) {
        if (point1 == null || point2 == null) {
            throw new IllegalArgumentException("point can not be null");
        }

        MapPoint p1 = point1.copy(CoordinateType.WGS84);
        MapPoint p2 = point2.copy(CoordinateType.WGS84);
        return distance(p1.getLongitude(), p1.getLatitude(), p2.getLongitude(), p2.getLatitude());

    }

    /**
     * @param longitude1 经度
     * @param latitude1  纬度
     * @param longitude2 经度
     * @param latitude2  纬度
     * @return 两点距离 单位 米
     */
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
}
