package com.ray.lib_map.impl.google;


import com.ray.lib_map.data.FailureCallback;

/**
 * @author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface ConnectListener extends FailureCallback {
    void onSuccess();
}
