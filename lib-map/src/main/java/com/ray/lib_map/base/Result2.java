package com.ray.lib_map.base;

/**
 * @author : leixing
 * @date : 2018/7/24 14:11
 * <p>
 * description : xxx
 */
public class Result2<T0, T1> {
    private boolean isSucceed;
    private T0 data0;
    private T1 data1;
    private int errorCode;
    private String errorMsg;
    private Throwable throwable;


    public boolean succeed() {
        return isSucceed;
    }

    public Result2<T0, T1> succeed(boolean succeed) {
        isSucceed = succeed;
        return this;
    }

    public T0 data0() {
        return data0;
    }

    public Result2<T0, T1> data0(T0 data0) {
        this.data0 = data0;
        return this;
    }

    public T1 data1() {
        return data1;
    }

    public Result2<T0, T1> data1(T1 data1) {
        this.data1 = data1;
        return this;
    }

    public int errorCode() {
        return errorCode;
    }

    public Result2<T0, T1> errorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String errorMsg() {
        return errorMsg;
    }

    public Result2<T0, T1> errorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Throwable throwable() {
        return throwable;
    }

    public Result2<T0, T1> throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public String toString() {
        return "\"Result2\": {"
                + "\"isSucceed\": \"" + isSucceed
                + ", \"data0\": \"" + data0
                + ", \"data1\": \"" + data1
                + ", \"errorCode\": \"" + errorCode
                + ", \"errorMsg\": \"" + errorMsg + '\"'
                + ", \"throwable\": \"" + throwable
                + '}';
    }
}
