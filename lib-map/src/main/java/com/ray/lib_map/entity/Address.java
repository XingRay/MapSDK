package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author      : leixing
 * Date        : 2017-07-12
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地址
 */

@SuppressWarnings("unused")
public class Address implements Parcelable, Serializable {
    /**
     * 地图点
     */
    private MapPoint mapPoint;

    /**
     * 国家
     */
    private String country;

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

    public Address() {
    }

    public MapPoint getMapPoint() {
        return this.mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
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
                "mapPoint=" + mapPoint +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", district='" + district + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", name='" + name + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                '}';
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!mapPoint.equals(address.mapPoint)) return false;
        if (country != null ? !country.equals(address.country) : address.country != null)
            return false;
        if (province != null ? !province.equals(address.province) : address.province != null)
            return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (cityCode != null ? !cityCode.equals(address.cityCode) : address.cityCode != null)
            return false;
        if (district != null ? !district.equals(address.district) : address.district != null)
            return false;
        if (districtCode != null ? !districtCode.equals(address.districtCode) : address.districtCode != null)
            return false;
        if (name != null ? !name.equals(address.name) : address.name != null) return false;
        return formattedAddress != null ? formattedAddress.equals(address.formattedAddress) : address.formattedAddress == null;

    }

    @Override
    public int hashCode() {
        int result = mapPoint.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (districtCode != null ? districtCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (formattedAddress != null ? formattedAddress.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mapPoint, flags);
        dest.writeString(this.country);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.cityCode);
        dest.writeString(this.district);
        dest.writeString(this.districtCode);
        dest.writeString(this.name);
        dest.writeString(this.formattedAddress);
    }

    protected Address(Parcel in) {
        this.mapPoint = in.readParcelable(MapPoint.class.getClassLoader());
        this.country = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.cityCode = in.readString();
        this.district = in.readString();
        this.districtCode = in.readString();
        this.name = in.readString();
        this.formattedAddress = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
