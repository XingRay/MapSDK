package com.ray.lib_map.listener;

import com.ray.lib_map.entity.MapMarker;

/**
 * @author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 信息窗口点击监听器
 */

public interface InfoWindowClickListener {
    /**
     * 信息窗口被点击时回调
     */
    void onInfoWindowClick(MapMarker marker);
}
