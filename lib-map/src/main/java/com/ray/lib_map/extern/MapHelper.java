package com.ray.lib_map.extern;

import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.Anchor;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.Poi;

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

    public static MapMarker buildLocationMarker(Address address, int resId) {
        MapMarker mapMarker = new MapMarker();
        mapMarker.setAnchor(new Anchor(0.5f, 0.5f));
        mapMarker.setIconResId(resId);
        mapMarker.setTitle(address.getName());
        mapMarker.setSubTitle(address.getFormattedAddress());
        mapMarker.setLatitude(address.getLatitude());
        mapMarker.setLongitude(address.getLongitude());

        return mapMarker;
    }

    public static MapMarker buildLocationMarker(Poi poi, int resId) {
        MapMarker mapMarker = new MapMarker();
        mapMarker.setAnchor(new Anchor(0.5f, 0.5f));
        mapMarker.setIconResId(resId);
        mapMarker.setTitle(poi.getName());
        mapMarker.setSubTitle(poi.getAddress());
        mapMarker.setLatitude(poi.getLatitude());
        mapMarker.setLongitude(poi.getLongitude());

        return mapMarker;
    }
}
