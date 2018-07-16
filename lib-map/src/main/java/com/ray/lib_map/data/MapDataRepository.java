package com.ray.lib_map.data;

import android.content.Context;

import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.impl.baidu.BaiduDataSource;
import com.ray.lib_map.impl.gaode.GaodeDataSource;
import com.ray.lib_map.impl.google.GoogleDataSource;


/**
 * @author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class MapDataRepository implements MapDataSource {
    private final Context mContext;
    private MapType mMapType;
    private MapDataSource mMapDataSource;

    public MapDataRepository(Context context) {
        this(context, MapType.GAODE);
    }

    public MapDataRepository(Context context, MapType mapType) {
        mContext = context;
        switchMapType(mapType);
    }

    /**
     * 切换地图数据源的类型
     *
     * @param mapType 地图类型
     */
    public void switchMapType(MapType mapType) {
        if (mapType == null) {
            throw new IllegalArgumentException("mapType can not be null");
        }

        if (mMapType == mapType) {
            return;
        }

        mMapType = mapType;
        mMapDataSource = getMapDataSource(mapType);
    }

    private MapDataSource getMapDataSource(MapType mapType) {
        switch (mapType) {
            case GAODE:
                return new GaodeDataSource(mContext);
            case BAIDU:
                return new BaiduDataSource(mContext);
            case GOOGLE:
                return new GoogleDataSource(mContext);
        }
        throw new IllegalStateException("unknown map type");
    }

    @Override
    public void reverseGeoCode(MapPoint mapPoint, float radius, DataCallback<Address> callback) {
        mMapDataSource.reverseGeoCode(mapPoint, radius, callback);
    }

    @Override
    public void queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize, POISearchCallback callback) {
        mMapDataSource.queryPoi(mapPoint, searchBound, pageIndex, pageSize, callback);
    }

    @Override
    public void queryPoi(String keyword, String city, int pageIndex, int pageSize, POISearchCallback callback) {
        mMapDataSource.queryPoi(keyword, city, pageIndex, pageSize, callback);
    }

    @Override
    public void geoCode(String address, String city, DataCallback<MapPoint> callback) {
        mMapDataSource.geoCode(address, city, callback);
    }

    @Override
    public void locate(DataCallback<Address> callback) {
        mMapDataSource.locate(callback);
    }
}
