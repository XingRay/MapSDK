package com.ray.lib_map.extern;

import android.content.Context;
import android.graphics.Bitmap;

import com.ray.lib_map.ScaleLevel;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapPoint;
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

@SuppressWarnings({"WeakerAccess", "unused"})
public class MapHelper {
    private MapHelper() {
        throw new UnsupportedOperationException();
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

    public static MapMarker createMarker(Address address, Context context, int resId) {
        return createMarker(context, address.getMapPoint(), resId, address.getName(), address.getFormattedAddress());
    }

    public static MapMarker createMarker(Poi poi, Context context, int resId) {
        return createMarker(context, poi.getMapPoint(), resId, poi.getName(), poi.getAddress());
    }

    public static MapMarker createMarker(Context context, MapPoint mapPoint, int resId) {
        return createMarker(context, mapPoint, resId, "", "");
    }

    public static MapMarker createMarker(Context context, MapPoint mapPoint, int resId, String title, String content) {
        MapMarker mapMarker = new MapMarker(mapPoint, BitmapUtil.fromResource(context, resId));
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        mapMarker.setTitle(title);
        mapMarker.setContent(content);
        return mapMarker;
    }

    public static MapMarker buildLocationMarker(MapPoint mapPoint, Bitmap bitmap) {
        MapMarker mapMarker = new MapMarker(mapPoint, bitmap);
        mapMarker.setAnchorX(0.5f);
        mapMarker.setAnchorY(0.5f);
        return mapMarker;
    }

    public static float convertScaleLevelToMapZoom(ScaleLevel scaleLevel) {
        switch (scaleLevel) {
            case COUNTRY:
                return 4.5f;
            case PROVINCE:
                return 6;
            case CITY:
                return 11;
            case DISTRICT:
                return 13.5f;
            case STREET:
                return 14.5f;
            default:
                return 13.5f;
        }
    }

    public static ScaleLevel convertMapZoomToScaleLevel(float scaleLevel) {
        if (scaleLevel <= 4.5f) {
            return ScaleLevel.COUNTRY;
        } else if (scaleLevel <= 6) {
            return ScaleLevel.PROVINCE;
        } else if (scaleLevel <= 11) {
            return ScaleLevel.CITY;
        } else if (scaleLevel <= 13.5) {
            return ScaleLevel.DISTRICT;
        } else if (scaleLevel <= 20) {
            return ScaleLevel.STREET;
        }
        return ScaleLevel.DISTRICT;
    }

    public static List<MapPoint> converMapMarkersToMapPoints(List<MapMarker> markers) {
        ArrayList<MapPoint> mapPoints = new ArrayList<>();
        for (MapMarker marker : markers) {
            MapPoint mapPoint = marker.getMapPoint();
            if (mapPoint != null) {
                mapPoints.add(mapPoint);
            }
        }
        return mapPoints;
    }
}
