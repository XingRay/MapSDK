package com.ray.lib_map.extern;

import android.content.Context;

import com.ray.lib_map.MapHolder;
import com.ray.lib_map.impl.baidu.BaiduMapHolder;
import com.ray.lib_map.impl.gaode.GaodeMapHolder;
import com.ray.lib_map.impl.google.GoogleMapHolder;

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
        public MapHolder createMapHolder(Context context) {
            return new GaodeMapHolder(context);
        }
    },

    BAIDU {
        @Override
        public MapHolder createMapHolder(Context context) {
            return new BaiduMapHolder(context);
        }
    },

    GOOGLE {
        @Override
        public MapHolder createMapHolder(Context context) {
            return new GoogleMapHolder(context);
        }
    };

    public abstract MapHolder createMapHolder(Context context);
}
