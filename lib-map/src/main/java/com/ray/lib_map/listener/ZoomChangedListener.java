package com.ray.lib_map.listener;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图缩放改变监听
 */

public interface ZoomChangedListener {
    /**
     * 缩放级别改变回调
     *
     * @param newLevel            当前地图缩放级别
     * @param isScaleLevelChanged 相对于改变之前是否具有地域级别的改变
     */
    void onZoomChanged(float newLevel, boolean isScaleLevelChanged);
}
