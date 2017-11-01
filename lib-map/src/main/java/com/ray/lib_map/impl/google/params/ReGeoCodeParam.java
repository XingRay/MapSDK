package com.ray.lib_map.impl.google.params;


import com.ray.lib_map.util.StringUtil;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 反向地理编码是将地理坐标转换为可人工读取的地址的过程。Google Maps Geocoding API 的反向地理
 * 编码服务还可让您找到对应于给定的地点 ID 的地址。
 */

public class ReGeoCodeParam {
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    /**
     * API key
     */
    private String key;

    /**
     * 返回结果时使用的语言
     */
    private Language language;

    /**
     * 地址类型 地址类型的示例：country、street_address、postal_code
     * 如果指定了一种类型，会将结果限制于这种类型。如果指定了多种类型，该 API 将返回匹配其中任何类型的所有地址
     */
    private List<String> resultTypes;

    /**
     * 位置类型
     * 如果指定了一种类型，会将结果限制于这种类型。如果指定了多种类型，该 API 将返回匹配其中任何类型的所有地址
     * <p>
     * 支持使用以下值：
     * "ROOFTOP" 将结果限制为我们使其位置信息精确到街道地址精度的地址。
     * "RANGE_INTERPOLATED" 将结果限制为反映了两个精确点（例如交叉路口）之间用内插法计算得到的近似值（通常在道路上）的地址。内插的范围通常表示某个街道地址的 rooftop 地理编码不可用。
     * "GEOMETRIC_CENTER" 将结果限制为某个位置（如多段线（例如街道）或多边形（地区））的几何中心。
     * "APPROXIMATE" 将结果限制为是近似值的地址。
     */
    private List<String> locationTypes;

    public ReGeoCodeParam(double latitude, double longitude, String key) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<String> getResultTypes() {
        return resultTypes;
    }

    public void setResultTypes(List<String> resultTypes) {
        this.resultTypes = resultTypes;
    }

    public List<String> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<String> locationTypes) {
        this.locationTypes = locationTypes;
    }

    @Override
    public String toString() {
        return "ReGeoCodeParam{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", key='" + key + '\'' +
                ", language=" + language +
                ", resultTypes=" + resultTypes +
                ", locationTypes=" + locationTypes +
                '}';
    }

    public String buildRequestUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?")
                .append("latlng=").append(latitude).append(",").append(longitude)
                .append("&key=").append(key);
        StringUtil.appendIfNotEmpty(urlBuilder, "language", language == null ? null : language.getCode());
        StringUtil.appendIfNotEmpty(urlBuilder, "result_type", StringUtil.fromList(resultTypes, "|"));
        StringUtil.appendIfNotEmpty(urlBuilder, "location_type", StringUtil.fromList(locationTypes, "|"));

        return urlBuilder.toString();
    }
}
