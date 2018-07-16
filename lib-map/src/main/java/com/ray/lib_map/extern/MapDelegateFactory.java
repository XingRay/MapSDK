package com.ray.lib_map.extern;

import android.content.Context;

import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.impl.baidu.BaiduMapDelegate;
import com.ray.lib_map.impl.gaode.GaodeMapDelegate;
import com.ray.lib_map.impl.google.GoogleMapDelegate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author      : leixing
 * Date        : 2017-09-27
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapDelegateFactory {
    private final Context mContext;
    private Map<MapType, MapDelegate> mMapDelegates;

    public MapDelegateFactory(Context context) {
        mContext = context;
        mMapDelegates = new HashMap<>();
    }

    public MapDelegate createMapDelegate(MapType mapType) {
        if (mapType == null) {
            throw new IllegalArgumentException("mapType can not be null");
        }
        MapDelegate mapDelegate = mMapDelegates.get(mapType);
        if (mapDelegate != null) {
            return mapDelegate;
        }
        switch (mapType) {
            case GAODE:
                mapDelegate = new GaodeMapDelegate(mContext);
                break;

            case BAIDU:
                mapDelegate = new BaiduMapDelegate(mContext);
                break;

            case GOOGLE:
                mapDelegate = new GoogleMapDelegate(mContext);
                break;
        }

        mMapDelegates.put(mapType, mapDelegate);
        return mapDelegate;
    }
}
