package com.ray.lib_map.impl.google.params;


import com.ray.lib_map.util.StringUtil;

/**
 * Author      : leixing
 * Date        : 2017-10-24
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class TextSearchParam {
    /**
     * 要搜索的文本字符串，例如：“restaurant”或“123 Main Street”。Google 地点服务将会根据该字符串返回
     * 候选匹配，并根据感知的相关度对结果排序。如果搜索请求中还使用了 type 参数，此参数将变为可选参数。
     */
    private String query;

    /**
     * 应用的 API 密钥
     */
    private String key;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 定义偏向地点结果的范围（以米为单位）。所允许的最大半径为 50000 米。此地区内的结果排名将高于搜索范围外
     * 的结果；但是，可能包含搜索半径外的知名地点结果。
     */
    private int radius;

    /**
     * 语言代码，表示返回结果所应使用的语言（如提供该语言的话）。搜索功能同样偏向于所选的语言；使用所选语言
     * 的结果排名更高
     */
    private Language language;

    /**
     * minprice 和 maxprice（可选）– 将结果仅限制为指定价位内的地点。有效值范围在 0（最实惠）和 4（最昂贵）
     * 之间，包括 0 和 4 本身。特定值所表示的准确数量因区域而异。
     */
    private int minPrice = -1;
    private int maxPrice = -1;

    /**
     * 仅返回发送查询时营业的地点。如果您在查询中包含此参数，就不会返回在 Google Places 数据库中未指定
     * 开放时间的地点。
     */
    private String opennow;

    /**
     * 返回上次所运行的搜索的后续 20 个结果。设置 pagetoken 参数将用上次使用的同一参数执行搜索 – 将忽略除
     * pagetoken 之外的所有参数。
     */
    private String pagetoken;

    /**
     * 将结果限制为与指定类型匹配的地点。只能指定一个类型（如果提供了多个类型，系统会忽略第一项之后的所有类型）。
     */
    private String types;

    public TextSearchParam(String query, String key) {
        this.query = query;
        this.key = key;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getOpennow() {
        return opennow;
    }

    public void setOpennow(String opennow) {
        this.opennow = opennow;
    }

    public String getPagetoken() {
        return pagetoken;
    }

    public void setPagetoken(String pagetoken) {
        this.pagetoken = pagetoken;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "TextSearchParam{" +
                "query='" + query + '\'' +
                ", key='" + key + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", language=" + language +
                ", minprice=" + minPrice +
                ", maxprice=" + maxPrice +
                ", opennow='" + opennow + '\'' +
                ", pagetoken='" + pagetoken + '\'' +
                ", types='" + types + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?")
                .append("query=").append(query)
                .append("&key=").append(key);

        if (latitude != 0 && longitude != 0) {
            StringUtil.appendIfNotEmpty(urlBuilder, "location", latitude + "," + longitude);
        }
        StringUtil.appendIfNotEmpty(urlBuilder, "radius", radius == 0 ? null : String.valueOf(radius));
        StringUtil.appendIfNotEmpty(urlBuilder, "language", language == null ? null : language.getCode());
        StringUtil.appendIfNotEmpty(urlBuilder, "minprice", minPrice == -1 ? null : String.valueOf(minPrice));
        StringUtil.appendIfNotEmpty(urlBuilder, "maxprice", maxPrice == -1 ? null : String.valueOf(maxPrice));
        StringUtil.appendIfNotEmpty(urlBuilder, "opennow", opennow);
        StringUtil.appendIfNotEmpty(urlBuilder, "types", types);
        StringUtil.appendIfNotEmpty(urlBuilder, "pagetoken", pagetoken);

        return urlBuilder.toString();
    }
}
