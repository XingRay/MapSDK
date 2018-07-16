package com.ray.lib_map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.graph.Graph;
import com.ray.lib_map.entity.polyline.Polyline;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapLongClickListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MapSwitchListener;
import com.ray.lib_map.listener.MarkerClickListener;

import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface MapDelegate {

    // ========= basic ===========//

    View getMapView();

    void clearMap();

    void onSwitchOut();

    void onSwitchIn(Bundle savedInstanceState, MapSwitchListener listener);

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

    void setMapClickListener(MapClickListener listener);

    void setMapLongClickListener(MapLongClickListener listener);

    // ===========  gesture ============ //

    boolean isZoomGestureEnable();

    void setZoomGestureEnable(boolean enable);

    boolean isScrollGestureEnable();

    void setScrollGestureEnable(boolean enable);

    boolean isRotateGestureEnable();

    void setRotateGestureEnable(boolean enable);

    boolean isOverlookGestureEnable();

    void setOverlookGestureEnable(boolean enable);

    // ======= camera ======== //

    MapPoint getPosition();

    void setPosition(MapPoint mapPoint);

    float getZoom();

    void setZoom(float zoom);

    void zoomOut();

    void zoomIn();

    float getMaxZoom();

    float getMinZoom();

    float getOverlook();

    void setOverlook(float overlook);

    float getRotate();

    void setRotate(float rotate);

    CameraPosition getCameraPosition();

    void setCameraPosition(CameraPosition position);

    void adjustCamera(List<MapPoint> mapPoints, int padding);

    // ========== marker and infoWindow ========== //

    void addMarker(MapMarker mapMarker);

    void removeMarker(MapMarker mapMarker);

    List<MapMarker> getMarkers();

    void clearMarkers();

    void updateMarker(MapMarker mapMarker);

    void setMarkerVisible(MapMarker mapMarker, boolean visible);

    void setInfoWindowInflater(InfoWindowInflater inflater);

    void hideInfoWindow();

    void showInfoWindow(MapMarker mapMarker);

    // == polyline == //
    void addPolyline(Polyline polyline);

    void removePolyline(Polyline polyline);

    List<Polyline> getPolylines();

    void clearPolylines();

    // ========== graph ========== //

    void addGraph(Graph graph);

    void removeGraph(Graph p);

    List<Graph> getGraphs();

    void clearGraphs();

    // ========== basic function =========//

    void screenShotAndSave(String saveFilePath, MapScreenCaptureListener listener);

    MapPoint graphicPointToMapPoint(Point point);

    Point mapPointToGraphicPoint(MapPoint point);

    void setZoomControlsEnabled(boolean enable);
}