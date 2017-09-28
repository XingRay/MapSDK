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
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 省份编码
     */
    private String provinceCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 区、县名称
     */
    private String districtName;
    /**
     * 区、县编码
     */
    private String districtCode;
    /**
     * 距离
     */
    private int distance;
    /**
     * 地图点
     */
    private MapPoint mapPoint;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址描述
     */
    private String address;
    /**
     * 地位类型
     */
    private String locationType;

    public Poi() {
    }

    private Poi(Parcel in) {
        this.provinceName = in.readString();
        this.provinceCode = in.readString();
        this.cityName = in.readString();
        this.cityCode = in.readString();
        this.districtName = in.readString();
        this.districtCode = in.readString();
        this.distance = in.readInt();
        this.mapPoint = in.readParcelable(MapPoint.class.getClassLoader());
        this.name = in.readString();
        this.address = in.readString();
        this.locationType = in.readString();
    }

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

    public MapPoint getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
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
                ", mapPoint=" + mapPoint +
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

        if (distance != poi.distance) return false;
        if (provinceName != null ? !provinceName.equals(poi.provinceName) : poi.provinceName != null)
            return false;
        if (provinceCode != null ? !provinceCode.equals(poi.provinceCode) : poi.provinceCode != null)
            return false;
        if (cityName != null ? !cityName.equals(poi.cityName) : poi.cityName != null) return false;
        if (cityCode != null ? !cityCode.equals(poi.cityCode) : poi.cityCode != null) return false;
        if (districtName != null ? !districtName.equals(poi.districtName) : poi.districtName != null)
            return false;
        if (districtCode != null ? !districtCode.equals(poi.districtCode) : poi.districtCode != null)
            return false;
        if (mapPoint != null ? !mapPoint.equals(poi.mapPoint) : poi.mapPoint != null) return false;
        if (name != null ? !name.equals(poi.name) : poi.name != null) return false;
        if (address != null ? !address.equals(poi.address) : poi.address != null) return false;
        return locationType != null ? locationType.equals(poi.locationType) : poi.locationType == null;

    }

    @Override
    public int hashCode() {
        int result = provinceName != null ? provinceName.hashCode() : 0;
        result = 31 * result + (provinceCode != null ? provinceCode.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (districtName != null ? districtName.hashCode() : 0);
        result = 31 * result + (districtCode != null ? districtCode.hashCode() : 0);
        result = 31 * result + distance;
        result = 31 * result + (mapPoint != null ? mapPoint.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (locationType != null ? locationType.hashCode() : 0);
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
        dest.writeParcelable(this.mapPoint, flags);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.locationType);
    }

    public double getLatitude() {
        return getMapPoint().getLatitude();
    }

    public double getLongitude() {
        return getMapPoint().getLongitude();
    }
}
