package com.ray.mapsdk.pages.poi;

import com.ray.lib_map.entity.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class PoiWrapper {
    private Poi poi;
    private boolean isSelected;

    public PoiWrapper(Poi poi) {
        this(poi, false);
    }

    public PoiWrapper(Poi poi, boolean isSelected) {
        this.poi = poi;
        this.isSelected = isSelected;
    }

    public static List<PoiWrapper> fromPois(List<Poi> pois) {
        List<PoiWrapper> poiWrappers = new ArrayList<>();
        if (pois != null) {
            for (Poi poi : pois) {
                poiWrappers.add(new PoiWrapper(poi));
            }
        }
        return poiWrappers;
    }

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "PoiWrapper{" +
                "poi=" + poi +
                ", isSelected=" + isSelected +
                '}';
    }
}
