package com.ray.lib_map.extern;

import android.content.Context;
import android.os.Bundle;

import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.impl.baidu.BaiduMapDelegate;
import com.ray.lib_map.impl.gaode.GaodeMapDelegate;
import com.ray.lib_map.impl.google.GoogleMapDelegate;

/**
 * Author      : leixing
 * Date        : 2017-09-22
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public enum MapType {
    GAODE {
        @Override
        public MapDelegate createMapDelegate(Context context) {
            return new GaodeMapDelegate(context);
        }

        @Override
        public void onSwitchOut(MapDelegate mapDelegate) {
            //高德地图在切出时必须要调用onDestroy，否则再次切换回高德地图时会显示空白
            mapDelegate.onPause();
            mapDelegate.onDestroy();
        }

        @Override
        public void onSwitchIn(MapDelegate mapDelegate, Bundle savedInstanceState) {
            mapDelegate.onCreate(savedInstanceState);
            mapDelegate.onResume();
        }
    },

    BAIDU {
        @Override
        public MapDelegate createMapDelegate(Context context) {
            return new BaiduMapDelegate(context);
        }

        @Override
        public void onSwitchOut(MapDelegate mapDelegate) {
            //百度地图在切出时不能调用onDestroy，否则会在onCreate时因为使用了已经回收的bitmap而崩溃
            mapDelegate.onPause();
        }

        @Override
        public void onSwitchIn(MapDelegate mapDelegate, Bundle savedInstanceState) {
            mapDelegate.onCreate(savedInstanceState);
            mapDelegate.onResume();
        }
    },

    GOOGLE {
        @Override
        public MapDelegate createMapDelegate(Context context) {
            return new GoogleMapDelegate(context);
        }

        @Override
        public void onSwitchOut(MapDelegate mapDelegate) {
            //谷歌地图在切出时，onPause和onDestroy可以调用，也可以不调用
            mapDelegate.onPause();
            mapDelegate.onDestroy();
        }

        @Override
        public void onSwitchIn(MapDelegate mapDelegate, Bundle savedInstanceState) {
            mapDelegate.onCreate(savedInstanceState);
            mapDelegate.onResume();
        }
    };

    public abstract MapDelegate createMapDelegate(Context context);

    /**
     * 当地图被切换出页面时调用
     *
     * @param mapDelegate 地图代理
     */
    public abstract void onSwitchOut(MapDelegate mapDelegate);

    /**
     * 当地图切换进地图时调用
     *
     * @param mapDelegate        地图代理
     * @param savedInstanceState 保存状态
     */
    public abstract void onSwitchIn(MapDelegate mapDelegate, Bundle savedInstanceState);
}
