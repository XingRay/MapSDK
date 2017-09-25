package com.ray.lib_map;

import android.view.View;

import com.ray.lib_map.entity.MapMarker;


/**
 * Author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface MarkerInflater {
    View inflateMarker(MapMarker marker);
}
