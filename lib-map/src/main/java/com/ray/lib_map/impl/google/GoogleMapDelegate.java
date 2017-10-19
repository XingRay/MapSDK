package com.ray.lib_map.impl.google;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.entity.polyline.Polyline;
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
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GoogleMapDelegate implements MapDelegate {

    private static final String MAP_VIEW_BUNDLE_KEY = "google_map_view_bundle_key";
    private static boolean sHasInited;
    private final MapView mMapView;
    private MapLoadListener mMapLoadListener;

    public GoogleMapDelegate(Context context) {
        if (!sHasInited) {
            MapsInitializer.initialize(context);
        }
        sHasInited = true;

        mMapView = new MapView(context);
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    @Override
    public void clearMap() {

    }

    @Override
    public void onSwitchOut() {
        onPause();
        onDestroy();
    }

    @Override
    public void onSwitchIn(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (mMapLoadListener != null) {
                    mMapLoadListener.onMapLoaded();
                }
            }
        });
    }

    @Override
    public void onResume() {
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Bundle mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            savedInstanceState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void setMapLoadListener(MapLoadListener listener) {
        mMapLoadListener = listener;
    }

    @Override
    public void setCameraMoveListener(CameraMoveListener listener) {

    }

    @Override
    public void setMarkerClickListener(MarkerClickListener listener) {

    }

    @Override
    public void setInfoWindowClickListener(InfoWindowClickListener listener) {

    }

    @Override
    public void setMapScreenCaptureListener(MapScreenCaptureListener listener) {

    }

    @Override
    public void setMapClickListener(MapClickListener listener) {

    }

    @Override
    public void setMapLongClickListener(MapLongClickListener listener) {

    }

    @Override
    public boolean isZoomGestureEnable() {
        return false;
    }

    @Override
    public void setZoomGestureEnable(boolean enable) {

    }

    @Override
    public boolean isScrollGestureEnable() {
        return false;
    }

    @Override
    public void setScrollGestureEnable(boolean enable) {

    }

    @Override
    public boolean isRotateGestureEnable() {
        return false;
    }

    @Override
    public void setRotateGestureEnable(boolean enable) {

    }

    @Override
    public boolean isOverlookGestureEnable() {
        return false;
    }

    @Override
    public void setOverlookGestureEnable(boolean enable) {

    }

    @Override
    public float getOverlook() {
        return 0;
    }

    @Override
    public void setOverlook(float overlook) {

    }

    @Override
    public float getRotate() {
        return 0;
    }

    @Override
    public void setRotate(float rotate) {

    }

    @Override
    public MapPoint getPosition() {
        return null;
    }

    @Override
    public void setPosition(MapPoint mapPoint) {

    }

    @Override
    public CameraPosition getCameraPosition() {
        return null;
    }

    @Override
    public void setCameraPosition(CameraPosition position) {

    }

    @Override
    public void setInfoWindowInflater(InfoWindowInflater inflater) {

    }

    @Override
    public List<MapMarker> getMarkers() {
        return null;
    }

    @Override
    public void clearMarkers() {

    }

    @Override
    public void screenShotAndSave(String saveFilePath) {

    }

    @Override
    public void moveByBounds(List<MapPoint> points, int padding) {

    }

    @Override
    public void moveByPolygon(Polygon polygon, int padding) {

    }

    @Override
    public void moveByCircle(Circle circle, int padding) {

    }

    @Override
    public MapPoint graphicPointToMapPoint(Point point) {
        return null;
    }

    @Override
    public Point mapPointToGraphicPoint(MapPoint point) {
        return null;
    }

    @Override
    public void zoomOut() {

    }

    @Override
    public void zoomIn() {

    }

    @Override
    public float getZoom() {
        return 0;
    }

    @Override
    public void setZoom(float zoom) {

    }

    @Override
    public float getMaxZoom() {
        return 0;
    }

    @Override
    public float getMinZoom() {
        return 0;
    }

    @Override
    public void addOverlay(MapOverlay overlay) {

    }

    @Override
    public void removeOverlay(MapOverlay overlay) {

    }

    @Override
    public void clearOverlay() {

    }

    @Override
    public void moveOverlay(MapOverlay overlay, MapPoint toPoint) {

    }

    @Override
    public void updateOverlay(MapOverlay overlay, float xOffset, float yOffset) {

    }

    @Override
    public MapOverlay getOverlay(MapPoint mapPoint) {
        return null;
    }

    @Override
    public List<MapOverlay> getOverlays() {
        return null;
    }

    @Override
    public void addMarker(MapMarker mapMarker) {

    }

    @Override
    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {

    }

    @Override
    public Polygon addPolygon(Polygon polygon) {
        return null;
    }

    @Override
    public void removePolygon(Polygon p) {

    }

    @Override
    public void addPolyline(Polyline polyline) {

    }

    @Override
    public void removePolyline(Polyline polyline) {
    }

    @Override
    public List<Polyline> getPolylines() {
        return null;
    }

    @Override
    public void addCircle(Circle circle) {

    }

    @Override
    public void removeCircle(Circle circle) {

    }

    @Override
    public void removeMarker(MapMarker mapMarker) {

    }

    @Override
    public void hideInfoWindow() {

    }

    @Override
    public void showInfoWindow(MapMarker mapMarker) {

    }
}
