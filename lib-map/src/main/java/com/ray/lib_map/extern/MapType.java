package com.ray.lib_map.extern;

/**
 * Author      : leixing
 * Date        : 2017-09-22
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public enum MapType {
    /**
     * 高德地图
     */
    GAODE(CoordinateType.GCJ02),

    /**
     * 百度地图
     */
    BAIDU(CoordinateType.GCJ02),

    /**
     * google地图
     */
    GOOGLE(CoordinateType.WGS84);

    private final CoordinateType coordinateType;

    MapType(CoordinateType coordinateType) {
        this.coordinateType = coordinateType;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }
}
