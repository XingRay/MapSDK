package com.ray.lib_map.listener;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 动画监听器
 */

public interface AnimationListener {
    /**
     * 动画结束时回调
     */
    void onFinished();

    /**
     * 当动画取消时回调
     */
    void onCanceled();
}
