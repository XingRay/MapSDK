package com.ray.lib_map.impl.gaode;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.CoordinateType;
import com.ray.lib_map.extern.MapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class GaodeDataConverter {
    private GaodeDataConverter() {
        throw new UnsupportedOperationException();
    }

    public static List<Poi> toPoiList(List<PoiItem> poiItems) {
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

    public static Poi toPoi(PoiItem poiItem) {
        if (poiItem == null) {
            throw new NullPointerException();
        }
        Poi poi = new Poi();

        poi.setDistance(Math.max(0, poiItem.getDistance()));
        poi.setName(poiItem.getTitle());
        poi.setAddress(poiItem.getSnippet());

        LatLonPoint latLonPoint = poiItem.getLatLonPoint();
        poi.setMapPoint(new MapPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude(), MapType.GAODE.getCoordinateType()));

        poi.setProvinceName(poiItem.getProvinceName());
        poi.setProvinceCode(poiItem.getProvinceCode());

        poi.setCityName(poiItem.getCityName());
        poi.setCityCode(poiItem.getCityCode());

        poi.setDistrictName(poiItem.getAdName());
        poi.setDistrictCode(poiItem.getAdCode());

        return poi;
    }

    public static List<PoiSearchSuggestion> toSuggestions(List<SuggestionCity> suggestionCities) {
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


    public static Address toAddress(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return null;
        }
        Address address = new Address();

        address.setMapPoint(new MapPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude(), CoordinateType.GCJ02));

        address.setCity(aMapLocation.getCity());
        address.setCityCode(aMapLocation.getCityCode());

        address.setDistrict(aMapLocation.getDistrict());
        address.setDistrictCode(aMapLocation.getAdCode());

        address.setProvince(aMapLocation.getProvince());

        return address;
    }

    public static Address toAddress(RegeocodeAddress regeocodeAddress, double latitude, double longitude) {
        if (regeocodeAddress == null) {
            return null;
        }
        Address address = new Address();

        address.setMapPoint(new MapPoint(latitude, longitude, CoordinateType.GCJ02));

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
        String city = regeocodeAddress.getCity();
        address.setProvince(province);
        address.setCity(TextUtils.isEmpty(city) ? province : city);

        address.setCityCode(regeocodeAddress.getCityCode());
        address.setDistrict(regeocodeAddress.getDistrict());
        address.setDistrictCode(regeocodeAddress.getAdCode());

        address.setFormattedAddress(regeocodeAddress.getFormatAddress());

        return address;
    }
}
