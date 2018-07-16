package com.ray.lib_map.impl.gaode;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ray.lib_map.R;
import com.ray.lib_map.data.DataCallback;
import com.ray.lib_map.data.FailureCallback;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;

import java.util.ArrayList;
import java.util.List;


/**
 * @author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 高德地图数据源，处理高德地图的数据获取请求
 */

public class GaodeDataSource implements MapDataSource {
    private static final int LOCATION_INTERVAL = 180 * 1000;
    private static final String SEARCH_TYPE = "";//"汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
    private final Context mContext;

    public GaodeDataSource(Context context) {
        mContext = context;
    }

    private AMapLocationClient createOnceLocationClient() {
        AMapLocationClient client = new AMapLocationClient(mContext);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setOnceLocation(true);
        option.setOnceLocationLatest(true);
        option.setNeedAddress(true);
        option.setMockEnable(false);
        option.setLocationCacheEnable(true);
        option.setHttpTimeOut(LOCATION_INTERVAL);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        client.setLocationOption(option);

        return client;
    }

    @Override
    public void reverseGeoCode(MapPoint mapPoint, float radius, DataCallback<Address> callback) {
        GeocodeSearch geoCoderSearch = new GeocodeSearch(mContext);
        MapPoint gaodePoint = mapPoint.copy(MapType.GAODE.getCoordinateType());
        LatLonPoint point = new LatLonPoint(gaodePoint.getLatitude(), gaodePoint.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(point, radius, GeocodeSearch.AMAP);
        RegeocodeAddress regeocodeAddress;
        try {
            regeocodeAddress = geoCoderSearch.getFromLocation(query);
        } catch (AMapException e) {
            e.printStackTrace();
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, e.getErrorMessage());
            return;
        }
        if (regeocodeAddress == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, "逆地理解析失败");
            return;
        }

        Address address = GaodeDataConverter.toAddress(regeocodeAddress, mapPoint);
        callback.onSuccess(address);
    }

    @Override
    public void queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize, POISearchCallback callback) {
        PoiSearch.Query query = new PoiSearch.Query(SEARCH_TYPE, null);
        query.setPageSize(pageSize);
        query.setPageNum(pageIndex);
        mapPoint = mapPoint.copy(MapType.GAODE.getCoordinateType());
        LatLonPoint lp = new LatLonPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
        PoiSearch poiSearch = new PoiSearch(mContext, query);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, searchBound, true));

        PoiResult poiResult;
        try {
            poiResult = poiSearch.searchPOI();
        } catch (AMapException e) {
            e.printStackTrace();
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, e.getErrorMessage());
            return;
        }

        if (poiResult == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.wufahuoqushuju));
            return;
        }

        ArrayList<PoiItem> pois = poiResult.getPois();
        if (pois != null && pois.size() > 0) {
            callback.onSuccess(GaodeDataConverter.toPoiList(pois));
            return;
        }

        List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
        if (suggestionCities != null && suggestionCities.size() > 0) {
            callback.onSuggestion(GaodeDataConverter.toSuggestions(suggestionCities));
            return;
        }

        callback.onNoSearchResult();
    }

    @Override
    public void queryPoi(String keyword, String city, int pageIndex, int pageSize, POISearchCallback callback) {
        PoiSearch.Query query = new PoiSearch.Query(keyword, SEARCH_TYPE, city);
        query.setPageSize(pageSize);
        query.setPageNum(pageIndex);
        PoiSearch poiSearch = new PoiSearch(mContext, query);

        PoiResult poiResult;
        try {
            poiResult = poiSearch.searchPOI();
        } catch (AMapException e) {
            e.printStackTrace();
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, e.getErrorMessage());
            return;
        }

        if (poiResult == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.wufahuoqushuju));
            return;
        }

        ArrayList<PoiItem> pois = poiResult.getPois();
        if (pois != null && pois.size() > 0) {
            callback.onSuccess(GaodeDataConverter.toPoiList(pois));
            return;
        }

        List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
        if (suggestionCities != null && suggestionCities.size() > 0) {
            callback.onSuggestion(GaodeDataConverter.toSuggestions(suggestionCities));
            return;
        }

        callback.onNoSearchResult();
    }

    @Override
    public void geoCode(String address, String city, DataCallback<MapPoint> callback) {
        GeocodeQuery query = new GeocodeQuery(address, city);
        GeocodeSearch geoCoderSearch = new GeocodeSearch(mContext);

        List<GeocodeAddress> addresses;
        try {
            addresses = geoCoderSearch.getFromLocationName(query);
        } catch (AMapException e) {
            e.printStackTrace();
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, e.getErrorMessage());
            return;
        }

        if (addresses == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.wufahuoqushuju));
            return;
        }

        for (GeocodeAddress geocodeAddress : addresses) {
            if (geocodeAddress == null) {
                continue;
            }
            LatLonPoint latLonPoint = geocodeAddress.getLatLonPoint();
            if (latLonPoint == null) {
                continue;
            }

            callback.onSuccess(new MapPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude(), MapType.GAODE.getCoordinateType()));
            return;
        }

        callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.wufahuoqushuju));
    }

    @Override
    public void locate(DataCallback<Address> callback) {
        final AMapLocationClient client = createOnceLocationClient();
        final AMapLocation[] location = new AMapLocation[1];
        client.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                location[0] = aMapLocation;
                synchronized (client) {
                    client.notify();
                }
            }
        });
        client.startLocation();
        try {
            synchronized (client) {
                client.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stopLocation();
        client.onDestroy();

        AMapLocation aMapLocation = location[0];
        if (aMapLocation == null) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.wufahuoqushuju));
            return;
        }
        if (aMapLocation.getErrorCode() != 0) {
            callback.onFailure(FailureCallback.ERROR_CODE_NO_RESULT, mContext.getResources().getString(R.string.dingweishibai) + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo());
            return;
        }

        callback.onSuccess(GaodeDataConverter.toAddress(aMapLocation));
    }
}
