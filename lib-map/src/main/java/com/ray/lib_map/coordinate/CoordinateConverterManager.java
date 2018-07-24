package com.ray.lib_map.coordinate;

/**
 * @author : leixing
 * @date : 2018/7/24 10:42
 * <p>
 * description : xxx
 */
public class CoordinateConverterManager {
    private static volatile CoordinateConverterManager INSTANCE;

    public static CoordinateConverterManager getInstance() {
        if (INSTANCE == null) {
            synchronized (CoordinateConverterManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CoordinateConverterManager();
                }
            }
        }
        return INSTANCE;
    }

    private CoordinateConverterManager() {

    }

    public CoordinateConverterManager addConverter(CoordinateConverter converter) {
        return this;
    }

    public Coordinate convert(String fromType, String toType, Coordinate coordinate) {
        return new Coordinate(0, 0);
    }
}
