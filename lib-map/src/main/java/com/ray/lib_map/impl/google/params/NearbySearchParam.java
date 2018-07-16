package com.ray.lib_map.impl.google.params;


import com.ray.lib_map.util.StringUtil;

/**
 * @author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("unused")
public class NearbySearchParam {
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
     * 半径（米） 最大半径50000米
     */
    private int radius;

    /**
     * 与 Google 为此地点编入索引的所有内容进行匹配的词语，
     * 包括但不仅限于名称、类型和地址，以及客户评论和其他第三方内容
     */
    private String keyword;

    private Language language;

    /**
     * 将结果仅限制为指定范围内的地点。有效值的范围在 0（最实惠）和 4（最昂贵）之间，包括 0 和 4 本身。
     * 特定值所表示的准确数量因区域而异。
     */
    private int minPrice = -1;

    private int maxPrice = -1;

    /**
     * 与 Google 为此地点编入索引的所有内容匹配时所对照的字词。相当于 keyword。name 字段不再受限于地点名称。
     * 此字段中的值与 keyword 字段中的值相结合，作为同一搜索字符串的一部分进行传递。我们建议所有搜索字词仅使用
     * keyword 参数。
     */
    private String name;

    /**
     * 仅返回发送查询时营业的地点。如果您在查询中包含此参数，就不会返回在 Google Places 数据库中未指定开放时间
     * 的地点。
     */
    private String openNow;

    /**
     * 指定结果列出的顺序。请注意，如果指定 radius（见上面所需参数部分中的描述），就不能添加 rankby
     * 可能的值为：
     * prominence（默认）。此选项根据重要性对结果排序。优先列出指定区域的知名地点。知名度受 Google 索引中
     * 地点排序、全球知名度和其他因素影响。
     * distance。此选项按其与指定的 location 之间的距离以升序偏向搜索结果。当指定 distance 时，需要提供
     * keyword、name 或 type 中的一个或多个参数。
     */
    private String rankBy;

    /**
     * 将结果限制为与指定类型匹配的地点。只能指定一个类型（如果提供了多个类型，系统会忽略第一项之后的所有类型）
     */
    private String types;

    /**
     * 返回上次所运行的搜索的后续 20 个结果。设置 pagetoken 参数将用上次使用的同一参数执行搜索 – 将忽略
     * 除 pagetoken 之外的所有参数。
     */
    private String pageToken;

    public NearbySearchParam(String key, double latitude, double longitude/*, int radius*/) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
//        this.radius = radius;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getRankBy() {
        return rankBy;
    }

    @SuppressWarnings("SameParameterValue")
    public void setRankBy(String rankBy) {
        this.rankBy = rankBy;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

    @Override
    public String toString() {
        return "LocationSearchParam{" +
                "key='" + key + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", keyword='" + keyword + '\'' +
                ", language=" + language +
                ", minprice=" + minPrice +
                ", maxprice=" + maxPrice +
                ", name='" + name + '\'' +
                ", opennow='" + openNow + '\'' +
                ", rankby='" + rankBy + '\'' +
                ", types='" + types + '\'' +
                ", pagetoken='" + pageToken + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                .append("location=").append(latitude).append(",").append(longitude)
                .append("&key=").append(key)
                /*.append("&radius=").append(radius)*/;

        StringUtil.appendIfNotEmpty(urlBuilder, "keyword", keyword);
        StringUtil.appendIfNotEmpty(urlBuilder, "language", language == null ? null : language.getCode());
        StringUtil.appendIfNotEmpty(urlBuilder, "minprice", minPrice == -1 ? null : String.valueOf(minPrice));
        StringUtil.appendIfNotEmpty(urlBuilder, "maxprice", maxPrice == -1 ? null : String.valueOf(maxPrice));
        StringUtil.appendIfNotEmpty(urlBuilder, "name", name);
        StringUtil.appendIfNotEmpty(urlBuilder, "opennow", openNow);
        StringUtil.appendIfNotEmpty(urlBuilder, "rankby", rankBy);
        StringUtil.appendIfNotEmpty(urlBuilder, "types", types);
        StringUtil.appendIfNotEmpty(urlBuilder, "pagetoken", pageToken);

        return urlBuilder.toString();
    }
}
