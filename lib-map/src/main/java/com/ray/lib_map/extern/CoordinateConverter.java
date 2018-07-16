package com.ray.lib_map.extern;

import android.support.annotation.NonNull;

import com.ray.lib_map.entity.Coordinate;

/**
 * @author      : leixing
 * Date        : 2017-09-28
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class CoordinateConverter {
    private static final double PI = 3.1415926535897932384626;

    //
    // Krasovsky 1940
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    private CoordinateConverter() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public static Coordinate convert(@NonNull Coordinate coordinate, @NonNull CoordinateType fromType, @NonNull CoordinateType toType) {
        if (fromType == toType) {
            return coordinate;
        }
        switch (toType) {
            case WGS84:
                return toWGS84(coordinate, fromType);
            case GCJ02:
                return toGCJ02(coordinate, fromType);
            case BD09:
                return toBD09(coordinate, fromType);
        }
        throw new IllegalStateException("unknown coordinate type");
    }

    @NonNull
    private static Coordinate toWGS84(@NonNull Coordinate coordinate, @NonNull CoordinateType fromType) {
        switch (fromType) {
            case GCJ02:
                return GCJ02ToWGS84(coordinate);
            case BD09:
                return BD09ToWGS84(coordinate);
        }
        throw new IllegalStateException("unknown coordinate type");
    }

    @NonNull
    private static Coordinate toGCJ02(@NonNull Coordinate coordinate, @NonNull CoordinateType fromType) {
        switch (fromType) {
            case WGS84:
                return WGS84ToGCJ02(coordinate);
            case BD09:
                return BD09ToGCJ02(coordinate);
        }
        throw new IllegalStateException("unknown coordinate type");
    }

    @NonNull
    private static Coordinate toBD09(@NonNull Coordinate coordinate, @NonNull CoordinateType fromType) {
        switch (fromType) {
            case WGS84:
                return WGS84ToBD09(coordinate);
            case GCJ02:
                return GCJ02ToBD09(coordinate);
        }
        throw new IllegalStateException("unknown coordinate type");
    }

    private static Coordinate GCJ02ToWGS84(Coordinate coordinate) {
        Coordinate tmp = WGS84ToGCJ02(coordinate);

        double dLat = tmp.getLatitude() - coordinate.getLatitude();
        double dLon = tmp.getLongitude() - coordinate.getLongitude();

        return new Coordinate(coordinate.getLatitude() - dLat, coordinate.getLongitude() - dLon);
    }

    private static Coordinate BD09ToWGS84(Coordinate coordinate) {
        return GCJ02ToWGS84(BD09ToGCJ02(coordinate));
    }

    private static Coordinate WGS84ToGCJ02(Coordinate coordinate) {
        double latitude = coordinate.getLatitude();
        double longitude = coordinate.getLongitude();
        double dLatitude = transformLat(longitude - 105.0, latitude - 35.0);
        double dLongitude = transformLon(longitude - 105.0, latitude - 35.0);

        double radLat = latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLatitude = (dLatitude * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLongitude = (dLongitude * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        return new Coordinate(latitude + dLatitude, longitude + dLongitude);
    }

    private static Coordinate BD09ToGCJ02(Coordinate coordinate) {
        double latitude = coordinate.getLatitude();
        double longitude = coordinate.getLongitude();

        double x = longitude - 0.0065;
        double y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);

        double gcjLongitude = z * Math.cos(theta);
        double gcjLatitude = z * Math.sin(theta);

        return new Coordinate(gcjLatitude, gcjLongitude);
    }

    private static Coordinate WGS84ToBD09(Coordinate coordinate) {
        return GCJ02ToBD09(WGS84ToGCJ02(coordinate));
    }

    private static Coordinate GCJ02ToBD09(Coordinate coordinate) {
        double latitude = coordinate.getLatitude();
        double longitude = coordinate.getLongitude();

        double z = Math.sqrt(longitude * longitude + latitude * latitude) + 0.00002 * Math.sin(latitude * PI);
        double theta = Math.atan2(latitude, longitude) + 0.000003 * Math.cos(longitude * PI);

        double bdLatitude = z * Math.sin(theta) + 0.006;
        double bdLongitude = z * Math.cos(theta) + 0.0065;

        return new Coordinate(bdLatitude, bdLongitude);
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
}
