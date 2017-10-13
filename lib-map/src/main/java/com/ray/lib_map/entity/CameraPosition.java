package com.ray.lib_map.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author      : leixing
 * Date        : 2017-09-30
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class CameraPosition implements Parcelable, Serializable {
    public static final Parcelable.Creator<CameraPosition> CREATOR = new Parcelable.Creator<CameraPosition>() {
        @Override
        public CameraPosition createFromParcel(Parcel source) {
            return new CameraPosition(source);
        }

        @Override
        public CameraPosition[] newArray(int size) {
            return new CameraPosition[size];
        }
    };
    /**
     * center point of camera
     */
    private MapPoint position;
    /**
     * zoom level, 1-20
     */
    private float zoom;
    /**
     * rotate angle of map, -180~180
     */
    private float rotate;
    /**
     * overlook angle, 0-45
     */
    private float overlook;

    public CameraPosition() {
    }

    private CameraPosition(Parcel in) {
        this.position = in.readParcelable(MapPoint.class.getClassLoader());
        this.zoom = in.readFloat();
        this.rotate = in.readFloat();
        this.overlook = in.readFloat();
    }

    public MapPoint getPosition() {
        return position;
    }

    public void setPosition(MapPoint position) {
        this.position = position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public float getOverlook() {
        return overlook;
    }

    public void setOverlook(float overlook) {
        this.overlook = overlook;
    }

    @Override
    public String toString() {
        return "MapStatus{" +
                "cameraPosition=" + position +
                ", zoom=" + zoom +
                ", rotate=" + rotate +
                ", overlook=" + overlook +
                '}';
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CameraPosition cameraPosition = (CameraPosition) o;

        if (Float.compare(cameraPosition.zoom, zoom) != 0) return false;
        if (Float.compare(cameraPosition.rotate, rotate) != 0) return false;
        if (Float.compare(cameraPosition.overlook, overlook) != 0) return false;
        return position.equals(cameraPosition.position);

    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + (zoom != +0.0f ? Float.floatToIntBits(zoom) : 0);
        result = 31 * result + (rotate != +0.0f ? Float.floatToIntBits(rotate) : 0);
        result = 31 * result + (overlook != +0.0f ? Float.floatToIntBits(overlook) : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.position, flags);
        dest.writeFloat(this.zoom);
        dest.writeFloat(this.rotate);
        dest.writeFloat(this.overlook);
    }
}
