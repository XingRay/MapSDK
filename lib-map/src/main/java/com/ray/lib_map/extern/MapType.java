package com.ray.lib_map.extern;

import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.coordinate.CoordinateConverter;

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

public class MapType {
    private String name;
    private Set<CoordinateConverter> coordinateConverters;
    private String coordinateType;
    private MapDelegate delegate;

    public MapType(String name) {
        this.name = name;
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

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public MapDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(MapDelegate delegate) {
        this.delegate = delegate;
    }
}
