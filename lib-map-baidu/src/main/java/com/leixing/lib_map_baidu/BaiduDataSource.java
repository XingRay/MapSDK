package com.leixing.lib_map_baidu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.leixing.lib_map_baidu.params.GeoCodeParam;
import com.leixing.lib_map_baidu.params.ReGeoCodeParam;
import com.ray.lib_map.KeyManager;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.MapConfig;
import com.ray.lib_map.util.IOUtil;

import java.util.List;


/**
 * @author      : leixing
 * Date        : 2017-10-20
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class BaiduDataSource implements MapDataSource {
    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    private final Context mContext;
    private LocationClient mLocationClient;
    private BDLocation mBDLocation;
    private final BDAbstractLocationListener mLocationListener;

    /**
     * 请在主线程中声明LocationClient类对象，该对象初始化需传入Context类型参数。
     * 推荐使用{@code getApplicationContext()}方法获取全进程有效的Context。
     */
    public BaiduDataSource(Context context) {
        mContext = context;
        mLocationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mBDLocation = bdLocation;
                synchronized (mLocationListener) {
                    mLocationListener.notify();
                }
            }
        };

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mLocationClient = new LocationClient(mContext);
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);
                option.setCoorType("gcj02");
                option.setProdName("com.hecom.sales.4.0");
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                option.setIsNeedAddress(true);
                option.setOpenGps(true);
                option.setScanSpan(3000);
                mLocationClient.setLocOption(option);
                mLocationClient.registerLocationListener(mLocationListener);
            }
        });
    }

    @Override
    public void reverseGeoCode(MapPoint mapPoint, float radius, DataCallback<Address> callback) {
        MapPoint baiduPoint = mapPoint.copy(MapConfig.BAIDU);
        ReGeoCodeParam param = new ReGeoCodeParam(baiduPoint.getLatitude(), baiduPoint.getLongitude(), KeyManager.getBaiduApiKey(mContext), KeyManager.getBaiduMCode(mContext));
        String response = IOUtil.get(param.buildRequestUrl());
        Address address = BaiduDataConverter.parseReGeoResponse(response);
        if (address == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "逆地理解析失败");
            return;
        }
        callback.onSuccess(address);
    }

    @Override
    public void geoCode(String address, String city, DataCallback<MapPoint> callback) {
        GeoCodeParam param = new GeoCodeParam(address, KeyManager.getBaiduApiKey(mContext), KeyManager.getBaiduMCode(mContext));
        String response = IOUtil.get(param.buildRequestUrl());
        MapPoint mapPoint = BaiduDataConverter.parseGeoResponse(response);
        if (mapPoint == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "地理解析失败");
            return;
        }
        callback.onSuccess(mapPoint);
    }

    @Override
    public void queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize, final POISearchCallback callback) {
        final PoiResult result = new PoiResult();
        final PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                result.setPoiInfo(poiResult.getAllPoi());
                result.setSuggestCityList(poiResult.getSuggestCityList());

                synchronized (result) {
                    result.notify();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                synchronized (result) {
                    result.notify();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                synchronized (result) {
                    result.notify();
                }
            }
        });
        MapPoint baiduPoint = mapPoint.copy(MapConfig.BAIDU);
        LatLng latLng = new LatLng(baiduPoint.getLatitude(), baiduPoint.getLongitude());

        final PoiNearbySearchOption searchOption = new PoiNearbySearchOption()
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(latLng)
                .keyword("")
                .radius(searchBound)
                .pageCapacity(pageSize)
                .pageNum(pageIndex);
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                poiSearch.searchNearby(searchOption);
            }
        });


        synchronized (result) {
            try {
                result.wait(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "POI查询失败");
                return;
            }
        }

        List<PoiInfo> poiInfoList = result.getAllPoi();
        List<Poi> pois = BaiduDataConverter.poiInfoListToPoiList(poiInfoList);
        if (pois != null && pois.size() > 0) {
            callback.onSuccess(pois);
            return;
        }

        List<PoiSearchSuggestion> suggestions = BaiduDataConverter.suggestCityListToSuggestionList(result.getSuggestCityList());
        if (suggestions != null && suggestions.size() > 0) {
            callback.onSuggestion(suggestions);
            return;
        }

        callback.onNoSearchResult();
    }

    @Override
    public void queryPoi(String keyword, String city, int pageIndex, int pageSize, final POISearchCallback callback) {
        final PoiResult result = new PoiResult();
        final PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                result.setPoiInfo(poiResult.getAllPoi());
                result.setSuggestCityList(poiResult.getSuggestCityList());

                synchronized (result) {
                    result.notify();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                synchronized (result) {
                    result.notify();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                synchronized (result) {
                    result.notify();
                }
            }
        });
        final PoiCitySearchOption searchOption = new PoiCitySearchOption()
                .city(city)
                .keyword(keyword)
                .pageCapacity(pageSize)
                .pageNum(pageIndex);

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                poiSearch.searchInCity(searchOption);
            }
        });

        synchronized (result) {
            try {
                result.wait(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "POI查询失败");
                return;
            }
        }

        List<PoiInfo> poiInfoList = result.getAllPoi();
        List<Poi> pois = BaiduDataConverter.poiInfoListToPoiList(poiInfoList);
        if (pois != null && pois.size() > 0) {
            callback.onSuccess(pois);
            return;
        }


        List<PoiSearchSuggestion> suggestions = BaiduDataConverter.suggestCityListToSuggestionList(result.getSuggestCityList());
        if (suggestions != null && suggestions.size() > 0) {
            callback.onSuggestion(suggestions);
            return;
        }

        callback.onNoSearchResult();
    }

    //    @Override
//    public void queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize, POISearchCallback callback) {
//        MapPoint bdPoint = mapPoint.copy(CoordinateType.BD09);
//        NearbyPoiSearchParam param = new NearbyPoiSearchParam(bdPoint.getLatitude(), bdPoint.getLongitude(), searchBound, pageSize, pageIndex, Contants.getBaiduApiKey(mContext), Contants.getBaiduMCode(mContext));
//        String response = IOUtil.get(param.buildRequestUrl());
//        List<Poi> pois = BaiduDataConverter.parsePoiSearchResponse(response);
//        if (pois == null) {
//            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "查询POI失败");
//            return;
//        }
//        if (pois.isEmpty()) {
//            callback.onNoSearchResult();
//            return;
//        }
//        callback.onSuccess(pois);
//    }
//
//    @Override
//    public void queryPoi(String keyword, String city, int pageIndex, int pageSize, POISearchCallback callback) {
//        RegionPoiSearchParam param = new RegionPoiSearchParam(keyword, city, pageSize, pageIndex, Contants.getBaiduApiKey(mContext), Contants.getBaiduMCode(mContext));
//        String response = IOUtil.get(param.buildRequestUrl());
//        List<Poi> pois = BaiduDataConverter.parsePoiSearchResponse(response);
//        if (pois == null) {
//            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "查询POI失败");
//            return;
//        }
//        if (pois.isEmpty()) {
//            callback.onNoSearchResult();
//            return;
//        }
//        callback.onSuccess(pois);
//    }

    @Override
    public void locate(DataCallback<Address> callback) {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }

        synchronized (mLocationListener) {
            try {
                // time out 30s
                mLocationListener.wait(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "定位失败");
                return;
            }
        }
        mLocationClient.stop();

        if (callback != null) {
            callback.onSuccess(BaiduDataConverter.bdLocationToAddress(mBDLocation));
        }
    }
}
