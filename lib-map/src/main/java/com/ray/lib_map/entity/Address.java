package com.ray.lib_map.entity;

/**
 * Author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地址
 */

public class Address {
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    /**
     * 省/直辖市的名称
     */
    private String province;

    /**
     * 城市的名称
     */
    private String city;

    /**
     * 城市的编码
     */
    private String cityCode;

    /**
     * 区/县的名称
     */
    private String district;

    /**
     * 区/县的编码
     */
    private String districtCode;

    /**
     * 地址名称
     */
    private String name;

    /**
     * 格式化后的地址
     */
    private String formattedAddress;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @Override
    public String toString() {
        return "Address{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", district='" + district + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", name='" + name + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                '}';
    }

    public MapPoint getMapPoint(){
        return new MapPoint(latitude, longitude);
    }
}
