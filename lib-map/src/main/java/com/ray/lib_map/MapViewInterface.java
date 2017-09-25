package com.ray.lib_map;

import android.view.View;

import com.ray.lib_map.entity.MapPoint;


/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图控件接口
 */

public interface MapViewInterface {
    /**
     * 地图加载监听器
     */
    interface MapLoadListener {
        /**
         * 地图加载完成时回调
         *
         */
        void onMapLoaded();
    }

    /**
     * 镜头操作监听器
     */
    interface CameraMoveListener {
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

    /**
     * 动画监听器
     */
    interface AnimationListener {
        /**
         * 动画结束时回调
         */
        void onFinished();

        /**
         * 当动画取消时回调
         */
        void onCanceled();
    }

    /**
     * 覆盖物点击监听器
     */
    interface MarkerClickListener {
        /**
         * 当用户点击覆盖物时回调
         *
         * @param marker   覆盖物
         * @param mapPoint 点击的坐标点
         * @return 是否消费点击事件
         */
        boolean onMarkClick(Object marker, MapPoint mapPoint);
    }

    /**
     * 信息窗口点击监听器
     */
    interface InfoWindowClickListener {
        /**
         * 信息窗口被点击时回调
         *
         * @param infoWindow 被点击的信息窗口
         * @param mapPoint   点击的位置
         */
        void onInfoWindowClick(Object infoWindow, MapPoint mapPoint);
    }

    /**
     * 地图截屏监听器
     */
    interface MapScreenCaptureListener {
        /**
         * 截图成功并且保存到文件后回调
         *
         * @param path
         */
        void onScreenCaptured(String path);

        /**
         * 截图失败或者保存失败时回调
         */
        void onFailure();
    }

    /**
     * 地图缩放改变监听
     */
    interface ZoomChangedListener{

        /**
         * 缩放级别改变回调
         * @param newLevel 当前地图缩放级别
         * @param isScaleLevelChanged   相对于改变之前是否具有地域级别的改变
         */
        void onZoomChanged(ScaleLevel newLevel, boolean isScaleLevelChanged);
    }

}