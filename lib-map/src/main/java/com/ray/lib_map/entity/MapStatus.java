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

public class MapStatus implements Parcelable, Serializable {
    public static final Parcelable.Creator<MapStatus> CREATOR = new Parcelable.Creator<MapStatus>() {
        @Override
        public MapStatus createFromParcel(Parcel source) {
            return new MapStatus(source);
        }

        @Override
        public MapStatus[] newArray(int size) {
            return new MapStatus[size];
        }
    };
    /**
     * center point of camera
     */
    private MapPoint cameraPosition;
    /**
     * zoom level, 1-20
     */
    private float zoom;
    /**
     * rotate angle of map, -180~180
     */
    private float rotate;
    /**
     * overlook angle, -45~0
     */
    private float overlook;

    public MapStatus() {
    }

    protected MapStatus(Parcel in) {
        this.cameraPosition = in.readParcelable(MapPoint.class.getClassLoader());
        this.zoom = in.readFloat();
        this.rotate = in.readFloat();
        this.overlook = in.readFloat();
    }

    public MapPoint getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(MapPoint cameraPosition) {
        this.cameraPosition = cameraPosition;
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
                "cameraPosition=" + cameraPosition +
                ", zoom=" + zoom +
                ", rotate=" + rotate +
                ", overlook=" + overlook +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapStatus mapStatus = (MapStatus) o;

        if (Float.compare(mapStatus.zoom, zoom) != 0) return false;
        if (Float.compare(mapStatus.rotate, rotate) != 0) return false;
        if (Float.compare(mapStatus.overlook, overlook) != 0) return false;
        return cameraPosition.equals(mapStatus.cameraPosition);

    }

    @Override
    public int hashCode() {
        int result = cameraPosition.hashCode();
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
        dest.writeParcelable(this.cameraPosition, flags);
        dest.writeFloat(this.zoom);
        dest.writeFloat(this.rotate);
        dest.writeFloat(this.overlook);
    }
}
