package com.ray.lib_map.listener;


/**
 * @author : leixing
 * Date        : 2017-10-22
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface MapSwitchListener {
    void onMapSwitch();

    void onFailure(int errorCode, String errorMsg);
}
