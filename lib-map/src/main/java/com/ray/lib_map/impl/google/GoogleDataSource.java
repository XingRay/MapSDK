package com.ray.lib_map.impl.google;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.ray.lib_map.KeyManager;
import com.ray.lib_map.data.DataCallback;
import com.ray.lib_map.data.FailureCallback;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.extern.CoordinateType;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.impl.google.params.GeoCodeParam;
import com.ray.lib_map.impl.google.params.Language;
import com.ray.lib_map.impl.google.params.NearbySearchParam;
import com.ray.lib_map.impl.google.params.ReGeoCodeParam;
import com.ray.lib_map.impl.google.params.TextSearchParam;
import com.ray.lib_map.util.IOUtil;
import com.ray.lib_map.util.MapUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.ray.lib_map.data.FailureCallback.ERROR_CODE_NO_RESULT;

/**
 * Author      : leixing
 * Date        : 2017-10-20
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */
public class GoogleDataSource implements MapDataSource {
    private final String SEARCH_API_KEY;
    private final String GEO_API_KEY;

    private final Context mContext;
    private final GoogleApiClient mApiClient;

    public GoogleDataSource(Context context) {

        mContext = context;
        mApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        SEARCH_API_KEY = KeyManager.getGoogleSearchApiKey(context);
        GEO_API_KEY = KeyManager.getGoogleGeoApiKey(context);
    }

    @Override
    public void geoCode(String address, String city, DataCallback<MapPoint> callback) {
        GeoCodeParam param = new GeoCodeParam(address, GEO_API_KEY);
        param.setLanguage(Language.getSystemLanguage(mContext));

        String response = IOUtil.get(param.buildRequestUrl());
        if (TextUtils.isEmpty(response)) {
            callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
            return;
        }

        MapPoint point = GoogleDataConverter.geoResultToMapPoint(response);
        if (point == null) {
            callback.onFailure(ERROR_CODE_NO_RESULT, "地理解析失败");
            return;
        }

        callback.onSuccess(point);
    }

    @Override
    public void reverseGeoCode(MapPoint mapPoint, float radius, DataCallback<Address> callback) {
        reverseGeoCode(mapPoint, radius, Language.ZH_CN, callback);
    }


    private void reverseGeoCode(MapPoint mapPoint, float radius, Language language, DataCallback<Address> callback) {
        MapPoint googlePoint = mapPoint.copy(MapType.GOOGLE.getCoordinateType());
        ReGeoCodeParam param = new ReGeoCodeParam(googlePoint.getLatitude(), googlePoint.getLongitude(), GEO_API_KEY);
        param.setLanguage(language);

        String response = IOUtil.get(param.buildRequestUrl());
        if (TextUtils.isEmpty(response)) {
            callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
            return;
        }

        Address address = GoogleDataConverter.reGeoResultToAddress(response);
        if (address == null) {
            callback.onFailure(ERROR_CODE_NO_RESULT, "逆地理解析失败");
            return;
        }

        callback.onSuccess(address);
    }

    @Override
    public void queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize, POISearchCallback callback) {
        MapPoint googlePoint = mapPoint.copy(MapType.GOOGLE);
        NearbySearchParam param = new NearbySearchParam(SEARCH_API_KEY, googlePoint.getLatitude(), googlePoint.getLongitude()/*, searchBound*/);
        param.setLanguage(Language.getSystemLanguage(mContext));
        param.setRankBy("distance");
        String response = IOUtil.get(param.buildRequestUrl());
        if (TextUtils.isEmpty(response)) {
            callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
            return;
        }

        List<Poi> pois = GoogleDataConverter.nearbySearchResponseToPois(response);
        //计算距离,超过距离的排除
        if (pois == null || pois.size() == 0) {
            callback.onNoSearchResult();
            return;
        }

        Iterator<Poi> iterator = pois.iterator();
        while (iterator.hasNext()) {
            Poi poi = iterator.next();
            int distance = (int) MapUtil.getDistance(googlePoint, new MapPoint(poi.getLatitude(), poi.getLongitude(), MapType.GOOGLE.getCoordinateType()));
            if (distance >= searchBound) {
                iterator.remove();
            } else {
                poi.setDistance(distance);
            }
        }

        Collections.sort(pois, new Comparator<Poi>() {
            @Override
            public int compare(Poi o1, Poi o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                return o1.getDistance() - o2.getDistance();
            }
        });

        callback.onSuccess(pois);
    }


    public void queryPoi(MapPoint mapPoint, String keyword, int searchBound, int pageIndex, int pageSize, POISearchCallback callback) {
        queryPoi(mapPoint, keyword, searchBound, pageIndex, pageSize, Language.ZH_CN, callback);
    }

    private void queryPoi(MapPoint mapPoint, String keyword, int searchBound, int pageIndex, int pageSize, Language language, POISearchCallback callback) {
        MapPoint googlePoint = mapPoint.copy(MapType.GOOGLE);
        NearbySearchParam param = new NearbySearchParam(SEARCH_API_KEY, googlePoint.getLatitude(), googlePoint.getLongitude());
        param.setKeyword(keyword);
        param.setLanguage(language);
        param.setRankBy("distance");
        String response = IOUtil.get(param.buildRequestUrl());
        if (TextUtils.isEmpty(response)) {
            callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
            return;
        }

        List<Poi> pois = GoogleDataConverter.nearbySearchResponseToPois(response);
        if (pois == null || pois.size() == 0) {
            callback.onNoSearchResult();
            return;
        }

        //计算距离,超过距离的排除
        Iterator<Poi> iterator = pois.iterator();
        while (iterator.hasNext()) {
            Poi poi = iterator.next();
            int distance = (int) MapUtil.getDistance(googlePoint, new MapPoint(poi.getLatitude(), poi.getLongitude(), MapType.GOOGLE.getCoordinateType()));
            if (distance >= searchBound) {
                iterator.remove();
            } else {
                poi.setDistance(distance);
            }
        }

        Collections.sort(pois, new Comparator<Poi>() {
            @Override
            public int compare(Poi o1, Poi o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                return o1.getDistance() - o2.getDistance();
            }
        });

        callback.onSuccess(pois);
    }

    @Override
    public void queryPoi(final String keyword, String city, int pageIndex, int pageSize, final POISearchCallback callback) {
        geoCode(city, city, new DataCallback<MapPoint>() {
            @Override
            public void onSuccess(MapPoint mapPoint) {
                TextSearchParam param = new TextSearchParam(keyword, SEARCH_API_KEY);
                param.setLongitude(mapPoint.getLongitude());
                param.setLatitude(mapPoint.getLatitude());
                param.setRadius(10000);
                param.setLanguage(Language.getSystemLanguage(mContext));
                String response = IOUtil.get(param.buildRequestUrl());
                if (TextUtils.isEmpty(response)) {
                    callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
                    return;
                }

                List<Poi> pois = GoogleDataConverter.nearbySearchResponseToPois(response);
                if (pois == null || pois.size() == 0) {
                    callback.onNoSearchResult();
                    return;
                }

                callback.onSuccess(pois);
            }

            @Override
            public void onFailure(int errorCode, String desc) {
                callback.onFailure(FailureCallback.ERROR_CODE_CONNECT_FAILED, "网络连接失败");
            }
        });


    }

    @WorkerThread
    @Override
    public void locate(final DataCallback<Address> callback) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("必须在子线程调用");
        }

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onFailure(FailureCallback.ERROR_CODE_PERMISSION_DENY, "需要授予相关权限");
            return;
        }

        ConnectionResult connectionResult = mApiClient.blockingConnect(30, TimeUnit.SECONDS);
        if (connectionResult.isSuccess()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setMaxWaitTime(10 * 1000)
                    .setPriority(PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1);
//            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//
//                }
//            });

            // TODO: 2017-11-01 完善Google定位
            @SuppressWarnings("deprecation")
            Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
            if (location == null) {
                callback.onFailure(ERROR_CODE_NO_RESULT, "定位失败");
            } else {
                Address address = new Address();
                address.setMapPoint(new MapPoint(location.getLatitude(), location.getLongitude(), CoordinateType.WGS84).copy(MapType.GOOGLE.getCoordinateType()));
                callback.onSuccess(address);
            }

        } else {
            callback.onFailure(ERROR_CODE_NO_RESULT, "定位失败");
        }
        Looper.loop();
    }

    public void destory() {
        if (mApiClient != null && (mApiClient.isConnected() || mApiClient.isConnecting())) {
            mApiClient.disconnect();
        }
    }
}
