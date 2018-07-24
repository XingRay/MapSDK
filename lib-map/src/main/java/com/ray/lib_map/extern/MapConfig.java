package com.ray.lib_map.extern;

import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.base.Factory;
import com.ray.lib_map.coordinate.CoordinateConverter;
import com.ray.lib_map.coordinate.CoordinateConverterManager;
import com.ray.lib_map.data.MapDataSource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : leixing
 * Date        : 2017-09-22
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapConfig {
    private String name;
    private Set<CoordinateConverter> coordinateConverters;
    private String coordinateType;
    private Factory<MapDelegate> delegateFactory;
    private Factory<MapDataSource> dataSourceFactory;

    public MapConfig() {
        coordinateConverters = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CoordinateConverter> getCoordinateConverters() {
        return coordinateConverters;
    }

    public void setCoordinateConverters(Set<CoordinateConverter> coordinateConverters) {
        this.coordinateConverters = coordinateConverters;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public MapConfig setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
        return this;
    }

    public Factory<MapDelegate> getDelegateFactory() {
        return delegateFactory;
    }

    public MapConfig setDelegateFactory(Factory<MapDelegate> delegateFactory) {
        this.delegateFactory = delegateFactory;
        return this;
    }

    public Factory<MapDataSource> getDataSourceFactory() {
        return dataSourceFactory;
    }

    public MapConfig setDataSourceFactory(Factory<MapDataSource> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        return this;
    }

    public MapConfig addConverter(CoordinateConverter converter) {
        CoordinateConverterManager.getInstance().addConverter(converter);
        return this;
    }
}
