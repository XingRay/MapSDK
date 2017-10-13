package com.ray.lib_map.listener;

import com.ray.lib_map.entity.MapPoint;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 覆盖物点击监听器
 */

public interface MarkerClickListener {
    /**
     * 当用户点击覆盖物时回调
     *
     * @param marker   覆盖物
     * @param mapPoint 点击的坐标点
     * @return 是否消费点击事件
     */
    boolean onMarkClick(Object marker, MapPoint mapPoint);
}
