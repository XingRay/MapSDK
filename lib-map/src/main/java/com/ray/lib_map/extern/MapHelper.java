package com.ray.lib_map.extern;

import android.content.Context;

import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017/7/16 0:19
 * Email       : leixing1012@gmail.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class MapHelper {
    private MapHelper() {
        throw new UnsupportedOperationException();
    }

    public static MapMarker createMarker(Address address, Context context, int resId) {
        MapMarker mapMarker = new MapMarker(address.getMapPoint(), BitmapUtil.fromResource(context, resId));
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        mapMarker.setTitle(address.getName());
        mapMarker.setContent(address.getFormattedAddress());
        return mapMarker;
    }

    public static MapMarker createMarker(Poi poi, Context context, int resId) {
        MapMarker mapMarker = new MapMarker(poi.getMapPoint(), BitmapUtil.fromResource(context, resId));
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        mapMarker.setTitle(poi.getName());
        mapMarker.setContent(poi.getAddress());
        return mapMarker;
    }

    public static List<MapMarker> createMarkers(List<Poi> pois, Context context, int resId) {
        List<MapMarker> mapMarkers = new ArrayList<>();
        if (pois == null) {
            return mapMarkers;
        }
        for (Poi poi : pois) {
            mapMarkers.add(createMarker(poi, context, resId));
        }
        return mapMarkers;
    }
}
