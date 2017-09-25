package com.ray.lib_map.impl.baidu;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.ray.lib_map.MapHolder;
import com.ray.lib_map.MapViewInterface;
import com.ray.lib_map.MarkerInflater;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class BaiduMapHolder implements MapHolder {

    private final Context mContext;
    private WeakReference<View> mapViewReference;
    private static boolean hasInited;

    public BaiduMapHolder(Context context) {
        if (!hasInited) {
            SDKInitializer.initialize(context.getApplicationContext());
            hasInited = true;
        }

        mContext = context;
    }

    @Override
    public View inflateMapView() {
        MapView mapView = new MapView(mContext);
        mapViewReference = new WeakReference<View>(mapView);
        return mapView;
    }

    @Override
    public View getMapView() {
        return mapViewReference.get();
    }

    @Override
    public void clearMap() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void setMapLoadListener(MapViewInterface.MapLoadListener listener) {

    }

    @Override
    public void setCameraMoveListener(MapViewInterface.CameraMoveListener listener) {

    }

    @Override
    public void setAnimationListener(MapViewInterface.AnimationListener listener) {

    }

    @Override
    public void setMarkerClickListener(MapViewInterface.MarkerClickListener listener) {

    }

    @Override
    public void setInfoWindowClickListener(MapViewInterface.InfoWindowClickListener listener) {

    }

    @Override
    public void setMapScreenCaptureListener(MapViewInterface.MapScreenCaptureListener listener) {

    }

    @Override
    public void setGestureEnable(boolean enable) {

    }

    @Override
    public void screenShotAndSave(String saveFilePath) {

    }

    @Override
    public void animateTo(MapPoint mapPoint, MapViewInterface.AnimationListener listener) {

    }

    @Override
    public void moveTo(MapPoint point, boolean isSmooth, float zoom) {

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
    public void setZoomGestureEnable(boolean enable) {

    }

    @Override
    public MapPoint getCameraPosition() {
        return null;
    }

    @Override
    public void setZoomControlsEnabled(boolean enabled) {

    }

    @Override
    public void zoomTo(float zoom) {

    }

    @Override
    public void zoomOut() {

    }

    @Override
    public void zoomIn() {

    }

    @Override
    public float getCurrentZoom() {
        return 0;
    }

    @Override
    public float getMaxZoomLevel() {
        return 0;
    }

    @Override
    public float getMinZoomLevel() {
        return 0;
    }

    @Override
    public void setMarkerInflater(MarkerInflater inflater) {

    }

    @Override
    public void addOverlay(MapOverlay overlay) {

    }

    @Override
    public void addOverlays(List<MapOverlay> overlays) {

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
    public void clearMarker() {

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
    public void addPolyline(MapLine mapLine) {

    }

    @Override
    public void addPolylines(MapLine... mapLines) {

    }

    @Override
    public void removePolyline(MapLine p) {

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
    public void addMarkers(List<MapMarker> mapMarkers) {

    }
}
