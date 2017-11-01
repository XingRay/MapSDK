package com.ray.lib_map.impl.baidu.params;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-10-31
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class RegionPoiSearchParam {
    private String query;
    private List<String> tags;
    private String region;
    private boolean city_limit;
    private int pageSize;
    //form 0
    private int pageIndex;
    private String key;
    private String code;

    public RegionPoiSearchParam(String query, String region, int pageSize, int pageIndex, String key, String code) {
        this.query = query;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isCity_limit() {
        return city_limit;
    }

    public void setCity_limit(boolean city_limit) {
        this.city_limit = city_limit;
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
        return "RegionPoiSearchParam{" +
                "query='" + query + '\'' +
                ", tags=" + tags +
                ", region='" + region + '\'' +
                ", city_limit=" + city_limit +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", key='" + key + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String buildRequestUrl() {
        return "http://api.map.baidu.com/place/v2/search?"
                + "query=" + query
                + "&region" + region
                + "&page_size=" + pageSize
                + "&page_num=" + pageIndex
                + "&scope=" + 2
                + "&output=json"
                + "&ak=" + key
                + "&mcode=" + code;
    }
}
