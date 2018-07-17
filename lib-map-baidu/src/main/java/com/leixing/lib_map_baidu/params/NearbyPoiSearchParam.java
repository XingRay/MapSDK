package com.leixing.lib_map_baidu.params;

import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-10-31
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class NearbyPoiSearchParam {
    private String query;
    private List<String> tags;
    private double latitude;
    private double longitude;
    private double radius;
    private boolean radiusLimit;
    private int pageSize;
    private int pageIndex;
    private String key;
    private String code;

    public NearbyPoiSearchParam(double latitude, double longitude, double radius, int pageSize, int pageIndex, String key, String code) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.key = key;
        this.code = code;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isRadiusLimit() {
        return radiusLimit;
    }

    public void setRadiusLimit(boolean radiusLimit) {
        this.radiusLimit = radiusLimit;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "NearbyPoiSearchParam{" +
                "query='" + query + '\'' +
                ", tags=" + tags +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", radiusLimit=" + radiusLimit +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", key='" + key + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        return "http://api.map.baidu.com/place/v2/search?"
                + "query=" + query
                + "&location=" + latitude + "," + longitude
                + "&radius=" + radius
                + "&output=json"
                + "&ak=" + key
                + "&scope=" + 2
                + "&coord_type=" + 3
                + "&page_size" + pageSize
                + "&page_num" + pageIndex
                + "&mcode=" + code;
    }
}
