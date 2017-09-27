package com.ray.lib_map.impl.baidu;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.MapViewInterface;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapType;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class BaiduMapDelegate implements MapDelegate {
    private static final String MAP_VIEW_BUNDLE_KEY = "baidu_map_view_bundle_key";
    private static boolean sHasInited;
    private final Context mContext;
    private MapView mMapView;

    public BaiduMapDelegate(Context context) {
        if (!sHasInited) {
            SDKInitializer.initialize(context.getApplicationContext());
            sHasInited = true;
        }

        mContext = context;
        mMapView = new MapView(mContext);
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
        mMapView.onCreate(mContext, mapViewBundle);
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
        mMapView.onDestroy();
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
    public void addMarker(MapMarker marker) {
        Overlay overlay = mMapView.getMap().addOverlay(new MarkerOptions()
                .position(new LatLng(marker.getLatitude(), marker.getLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .title(marker.getTitle()));
        marker.setRawMarker(MapType.BAIDU, overlay);
    }

    @Override
    public void removeMarker(MapMarker mapMarker) {
        Overlay overlay = (Overlay) mapMarker.getRawMarker(MapType.BAIDU);
        overlay.remove();
        mapMarker.removeRawMarker(MapType.BAIDU);
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
    public void removePolyline(MapLine p) {

    }

    @Override
    public void addCircle(Circle circle) {

    }

    @Override
    public void removeCircle(Circle circle) {

    }


}
