package com.ray.lib_map.extern;

/**
 * @author      : leixing
 * Date        : 2017-09-22
 * Email       : leixing@qq.com
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
    BAIDU(CoordinateType.BD09),

    /**
     * google地图
     */
    GOOGLE(CoordinateType.GCJ02);

    private final CoordinateType coordinateType;

    MapType(CoordinateType coordinateType) {
        this.coordinateType = coordinateType;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }
}
