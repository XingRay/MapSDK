package com.ray.lib_map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.entity.polyline.PolyLine;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapLongClickListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MarkerClickListener;

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
    void setMapLoadListener(MapLoadListener listener);

    void setCameraMoveListener(CameraMoveListener listener);

    void setMarkerClickListener(MarkerClickListener listener);

    void setInfoWindowClickListener(InfoWindowClickListener listener);

    void setMapScreenCaptureListener(MapScreenCaptureListener listener);

    void setMapClickListener(MapClickListener listener);

    void setMapLongClickListener(MapLongClickListener listener);

    // ===  gesture === //

    boolean isZoomGestureEnable();

    void setZoomGestureEnable(boolean enable);

    boolean isScrollGestureEnable();

    void setScrollGestureEnable(boolean enable);

    boolean isRotateGestureEnable();

    void setRotateGestureEnable(boolean enable);

    boolean isOverlookGestureEnable();

    void setOverlookGestureEnable(boolean enable);

    // == map status == //

    float getZoom();

    void setZoom(float zoom);

    float getOverlook();

    void setOverlook(float overlook);

    float getRotate();

    void setRotate(float rotate);

    MapPoint getPosition();

    void setPosition(MapPoint mapPoint);

    CameraPosition getCameraPosition();

    void setCameraPosition(CameraPosition position);

    // === zoom === //

    void zoomOut();

    void zoomIn();

    float getMaxZoom();

    float getMinZoom();

    // ========== basic function =========//

    void screenShotAndSave(String saveFilePath);

    void moveByBounds(List<MapPoint> points, int padding);

    void moveByPolygon(Polygon polygon, int padding);

    void moveByCircle(Circle circle, int padding);

    MapPoint graphicPointToMapPoint(Point point);

    Point mapPointToGraphicPoint(MapPoint point);

    // ========== marker ========== //

    void addMarker(MapMarker mapMarker);

    void removeMarker(MapMarker mapMarker);

    List<MapMarker> getMarkers();

    void clearMarkers();

    void setMarkerVisible(MapMarker mapMarker, boolean visible);

    void setInfoWindowInflater(InfoWindowInflater inflater);

    void hideInfoWindow();

    void showInfoWindow(MapMarker mapMarker);

    // ========== overlay ========= //
    void addOverlay(MapOverlay overlay);

    void removeOverlay(MapOverlay overlay);

    void clearOverlay();

    void moveOverlay(MapOverlay overlay, MapPoint toPoint);

    void updateOverlay(MapOverlay overlay, float xOffset, float yOffset);

    MapOverlay getOverlay(MapPoint mapPoint);

    List<MapOverlay> getOverlays();

    // == polyline == //
    void addPolyline(PolyLine polyLine);

    void removePolyline(PolyLine p);

    // == circle == //
    void addCircle(Circle circle);

    void removeCircle(Circle circle);

    // ========== polygon ========== //
    Polygon addPolygon(Polygon polygon);

    void removePolygon(Polygon p);
}