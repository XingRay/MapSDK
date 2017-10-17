package com.ray.lib_map.impl.baidu;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.extern.ZoomStandardization;

/**
 * Author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

class BaiduDataConverter {
    private BaiduDataConverter() {
        throw new UnsupportedOperationException();
    }

    static CameraPosition toCameraPosition(MapStatus mapStatus) {
        CameraPosition position = new CameraPosition();

        position.setPosition(new MapPoint(mapStatus.target.latitude, mapStatus.target.longitude, MapType.BAIDU.getCoordinateType()));
        position.setZoom(ZoomStandardization.toStandardZoom(mapStatus.zoom, MapType.BAIDU));
        position.setRotate(mapStatus.rotate);
        position.setOverlook(toStandardOverlook(mapStatus.overlook));

        return position;
    }


    static float toStandardOverlook(float overlook) {
        return overlook == 0 ? 0 : -overlook;
    }

    static float fromStandardOverlook(float overlook) {
        return overlook == 0 ? 0 : -overlook;
    }

    static MapPoint toMapPoint(LatLng latLng) {
        return new MapPoint(latLng.latitude, latLng.longitude, MapType.BAIDU.getCoordinateType());
    }
}
