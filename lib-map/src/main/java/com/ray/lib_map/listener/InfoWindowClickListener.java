package com.ray.lib_map.listener;

import com.ray.lib_map.entity.MapPoint;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 信息窗口点击监听器
 */

public interface InfoWindowClickListener {
    /**
     * 信息窗口被点击时回调
     *
     * @param infoWindow 被点击的信息窗口
     * @param mapPoint   点击的位置
     */
    void onInfoWindowClick(Object infoWindow, MapPoint mapPoint);
}
