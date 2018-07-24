package com.leixing.lib_map_amap;

import android.content.Context;

import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.base.Converter;
import com.ray.lib_map.base.Factory;
import com.ray.lib_map.coordinate.Coordinate;
import com.ray.lib_map.coordinate.CoordinateConverter;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.extern.MapConfig;

/**
 * @author : leixing
 * @date : 2018/7/17 16:00
 * <p>
 * description : xxx
 */
public class GaodeMap {
    @SuppressWarnings("WeakerAccess")
    public static final String COORDINATE_GCJ02 = "gcj02";
    public static final String COORDINATE_WGS84 = "wgs84";

    /**
     * Krasovsky 1940
     * <p>
     * a = 6378245.0, 1/f = 298.3
     * b = a * (1 - f)
     * ee = (a^2 - b^2) / a^2;
     */
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;
    private static final double PI = 3.1415926535897932384626;

    private static MapConfig DEFAULT;

    public static MapConfig getDefault() {
        return DEFAULT;
    }

    public static void init(final Context context) {
        DEFAULT = new MapConfig()
                .setCoordinateType(COORDINATE_GCJ02)
                .setDelegateFactory(new Factory<MapDelegate>() {
                    @Override
                    public MapDelegate create() {
                        return new GaodeMapDelegate(context, DEFAULT);
                    }
                })
                .setDataSourceFactory(new Factory<MapDataSource>() {
                    @Override
                    public MapDataSource create() {
                        return new GaodeDataSource(context, DEFAULT.getCoordinateType());
                    }
                }).addConverter(new CoordinateConverter(COORDINATE_GCJ02, COORDINATE_WGS84, new Converter<Coordinate, Coordinate>() {
                    @Override
                    public Coordinate convert(Coordinate coordinate) {
                        Coordinate tmp = wgs84ToGcj02(coordinate);

                        double dLat = tmp.getLatitude() - coordinate.getLatitude();
                        double dLon = tmp.getLongitude() - coordinate.getLongitude();

                        double latitude = coordinate.getLatitude() - dLat;
                        double longitude = coordinate.getLongitude() - dLon;

                        return new Coordinate(latitude, longitude);
                    }
                })).addConverter(new CoordinateConverter(COORDINATE_WGS84, COORDINATE_GCJ02, new Converter<Coordinate, Coordinate>() {
                    @Override
                    public Coordinate convert(Coordinate coordinate) {
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
                }));
    }

    private static Coordinate wgs84ToGcj02(Coordinate coordinate) {
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
