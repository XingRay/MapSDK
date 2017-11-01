package com.ray.lib_map.impl.baidu.params;

/**
 * Author      : leixing
 * Date        : 2017-10-31
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class ReGeoCodeParam {
    private double latitude;
    private double longitude;
    private String key;
    private String code;

    public ReGeoCodeParam(double latitude, double longitude, String key, String code) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
        this.code = code;
    }

    public String buildRequestUrl() {
        return "http://api.map.baidu.com/geocoder/v2/?"
                + "location=" + latitude + "," + longitude
                + "&output=json"
                + "&pois=1"
                + "&ak=" + key
                + "&mcode=" + code;
    }
}
