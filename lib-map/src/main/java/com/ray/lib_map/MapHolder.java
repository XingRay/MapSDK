package com.ray.lib_map;

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

public interface MapHolder {

    View inflateMapView();

    View getMapView();

    void clearMap();

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

    // ========== basic function =========//
    void setGestureEnable(boolean enable);

    void screenShotAndSave(String saveFilePath);

    void animateTo(MapPoint mapPoint, MapViewInterface.AnimationListener listener);

    void moveTo(MapPoint point, boolean isSmooth, float zoom);

    void moveByBounds(List<MapPoint> points, int padding);

    void moveByPolygon(Polygon polygon, int padding);

    void moveByCircle(Circle circle, int padding);

    void setZoomGestureEnable(boolean enable);

    MapPoint getCameraPosition();

    // ========== zoom ========== //
    void setZoomControlsEnabled(boolean enabled);

    void zoomTo(float zoom);

    void zoomOut();

    void zoomIn();

    float getCurrentZoom();

    float getMaxZoomLevel();

    float getMinZoomLevel();


    // ========== marker and overlay ========= //

    void setMarkerInflater(MarkerInflater inflater);

    void addOverlay(MapOverlay overlay);

    void addOverlays(List<MapOverlay> overlays);

    void removeOverlay(MapOverlay overlay);

    void clearOverlay();

    void moveOverlay(MapOverlay overlay, MapPoint toPoint);

    void updateOverlay(MapOverlay overlay, float xOffset, float yOffset);

    MapOverlay getOverlay(MapPoint mapPoint);

    List<MapOverlay> getOverlays();

    // ========== marker ========== //

    void addMarker(MapMarker mapMarker);

    void clearMarker();

    void setMarkerVisible(MapMarker mapMarker, boolean visible);

    // ========== polygon and polyline ========== //
    Polygon addPolygon(Polygon polygon);

    void removePolygon(Polygon p);

    void addPolyline(MapLine mapLine);

    void addPolylines(MapLine... mapLines);

    void removePolyline(MapLine p);

    void addCircle(Circle circle);

    void removeCircle(Circle circle);

    void removeMarker(MapMarker mapMarker);

    void addMarkers(List<MapMarker> mapMarkers);
}