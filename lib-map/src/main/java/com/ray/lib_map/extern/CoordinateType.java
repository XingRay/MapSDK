package com.ray.lib_map.extern;


import com.ray.lib_map.entity.MapPoint;

/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 坐标系的类型
 */

public enum CoordinateType {
    /**
     * 高德使用的坐标系
     */
    GAODE {
        @Override
        public MapPoint toStandard(MapPoint srcPoint, CoordinateType srcType) {
            return null;
        }

        @Override
        public MapPoint fromStandard(MapPoint srcPoint, CoordinateType dstType) {
            return null;
        }
    },

    /**
     * 标准坐标系
     */
    STANDARD {
        @Override
        public MapPoint toStandard(MapPoint srcPoint, CoordinateType srcType) {
            return null;
        }

        @Override
        public MapPoint fromStandard(MapPoint srcPoint, CoordinateType dstType) {
            return null;
        }
    };

    public abstract MapPoint toStandard(MapPoint srcPoint, CoordinateType srcType);

    public abstract MapPoint fromStandard(MapPoint srcPoint, CoordinateType dstType);
}
