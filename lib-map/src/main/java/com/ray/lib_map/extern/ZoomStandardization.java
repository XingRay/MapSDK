package com.ray.lib_map.extern;

/**
 * Author      : leixing
 * Date        : 2017-10-11
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 标准化
 */
//因子
public class ZoomStandardization {
    // log(2, 14.5)
    private static final float GAODE_ZOOM_FACTOR = 3.857980995f;

    private ZoomStandardization() {
        throw new UnsupportedOperationException();
    }

    public static float toStandardZoom(float zoom, MapType mapType) {
        switch (mapType) {
            case GAODE:
                return gaodeZoomToStandardZoom(zoom);
            case BAIDU:
                return baiduZoomToStandardZoom(zoom);
            case GOOGLE:
                return googleZoomToStandardZoom(zoom);
            default:
                throw new IllegalArgumentException("unknown map type");
        }
    }

    public static float fromStandardZoom(float zoom, MapType mapType) {
        switch (mapType) {
            case GAODE:
                return standardZoomToGaodeZoom(zoom);
            case BAIDU:
                return standardZoomToBaiduZoom(zoom);
            case GOOGLE:
                return standardZoomToGoogleZoom(zoom);
            default:
                throw new IllegalArgumentException("unknown map type");
        }
    }


    private static float gaodeZoomToStandardZoom(float zoom) {
        return zoom + 5 - GAODE_ZOOM_FACTOR;
    }

    private static float standardZoomToGaodeZoom(float zoom) {
        return zoom - 5 + GAODE_ZOOM_FACTOR;
    }

    private static float baiduZoomToStandardZoom(float zoom) {
        return zoom;
    }

    private static float standardZoomToBaiduZoom(float zoom) {
        return zoom;
    }

    private static float googleZoomToStandardZoom(float zoom) {
        return zoom + 1.7f;
    }

    private static float standardZoomToGoogleZoom(float zoom) {
        return zoom - 1.7f;
    }
}
