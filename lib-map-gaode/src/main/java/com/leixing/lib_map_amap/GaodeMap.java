package com.leixing.lib_map_amap;

import android.content.Context;

import com.ray.lib_map.extern.MapType;

/**
 * @author : leixing
 * @date : 2018/7/17 16:00
 * <p>
 * description : xxx
 */
public class GaodeMap {
    public static final String NAME = "amap";
    public static final String
    private MapType mapType;

    public GaodeMap(Context context) {
        mapType = new MapType(NAME);
        mapType.setCoordinateType("gcj02");
        mapType.setDelegate(new GaodeMapDelegate(context, NAME));
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }
}
