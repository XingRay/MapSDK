package com.leixing.lib_map_baidu;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.extern.ZoomStandardization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
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

    static List<LatLng> fromMapPoints(List<MapPoint> points) {
        List<LatLng> latLngs = new ArrayList<>();
        if (points == null) {
            return latLngs;
        }

        for (MapPoint mapPoint : points) {
            latLngs.add(fromMapPoint(mapPoint));
        }

        return latLngs;
    }

    static LatLng fromMapPoint(MapPoint mapPoint) {
        MapPoint baiduPoint = mapPoint.copy(MapType.BAIDU.getCoordinateType());
        return new LatLng(baiduPoint.getLatitude(), baiduPoint.getLongitude());
    }

    static Address bdLocationToAddress(BDLocation bdLocation) {
        if (bdLocation == null) {
            return null;
        }
        Address address = new Address();
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        address.setMapPoint(new MapPoint(latitude, longitude, CoordinateType.GCJ02));
        address.setName(bdLocation.getBuildingName());
        address.setCity(bdLocation.getCity());
        address.setCityCode(bdLocation.getCityCode());
        address.setDistrict(bdLocation.getDistrict());
        address.setProvince(bdLocation.getProvince());
        address.setCountry(bdLocation.getCountry());
        address.setFormattedAddress(bdLocation.getAddrStr());
        return address;
    }

    public static Address parseReGeoResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.has("status")) {
                return null;
            }
            int status = jsonObject.getInt("status");
            if (status != 0) {
                return null;
            }
            if (!jsonObject.has("result")) {
                return null;
            }
            JSONObject result = jsonObject.getJSONObject("result");
            Address address = new Address();
            if (result.has("formatted_address")) {
                address.setFormattedAddress(result.getString("formatted_address"));
            }
            if (result.has("cityCode")) {
                address.setCityCode(result.getString("cityCode"));
            }
            if (result.has("addressComponent")) {
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                if (addressComponent.has("city")) {
                    address.setCity(addressComponent.getString("city"));
                }

                if (addressComponent.has("province")) {
                    address.setProvince(addressComponent.getString("province"));
                }
                if (addressComponent.has("district")) {
                    address.setDistrict(addressComponent.getString("district"));
                }
            }

            if (result.has("location")) {
                JSONObject location = result.getJSONObject("location");
                double latitude = location.getDouble("lat");
                double longitude = location.getDouble("lng");
                address.setMapPoint(new MapPoint(latitude, longitude, MapType.BAIDU.getCoordinateType()));
            }

            String name = null;
            if (result.has("pois")) {
                JSONArray pois = result.getJSONArray("pois");
                if (pois.length() > 0) {
                    JSONObject poi = pois.getJSONObject(0);
                    if (poi.has("name")) {
                        name = poi.getString("name");
                    }
                }
            }
            if (TextUtils.isEmpty(name)) {
                name = address.getFormattedAddress();
            }
            address.setName(name);
            return address;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MapPoint parseGeoResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.has("status")) {
                return null;
            }

            int status = jsonObject.getInt("status");
            if (status != 0) {
                return null;
            }
            if (!jsonObject.has("result")) {
                return null;
            }

            JSONObject result = jsonObject.getJSONObject("result");
            if (!result.has("location")) {
                return null;
            }
            JSONObject location = result.getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lng");
            return new MapPoint(latitude, longitude, MapType.BAIDU.getCoordinateType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static List<Poi> parsePoiSearchResponse(String response) {
//        if (TextUtils.isEmpty(response)) {
//            return null;
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (!jsonObject.has("status")) {
//                return null;
//            }
//            int status = jsonObject.getInt("status");
//            if (status != 0) {
//                return null;
//            }
//            return null;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    static List<PoiSearchSuggestion> suggestCityListToSuggestionList(List<CityInfo> suggestCityList) {
        if (suggestCityList == null) {
            return null;
        }
        List<PoiSearchSuggestion> poiSearchSuggestions = new ArrayList<>();
        for (CityInfo cityInfo : suggestCityList) {
            poiSearchSuggestions.add(suggestCityToSuggestion(cityInfo));
        }
        return poiSearchSuggestions;
    }

    static PoiSearchSuggestion suggestCityToSuggestion(CityInfo cityInfo) {
        PoiSearchSuggestion suggestion = new PoiSearchSuggestion();
        suggestion.setCityName(cityInfo.city);
        suggestion.setSuggestionNum(cityInfo.num);
        return suggestion;
    }

    static List<Poi> poiInfoListToPoiList(List<PoiInfo> poiInfoList) {
        if (poiInfoList == null) {
            return null;
        }
        List<Poi> pois = new ArrayList<>();
        for (PoiInfo poiInfo : poiInfoList) {
            pois.add(poiInfoToPoi(poiInfo));
        }
        return pois;
    }

    static Poi poiInfoToPoi(PoiInfo poiInfo) {
        Poi poi = new Poi();
        poi.setAddress(poiInfo.address);
        poi.setCityName(poiInfo.city);
        poi.setMapPoint(new MapPoint(poiInfo.location.latitude, poiInfo.location.longitude, MapType.BAIDU.getCoordinateType()));
        poi.setName(poiInfo.name);
        return poi;
    }
}
