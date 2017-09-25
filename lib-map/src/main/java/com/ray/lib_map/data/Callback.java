package com.ray.lib_map.data;

/**
 * Author      : leixing
 * Date        : 2016-11-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 异步操作的回调，如删除操作
 * 操作成功则调用onSuccess
 * 操作失败则调用onFailure，并返回错误码和错误描述
 */

public interface Callback extends FailureCallback {
    void onSuccess();
}