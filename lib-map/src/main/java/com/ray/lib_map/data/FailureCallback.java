package com.ray.lib_map.data;

/**
 * @author      : leixing
 * Date        : 2016-11-25
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 操作失败的回调，用于被其他的错做接口继承
 */

public interface FailureCallback {
    int ERROR_CODE_NO_RESULT = -1;
    int ERROR_CODE_PERMISSION_DENY = -2;
    int ERROR_CODE_MAP_LOAD_TIMEOUT = -3;
    int ERROR_CODE_CONNECT_FAILED = -4;

    /**
     * 失败回调，传入错误码和错误描述
     *
     * @param errorCode 错误码
     * @param desc 错误描述
     */
    void onFailure(int errorCode, String desc);
}
