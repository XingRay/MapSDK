package com.ray.lib_map.listener;

import android.graphics.Bitmap;

/**
 * Author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图截屏监听器
 */

public interface MapScreenCaptureListener {
    /**
     * 截图成功并且保存到文件后回调
     *
     * @param bitmap 截图
     */
    void onScreenCaptured(Bitmap bitmap);

    /**
     * 截图失败或者保存失败时回调
     */
    void onFailure(int errorCode, String desc);
}
