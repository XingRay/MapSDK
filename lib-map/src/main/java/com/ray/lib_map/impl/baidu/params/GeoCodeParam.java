package com.ray.lib_map.impl.baidu.params;

/**
 * Author      : leixing
 * Date        : 2017-10-31
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GeoCodeParam {
    private String address;
    private String key;
    private String code;

    public GeoCodeParam(String address, String key, String code) {
        this.address = address;
        this.key = key;
        this.code = code;
    }

    @Override
    public String toString() {
        return "GeoCodeParam{" +
                "address='" + address + '\'' +
                ", key='" + key + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        return "http://api.map.baidu.com/geocoder/v2/?"
                + "address=" + address
                + "&output=json"
                + "&ak=" + key
                + "&mcode=" + code;
    }
}
