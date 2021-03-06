package com.ray.lib_map.data;


import com.ray.lib_map.base.Result;
import com.ray.lib_map.base.Result2;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;

import java.util.List;

/**
 * @author : leixing
 * Date        : 2017-07-12
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 地图数据源
 */
@SuppressWarnings("unused")
public interface MapDataSource {
    int ERROR_CODE_NO_RESULT = 1;
    int ERROR_CODE_POI_SUGGESTION = 2;

    /**
     * 逆地理编码
     * <p>
     * 从已知的经纬度坐标到对应的地址描述（如行政区划、街区、楼层、房间等）的转换。
     * 常用于根据定位的坐标来获取该地点的位置详细信息
     * <p>
     * 如：
     * (latitude, longitude) -> "xx省xx市xx区(县)"
     */
    Result<Address> reverseGeoCode(MapPoint mapPoint, float radius);

    /**
     * 地理编码，又称为地址匹配，
     * 是从已知的结构化地址描述到对应的经纬度坐标的转换过程。该功能适用于根据用户输入的地址确认用户具体位置的场景，
     * 常用于配送人员根据用户输入的具体地址找地点。
     * <p>
     * 结构化地址的定义：
     * 首先，地址肯定是一串字符，内含国家、省份、城市、城镇、乡村、街道、门牌号码、屋邨、大厦等建筑物名称。
     * 按照由大区域名称到小区域名称组合在一起的字符。
     *
     * @param address 结构化地址
     * @param city    查询城市名称、城市编码或行政区划代码
     */
    Result<MapPoint> geoCode(String address, String city);

    /**
     * 搜索POI列表
     *
     * @param mapPoint    坐标点
     * @param searchBound 搜索范围
     * @param pageIndex   页码
     * @param pageSize    页大小
     */
    Result2<List<Poi>, List<PoiSearchSuggestion>> queryPoi(MapPoint mapPoint, int searchBound, int pageIndex, int pageSize);

    /**
     * 搜索POI列表
     *
     * @param keyword   关键字
     * @param city      城市
     * @param pageIndex 页码
     * @param pageSize  页大小
     */
    Result2<List<Poi>, List<PoiSearchSuggestion>> queryPoi(String keyword, String city, int pageIndex, int pageSize);

    /**
     * 定位
     *
     * @return 位置
     */
    Result<Address> locate();
}
