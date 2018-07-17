package com.leixing.lib_map_google.params;


import com.ray.lib_map.util.StringUtil;

/**
 * @author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 地理编码是将地址（如“1600 Amphitheatre Parkway, Mountain View, CA”）转换为地理坐标
 * （如纬度 37.423021 和经度 -122.083739）的过程，您可以借此在地图上放置标记，或在地图上定位。
 */

public class GeoCodeParam {
    /**
     * 要进行地理编码的街道地址，采用相关国家/地区的全国邮政服务所使用的格式。应避免其他地址元素，
     * 例如企业名称以及单元号、套房号或楼层
     */
    private String address;
    /**
     * 应用的 API 密钥
     */
    private String key;

    /**
     * 视口的边框，在其中可以使地理编码结果更显著地发生偏向。此参数只会影响，而不会完全限制地理编码器中的结果
     */
    private String bounds;

    /**
     * 返回结果时使用的语言
     */
    private Language language;

    /**
     * 地区代码，指定为一个 ccTLD（“顶级域名”）双字符值。此参数只会影响，而不会完全限制地理编码器中的结果
     */
    private String region;

    /**
     * 组成部分过滤器，用管道符号 (|) 分隔。每个组成部分过滤器由一个 component:value 对组成，将完全限制地理编码器中的结果
     */
    private String components;

    public GeoCodeParam(String address, String key) {
        this.address = address;
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "GeoCodingParam{" +
                "address='" + address + '\'' +
                ", key='" + key + '\'' +
                ", bounds='" + bounds + '\'' +
                ", language=" + language +
                ", region='" + region + '\'' +
                ", components='" + components + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?")
                .append("address=").append(address)
                .append("&key=").append(key);

        StringUtil.appendIfNotEmpty(urlBuilder, "bounds", bounds);
        StringUtil.appendIfNotEmpty(urlBuilder, "language", language == null ? null : language.getCode());
        StringUtil.appendIfNotEmpty(urlBuilder, "region", region);
        StringUtil.appendIfNotEmpty(urlBuilder, "components", components);

        return urlBuilder.toString();
    }
}
