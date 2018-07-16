package com.ray.lib_map;

import android.view.View;

import com.ray.lib_map.entity.MapMarker;


/**
 * @author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface InfoWindowInflater {
    View inflate(MapMarker marker);
}
