package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class Poi implements Parcelable {
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String districtName;
    private String districtCode;
    private int distance;
    private double latitude;
    private double longitude;
    private String name;
    private String address;
    private String locationType;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "provinceName='" + provinceName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", districtName='" + districtName + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", distance=" + distance +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", locationType='" + locationType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poi poi = (Poi) o;

        if (name != null ? !name.equals(poi.name) : poi.name != null) return false;
        return address != null ? address.equals(poi.address) : poi.address == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provinceName);
        dest.writeString(this.provinceCode);
        dest.writeString(this.cityName);
        dest.writeString(this.cityCode);
        dest.writeString(this.districtName);
        dest.writeString(this.districtCode);
        dest.writeInt(this.distance);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.locationType);
    }

    public Poi() {
    }

    protected Poi(Parcel in) {
        this.provinceName = in.readString();
        this.provinceCode = in.readString();
        this.cityName = in.readString();
        this.cityCode = in.readString();
        this.districtName = in.readString();
        this.districtCode = in.readString();
        this.distance = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.name = in.readString();
        this.address = in.readString();
        this.locationType = in.readString();
    }

    public static final Creator<Poi> CREATOR = new Creator<Poi>() {
        @Override
        public Poi createFromParcel(Parcel source) {
            return new Poi(source);
        }

        @Override
        public Poi[] newArray(int size) {
            return new Poi[size];
        }
    };

    public MapPoint getPoint() {
        return new MapPoint(latitude, longitude);
    }
}
