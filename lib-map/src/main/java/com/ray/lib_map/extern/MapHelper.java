package com.ray.lib_map.extern;

import android.content.Context;

import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.util.BitmapUtil;

/**
 * Author      : leixing
 * Date        : 2017/7/16 0:19
 * Email       : leixing1012@gmail.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapHelper {
    private MapHelper() {
        throw new UnsupportedOperationException();
    }

    public static MapMarker buildLocationMarker(Address address, Context context, int resId) {
        MapMarker mapMarker = new MapMarker();
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        mapMarker.setIcon(BitmapUtil.fromResource(context, resId));
        mapMarker.setTitle(address.getName());
        mapMarker.setSubTitle(address.getFormattedAddress());
        mapMarker.setLatitude(address.getLatitude());
        mapMarker.setLongitude(address.getLongitude());

        return mapMarker;
    }

    public static MapMarker buildLocationMarker(Poi poi, Context context, int resId) {
        MapMarker mapMarker = new MapMarker();
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        mapMarker.setIcon(BitmapUtil.fromResource(context, resId));
        mapMarker.setTitle(poi.getName());
        mapMarker.setSubTitle(poi.getAddress());
        mapMarker.setLatitude(poi.getLatitude());
        mapMarker.setLongitude(poi.getLongitude());

        return mapMarker;
    }
}
