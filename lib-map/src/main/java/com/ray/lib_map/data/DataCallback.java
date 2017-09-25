package com.ray.lib_map.data;

/**
 * Author      : leixing
 * Date        : 2016-11-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 数据操作回调，如请求数据，提交数据等可能返回数据的接口
 * 操作成功则调用onSuccess,并将返回的数据传入
 * 操作失败则调用onFailure，并传入错误码和错误描述
 */

public interface DataCallback<T> extends FailureCallback {
    /**
     * 操作成功，传入操作返回的数据
     *
     * @param t 返回的数据
     */
    void onSuccess(T t);
}