package com.ray.lib_map.data;

/**
 * Author      : leixing
 * Date        : 2016-11-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 操作失败的回调，用于被其他的错做接口继承
 */

public interface FailureCallback {
    int ERROR_CODE_NO_RESULT = -1;

    /**
     * 失败回调，传入错误码和错误描述
     *
     * @param errorCode
     * @param desc
     */
    void onFailure(int errorCode, String desc);
}
