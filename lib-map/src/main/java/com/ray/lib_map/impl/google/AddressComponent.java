package com.ray.lib_map.impl.google;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class AddressComponent {
    private String longName;
    private String shortName;
    private List<String> types;

    public AddressComponent() {
        types = new ArrayList<>();
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void addType(String type) {
        if (!TextUtils.isEmpty(type)) {
            types.add(type);
        }
    }

    public boolean containsType(String type) {
        return types.contains(type);
    }

    @Override
    public String toString() {
        return "AddressComponent{" +
                "longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", types=" + types +
                '}';
    }
}
