package com.leixing.lib_map_amap;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.ZoomStandardization;
import com.ray.lib_map.manager.MapConfigManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : leixing
 * Date        : 2017-07-14
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
class GaodeDataConverter {
    private GaodeDataConverter() {
        throw new UnsupportedOperationException();
    }

    static CameraPosition toCameraPosition(com.amap.api.maps.model.CameraPosition cameraPosition) {
        CameraPosition position = new CameraPosition();

        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        double latitude = cameraPosition.target.latitude;
        double longitude = cameraPosition.target.longitude;
        position.setPosition(new MapPoint(latitude, longitude, coordinateType));
        position.setZoom(ZoomStandardization.toStandardZoom(cameraPosition.zoom, GaodeMap.getDefault().getName()));
        position.setRotate(cameraPosition.bearing);
        position.setOverlook(cameraPosition.tilt);

        return position;
    }

    static com.amap.api.maps.model.CameraPosition fromCameraPosition(CameraPosition position) {
        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        MapPoint mapPoint = position.getPosition().copy(coordinateType);
        LatLng latLng = new LatLng(mapPoint.getLatitude(), mapPoint.getLongitude());
        float gaodeZoom = ZoomStandardization.fromStandardZoom(position.getZoom(), GaodeMap.getDefault().getName());

        return new com.amap.api.maps.model.CameraPosition(latLng, gaodeZoom, position.getOverlook(), position.getRotate());
    }

    static MapPoint toMapPoint(LatLng latLng) {
        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        return new MapPoint(latLng.latitude, latLng.longitude, coordinateType);
    }

    static LatLng fromMapPoint(MapPoint point) {
        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        MapPoint gaodePoint = point.copy(coordinateType);
        return new LatLng(gaodePoint.getLatitude(), gaodePoint.getLongitude());
    }

    static List<LatLng> fromMapPoints(List<MapPoint> points) {
        List<LatLng> latLngs = new ArrayList<>();
        if (points == null) {
            return latLngs;
        }
        for (MapPoint point : points) {
            latLngs.add(fromMapPoint(point));
        }
        return latLngs;
    }

    static List<Poi> toPoiList(List<PoiItem> poiItems) {
        List<Poi> poiList = new ArrayList<>();
        if (poiItems == null) {
            return poiList;
        }

        for (PoiItem poiItem : poiItems) {
            if (poiItem == null) {
                continue;
            }
            poiList.add(toPoi(poiItem));
        }

        return poiList;
    }

    static Poi toPoi(PoiItem poiItem) {
        if (poiItem == null) {
            throw new NullPointerException();
        }
        Poi poi = new Poi();

        poi.setDistance(Math.max(0, poiItem.getDistance()));
        poi.setName(poiItem.getTitle());
        poi.setAddress(poiItem.getSnippet());

        LatLonPoint latLonPoint = poiItem.getLatLonPoint();
        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        poi.setMapPoint(new MapPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude(), coordinateType));

        poi.setProvinceName(poiItem.getProvinceName());
        poi.setProvinceCode(poiItem.getProvinceCode());

        poi.setCityName(poiItem.getCityName());
        poi.setCityCode(poiItem.getCityCode());

        poi.setDistrictName(poiItem.getAdName());
        poi.setDistrictCode(poiItem.getAdCode());

        return poi;
    }

    static List<PoiSearchSuggestion> toSuggestions(List<SuggestionCity> suggestionCities) {
        List<PoiSearchSuggestion> suggestions = new ArrayList<>();
        if (suggestionCities == null) {
            return suggestions;
        }

        for (SuggestionCity city : suggestionCities) {
            suggestions.add(toSuggestion(city));
        }

        return suggestions;
    }

    private static PoiSearchSuggestion toSuggestion(SuggestionCity suggestionCity) {
        if (suggestionCity == null) {
            return null;
        }

        PoiSearchSuggestion suggestion = new PoiSearchSuggestion();
        suggestion.setCityCode(suggestionCity.getCityCode());
        suggestion.setCityName(suggestionCity.getCityName());
        suggestion.setSuggestionNum(suggestionCity.getSuggestionNum());

        return suggestion;
    }


    static Address toAddress(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return null;
        }
        Address address = new Address();
        String coordinateType = GaodeMap.getDefault().getCoordinateType();
        address.setMapPoint(new MapPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude(), coordinateType));

        address.setCity(aMapLocation.getCity());
        address.setCityCode(aMapLocation.getCityCode());

        address.setDistrict(aMapLocation.getDistrict());
        address.setDistrictCode(aMapLocation.getAdCode());

        address.setProvince(aMapLocation.getProvince());

        return address;
    }

    static Address toAddress(RegeocodeAddress regeocodeAddress, MapPoint mapPoint) {
        if (regeocodeAddress == null) {
            return null;
        }
        Address address = new Address();

        address.setMapPoint(mapPoint);

        List<PoiItem> pois = regeocodeAddress.getPois();
        if (pois != null) {
            for (PoiItem poi : pois) {
                if (poi == null) {
                    continue;
                }
                address.setName(poi.getTitle());
                break;
            }
        }
        String province = regeocodeAddress.getProvince();
        address.setProvince(province);

        String city = regeocodeAddress.getCity();
        address.setCity(TextUtils.isEmpty(city) ? province : city);
        address.setCityCode(regeocodeAddress.getCityCode());

        address.setDistrict(regeocodeAddress.getDistrict());
        address.setDistrictCode(regeocodeAddress.getAdCode());

        address.setFormattedAddress(regeocodeAddress.getFormatAddress());

        return address;
    }
}
