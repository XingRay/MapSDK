package com.ray.lib_map.entity;

/**
 * Author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PoiSearchSuggestion {
    private String cityCode;
    private String cityName;
    private int suggestionNum;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getSuggestionNum() {
        return suggestionNum;
    }

    public void setSuggestionNum(int suggestionNum) {
        this.suggestionNum = suggestionNum;
    }

    @Override
    public String toString() {
        return "PoiSearchSuggestion{" +
                "cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", suggestionNum=" + suggestionNum +
                '}';
    }
}
