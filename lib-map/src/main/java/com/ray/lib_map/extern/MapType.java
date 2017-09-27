package com.ray.lib_map.extern;

import android.content.Context;

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
    },

    BAIDU {
        @Override
        public MapDelegate createMapDelegate(Context context) {
            return new BaiduMapDelegate(context);
        }
    },

    GOOGLE {
        @Override
        public MapDelegate createMapDelegate(Context context) {
            return new GoogleMapDelegate(context);
        }
    };

    public abstract MapDelegate createMapDelegate(Context context);
}
