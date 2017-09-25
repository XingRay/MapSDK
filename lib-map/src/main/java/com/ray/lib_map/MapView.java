package com.ray.lib_map;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 地图
 */

@SuppressWarnings("unused")
public class MapView extends View {
    private Map<MapType, MapHolder> mHolders;
    private View mCurrentMapView;
    private MapType mMapType;
    private Context mContext;
    private MapHolder mHolder;

    public MapView(@NonNull Context context) {
        this(context, null);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHolders = new HashMap<>();

        setVisibility(GONE);
        setWillNotDraw(true);
        mCurrentMapView = this;
    }

    public void onCreate(Bundle savedInstanceState) {
        mHolder.onCreate(savedInstanceState);
    }

    public void onResume() {
        mHolder.onResume();
    }

    public void onPause() {
        mHolder.onPause();
    }

    public void onDestroy() {
        mHolder.onDestroy();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //do nothing
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //do nothing
    }

    @Nullable
    public View getMapView() {
        return mHolder == null ? null : mHolder.getMapView();
    }

    @Override
    public void setVisibility(int visibility) {
        View mapView = getMapView();
        if (mapView == null) {
            super.setVisibility(visibility);
        } else {
            mapView.setVisibility(visibility);
        }
    }

    public void inflate(MapType mapType) {
        mMapType = mapType;
        mHolder = getMapHolder();
        View mapView = mHolder.inflateMapView();

        final ViewParent viewParent = mCurrentMapView.getParent();
        if (viewParent == null || !(viewParent instanceof ViewGroup)) {
            throw new IllegalStateException("must have a non-null ViewGroup viewParent");
        }
        ViewGroup parent = (ViewGroup) viewParent;

        int id = mCurrentMapView.getId();
        if (id != NO_ID) {
            mapView.setId(id);
        }

        final int index = parent.indexOfChild(mCurrentMapView);
        parent.removeViewAt(index);

        final ViewGroup.LayoutParams layoutParams = mCurrentMapView.getLayoutParams();
        if (layoutParams == null) {
            parent.addView(mapView, index);
        } else {
            parent.addView(mapView, index, layoutParams);
        }
        mCurrentMapView = mapView;
    }

    public void switchMapType(MapType mapType) {
        switchMapType(mapType, null);
    }

    public void switchMapType(MapType mapType, Bundle savedInstanceState) {
        if (mMapType == mapType) {
            return;
        }

        inflate(mapType);
        mHolder.onCreate(savedInstanceState);
    }

    private MapHolder getMapHolder() {
        MapHolder holder = mHolders.get(mMapType);
        if (holder == null) {
            holder = mMapType.createMapHolder(mContext);
            mHolders.put(mMapType, holder);
        }
        return holder;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        mHolder.onSaveInstanceState(savedInstanceState);
    }

    public void setMapLoadListener(MapViewInterface.MapLoadListener listener) {
        mHolder.setMapLoadListener(listener);
    }

    public void setCameraMoveListener(MapViewInterface.CameraMoveListener listener) {
        mHolder.setCameraMoveListener(listener);
    }

    public void setAnimationListener(MapViewInterface.AnimationListener listener) {
        mHolder.setAnimationListener(listener);
    }

    public void setMarkerClickListener(MapViewInterface.MarkerClickListener listener) {
        mHolder.setMarkerClickListener(listener);
    }

    public void setInfoWindowClickListenr(MapViewInterface.InfoWindowClickListener listener) {
        mHolder.setInfoWindowClickListener(listener);
    }

    public void setMapScreenCaptureListener(MapViewInterface.MapScreenCaptureListener listener) {
        mHolder.setMapScreenCaptureListener(listener);
    }


    public void clearMap() {
        mHolder.clearMap();
    }

    public void setInfoWindowClickListener(MapViewInterface.InfoWindowClickListener listener) {
        mHolder.setInfoWindowClickListener(listener);
    }

    public void screenShotAndSave(String saveFilePath) {
        mHolder.screenShotAndSave(saveFilePath);
    }

    public void setZoomControlsEnabled(boolean enabled) {
        mHolder.setZoomControlsEnabled(enabled);
    }

    public void zoomTo(float zoom) {
        mHolder.zoomTo(zoom);
    }

    public void zoomOut() {
        mHolder.zoomOut();
    }

    public void zoomIn() {
        mHolder.zoomIn();
    }

    public void animateTo(MapPoint mapPoint, MapViewInterface.AnimationListener listener) {
        mHolder.animateTo(mapPoint, listener);
    }

    public void moveTo(MapPoint point, boolean isSmooth, float zoom) {
        mHolder.moveTo(point, isSmooth, zoom);
    }

    public void moveByBounds(List<MapPoint> points, int padding) {
        mHolder.moveByBounds(points, padding);
    }

    public void moveByPolygon(Polygon polygon, int padding) {
        mHolder.moveByPolygon(polygon, padding);
    }

    public void moveByCircle(Circle circle, int padding) {
        mHolder.moveByCircle(circle, padding);
    }

    public void setGestureEnable(boolean enable) {
        mHolder.setGestureEnable(enable);
    }

    public void setZoomGestureEnable(boolean enable) {
        mHolder.setZoomGestureEnable(enable);
    }

    public MapPoint getCameraPosition() {
        return mHolder.getCameraPosition();
    }

    public float getCurrentZoom() {
        return mHolder.getCurrentZoom();
    }

    public float getMaxZoomLevel() {
        return mHolder.getMaxZoomLevel();
    }

    public float getMinZoomLevel() {
        return mHolder.getMinZoomLevel();
    }

    public void setMarkerInflater(MarkerInflater inflater) {
        mHolder.setMarkerInflater(inflater);
    }

    public void addOverlay(MapOverlay overlay) {
        mHolder.addOverlay(overlay);
    }

    public void addOverlays(List<MapOverlay> overlays) {
        mHolder.addOverlays(overlays);
    }

    public void removeOverlay(MapOverlay overlay) {
        mHolder.removeOverlay(overlay);
    }

    public void clearOverlay() {
        mHolder.clearOverlay();
    }

    public void moveOverlay(MapOverlay overlay, MapPoint toPoint) {
        mHolder.moveOverlay(overlay, toPoint);
    }

    public void updateOverlay(MapOverlay overlay, float xOffset, float yOffset) {
        mHolder.updateOverlay(overlay, xOffset, yOffset);
    }

    public MapOverlay getOverlay(MapPoint mapPoint) {
        return mHolder.getOverlay(mapPoint);
    }

    public List<MapOverlay> getOverlays() {
        return mHolder.getOverlays();
    }

    public void clearMarker() {
        mHolder.clearMarker();
    }

    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {
        mHolder.setMarkerVisible(mapMarker, visible);
    }

    public void addPolygon(Polygon polygon) {
        mHolder.addPolygon(polygon);
    }

    public void removePolygon(Polygon polygon) {
        mHolder.removePolygon(polygon);
    }

    public void addPolyline(MapLine mapLine) {
        mHolder.addPolyline(mapLine);
    }

    public void addPolylines(MapLine... mapLines) {
        mHolder.addPolylines(mapLines);
    }

    public void removePolyline(MapLine mapLine) {
        mHolder.removePolyline(mapLine);
    }

    public void addCircle(Circle circle) {
        mHolder.addCircle(circle);
    }

    public void removeCircle(Circle circle) {
        mHolder.removeCircle(circle);
    }

    public void addMarker(MapMarker mapMarker) {
        mHolder.addMarker(mapMarker);
    }

    public void addMarkers(List<MapMarker> mapMarkers) {
        mHolder.addMarkers(mapMarkers);
    }

    public void removeMarker(MapMarker mapMarker) {
        mHolder.removeMarker(mapMarker);
    }
}
