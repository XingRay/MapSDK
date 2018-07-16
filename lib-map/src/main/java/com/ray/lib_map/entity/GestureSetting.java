package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author      : leixing
 * Date        : 2017-10-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class GestureSetting implements Parcelable, Serializable {
    private boolean zoomGestureEnable;
    private boolean scrollGestureEnable;
    private boolean rotateGestureEnable;
    private boolean overlookGestureEnable;

    public GestureSetting() {
    }

    protected GestureSetting(Parcel in) {
        this.zoomGestureEnable = in.readByte() != 0;
        this.scrollGestureEnable = in.readByte() != 0;
        this.rotateGestureEnable = in.readByte() != 0;
        this.overlookGestureEnable = in.readByte() != 0;
    }

    public boolean isZoomGestureEnable() {
        return zoomGestureEnable;
    }

    public void setZoomGestureEnable(boolean zoomGestureEnable) {
        this.zoomGestureEnable = zoomGestureEnable;
    }

    public boolean isScrollGestureEnable() {
        return scrollGestureEnable;
    }

    public void setScrollGestureEnable(boolean scrollGestureEnable) {
        this.scrollGestureEnable = scrollGestureEnable;
    }

    public boolean isRotateGestureEnable() {
        return rotateGestureEnable;
    }

    public void setRotateGestureEnable(boolean rotateGestureEnable) {
        this.rotateGestureEnable = rotateGestureEnable;
    }

    public boolean isOverlookGestureEnable() {
        return overlookGestureEnable;
    }

    public void setOverlookGestureEnable(boolean overlookGestureEnable) {
        this.overlookGestureEnable = overlookGestureEnable;
    }

    @Override
    public String toString() {
        return "GestureSetting{" +
                "zoomGestureEnable=" + zoomGestureEnable +
                ", scrollGestureEnable=" + scrollGestureEnable +
                ", rotateGestureEnable=" + rotateGestureEnable +
                ", overlookGestureEnable=" + overlookGestureEnable +
                '}';
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GestureSetting setting = (GestureSetting) o;

        if (zoomGestureEnable != setting.zoomGestureEnable) return false;
        if (scrollGestureEnable != setting.scrollGestureEnable) return false;
        if (rotateGestureEnable != setting.rotateGestureEnable) return false;
        return overlookGestureEnable == setting.overlookGestureEnable;

    }

    @Override
    public int hashCode() {
        int result = (zoomGestureEnable ? 1 : 0);
        result = 31 * result + (scrollGestureEnable ? 1 : 0);
        result = 31 * result + (rotateGestureEnable ? 1 : 0);
        result = 31 * result + (overlookGestureEnable ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.zoomGestureEnable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.scrollGestureEnable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.rotateGestureEnable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.overlookGestureEnable ? (byte) 1 : (byte) 0);
    }

    public static final Creator<GestureSetting> CREATOR = new Creator<GestureSetting>() {
        @Override
        public GestureSetting createFromParcel(Parcel source) {
            return new GestureSetting(source);
        }

        @Override
        public GestureSetting[] newArray(int size) {
            return new GestureSetting[size];
        }
    };
}
