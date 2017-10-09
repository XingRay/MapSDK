package com.ray.lib_map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface MapDelegate {

    View getMapView();

    void clearMap();

    void onSwitchOut();

    void onSwitchIn(Bundle savedInstanceState);

    // ========== lifecycle ========== //
    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();

    void onSaveInstanceState(Bundle savedInstanceState);

    // ========== listener ========== //
    void setMapLoadListener(MapViewInterface.MapLoadListener listener);

    void setCameraMoveListener(MapViewInterface.CameraMoveListener listener);

    void setAnimationListener(MapViewInterface.AnimationListener listener);

    void setMarkerClickListener(MapViewInterface.MarkerClickListener listener);

    void setInfoWindowClickListener(MapViewInterface.InfoWindowClickListener listener);

    void setMapScreenCaptureListener(MapViewInterface.MapScreenCaptureListener listener);

    // ========== zoom ========== //
    void setZoomControlEnable(boolean enable);

    void zoomTo(float zoom);

    void zoomOut();

    void zoomIn();

    float getCurrentZoom();

    float getMaxZoomLevel();

    float getMinZoomLevel();

    // ========== basic function =========//
    void setGestureEnable(boolean enable);

    void screenShotAndSave(String saveFilePath);

    void animateTo(MapPoint mapPoint, float zoom, MapViewInterface.AnimationListener listener);

    void moveTo(MapPoint point, boolean isSmooth, float zoom);

    void moveByBounds(List<MapPoint> points, int padding);

    void moveByPolygon(Polygon polygon, int padding);

    void moveByCircle(Circle circle, int padding);

    void setZoomGestureEnable(boolean enable);

    MapPoint getCameraPosition();

    MapPoint fromScreenLocation(Point point);

    Point toScreenLocation(MapPoint point);

    // ========== marker ========== //

    void addMarker(MapMarker mapMarker);

    void removeMarker(MapMarker mapMarker);

    void setMarkerVisible(MapMarker mapMarker, boolean visible);

    // ========== overlay ========= //
    void addOverlay(MapOverlay overlay);

    void removeOverlay(MapOverlay overlay);

    void clearOverlay();

    void moveOverlay(MapOverlay overlay, MapPoint toPoint);

    void updateOverlay(MapOverlay overlay, float xOffset, float yOffset);

    MapOverlay getOverlay(MapPoint mapPoint);

    List<MapOverlay> getOverlays();

    // == polyline == //
    void addPolyline(MapLine mapLine);

    void removePolyline(MapLine p);

    // == circle == //
    void addCircle(Circle circle);

    void removeCircle(Circle circle);

    // ========== polygon ========== //
    Polygon addPolygon(Polygon polygon);

    void removePolygon(Polygon p);
}