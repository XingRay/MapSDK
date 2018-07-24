package com.ray.lib_map.data;

import com.ray.lib_map.base.Result;
import com.ray.lib_map.base.Result2;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.MapConfig;

import java.util.List;


/**
 * @author : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class MapDataRepository implements MapDataSource {
    private MapConfig mMapConfig;
    private MapDataSource mDataSource;

    public MapDataRepository(MapConfig mapConfig) {
        switchMapType(mapConfig);
    }

    /**
     * 切换地图数据源的类型
     *
     * @param mapConfig 地图类型
     */
    public void switchMapType(MapConfig mapConfig) {
        if (mapConfig == null) {
            throw new IllegalArgumentException("mapConfig can not be null");
        }

        if (mMapConfig == mapConfig) {
            return;
        }

        mMapConfig = mapConfig;
        mDataSource = mapConfig.getDataSourceFactory().create();
    }

    @Override
    public Result<Address> reverseGeoCode(MapPoint mapPoint, float radius) {
        return mDataSource.reverseGeoCode(mapPoint, radius);
    }

    @Override
    public Result<MapPoint> geoCode(String address, String city) {
        return mDataSource.geoCode(address, city);
    }

    @Override
    public Result2<List<Poi>, List<PoiSearchSuggestion>> queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize) {
        return mDataSource.queryPoi(mapPoint, searchBound, pageIndex, pageSize);
    }

    @Override
    public Result2<List<Poi>, List<PoiSearchSuggestion>> queryPoi(String keyword, String city, int pageIndex, int pageSize) {
        return mDataSource.queryPoi(keyword, city, pageIndex, pageSize);
    }

    @Override
    public Result<Address> locate() {
        return mDataSource.locate();
    }
}
