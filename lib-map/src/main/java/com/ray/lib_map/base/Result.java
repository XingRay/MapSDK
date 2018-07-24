package com.ray.lib_map.base;

/**
 * 数据结果，一般用于io同步操作的结果
 *
 * @author : leixing
 * @date : 2018/5/14
 * Version : 0.0.1
 */
public class Result<T> {
    private boolean isSucceed;
    private T data;
    private int errorCode;
    private String errorMsg;
    private Throwable throwable;

    public boolean succeed() {
        return isSucceed;
    }

    public Result<T> succeed(boolean succeed) {
        isSucceed = succeed;
        return this;
    }

    public T data() {
        return data;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public int errorCode() {
        return errorCode;
    }

    public Result<T> errorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String errorMsg() {
        return errorMsg;
    }

    public Result<T> errorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Throwable throwable() {
        return throwable;
    }

    public Result<T> throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public String toString() {
        return "\"Result\": {"
                + "\"isSucceed\": \"" + isSucceed
                + ", \"data\": \"" + data
                + ", \"errorCode\": \"" + errorCode
                + ", \"errorMsg\": \"" + errorMsg + '\"'
                + ", \"throwable\": \"" + throwable
                + '}';
    }
}
