package com.ray.lib_map.listener;

import com.ray.lib_map.entity.MapPoint;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 镜头操作监听器
 */

public interface CameraMoveListener {
    /**
     * 当用户滑动镜头时回调
     *
     * @param point 滑动过程中镜头中心的坐标点
     */
    void onCameraMoving(MapPoint point);

    /**
     * 当用户滑动镜头结束时回调
     *
     * @param point 滑动结束时镜头中心的坐标点
     */
    void onCameraMoved(MapPoint point);
}
