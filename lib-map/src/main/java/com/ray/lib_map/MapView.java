package com.ray.lib_map;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapDelegateFactory;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private View mCurrentMapView;
    private MapType mMapType;
    private Context mContext;
    private MapDelegate mMapDelegate;
    private List<MapMarker> mMapMarkers;
    private MapDelegateFactory mMapDelegateFactory;

    public MapView(@NonNull Context context) {
        this(context, null);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCurrentMapView = this;
        mContext = context;
        mMapDelegateFactory = new MapDelegateFactory(context);
        mMapMarkers = new ArrayList<>();

        setVisibility(GONE);
        setWillNotDraw(true);

    }

    public void onCreate(Bundle savedInstanceState) {
        mMapDelegate.onCreate(savedInstanceState);
    }

    public void onResume() {
        mMapDelegate.onResume();
    }

    public void onPause() {
        mMapDelegate.onPause();
    }

    public void onDestroy() {
        mMapDelegate.onDestroy();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //do nothing
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //do nothing
    }

    @Override
    public void setVisibility(int visibility) {
        if (mCurrentMapView == this) {
            super.setVisibility(visibility);
        } else {
            mCurrentMapView.setVisibility(visibility);
        }
    }

    public void createMap(MapType mapType) {
        mMapType = mapType;
        mMapDelegate = mMapDelegateFactory.getMapDelegate(mMapType);
        mCurrentMapView = ViewUtil.replaceView(mCurrentMapView, mMapDelegate.getMapView());
    }

    public void switchMapType(MapType mapType) {
        switchMapType(mapType, null);
    }

    public void switchMapType(MapType mapType, Bundle savedInstanceState) {
        if (mMapType == mapType) {
            return;
        }

        // == 地图切换 注意：不同的地图类型切换时执行的生命周期方法不一样
        // 旧地图切换出界面
        mMapDelegate.onSwitchOut();
        clearAllMarkers(mMapDelegate);

        mMapType = mapType;
        mMapDelegate = mMapDelegateFactory.getMapDelegate(mMapType);

        //新地图控件切换进界面
        mMapDelegate.onSwitchIn(savedInstanceState);
        addAllMarkers(mMapDelegate);

        mCurrentMapView = ViewUtil.replaceView(mCurrentMapView, mMapDelegate.getMapView());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        mMapDelegate.onSaveInstanceState(savedInstanceState);
    }

    public void setMapLoadListener(MapViewInterface.MapLoadListener listener) {
        mMapDelegate.setMapLoadListener(listener);
    }

    public void setCameraMoveListener(MapViewInterface.CameraMoveListener listener) {
        mMapDelegate.setCameraMoveListener(listener);
    }

    public void setAnimationListener(MapViewInterface.AnimationListener listener) {
        mMapDelegate.setAnimationListener(listener);
    }

    public void setMarkerClickListener(MapViewInterface.MarkerClickListener listener) {
        mMapDelegate.setMarkerClickListener(listener);
    }

    public void setInfoWindowClickListenr(MapViewInterface.InfoWindowClickListener listener) {
        mMapDelegate.setInfoWindowClickListener(listener);
    }

    public void setMapScreenCaptureListener(MapViewInterface.MapScreenCaptureListener listener) {
        mMapDelegate.setMapScreenCaptureListener(listener);
    }


    public void clearMap() {
        mMapDelegate.clearMap();
    }

    public void setInfoWindowClickListener(MapViewInterface.InfoWindowClickListener listener) {
        mMapDelegate.setInfoWindowClickListener(listener);
    }

    public void screenShotAndSave(String saveFilePath) {
        mMapDelegate.screenShotAndSave(saveFilePath);
    }

    public void setZoomControlsEnabled(boolean enabled) {
        mMapDelegate.setZoomControlsEnabled(enabled);
    }

    public void zoomTo(float zoom) {
        mMapDelegate.zoomTo(zoom);
    }

    public void zoomOut() {
        mMapDelegate.zoomOut();
    }

    public void zoomIn() {
        mMapDelegate.zoomIn();
    }

    public void animateTo(MapPoint mapPoint, MapViewInterface.AnimationListener listener) {
        mMapDelegate.animateTo(mapPoint, listener);
    }

    public void moveTo(MapPoint point, boolean isSmooth, float zoom) {
        mMapDelegate.moveTo(point, isSmooth, zoom);
    }

    public void moveByBounds(List<MapPoint> points, int padding) {
        mMapDelegate.moveByBounds(points, padding);
    }

    public void moveByPolygon(Polygon polygon, int padding) {
        mMapDelegate.moveByPolygon(polygon, padding);
    }

    public void moveByCircle(Circle circle, int padding) {
        mMapDelegate.moveByCircle(circle, padding);
    }

    public void setGestureEnable(boolean enable) {
        mMapDelegate.setGestureEnable(enable);
    }

    public void setZoomGestureEnable(boolean enable) {
        mMapDelegate.setZoomGestureEnable(enable);
    }

    public MapPoint getCameraPosition() {
        return mMapDelegate.getCameraPosition();
    }

    public float getCurrentZoom() {
        return mMapDelegate.getCurrentZoom();
    }

    public float getMaxZoomLevel() {
        return mMapDelegate.getMaxZoomLevel();
    }

    public float getMinZoomLevel() {
        return mMapDelegate.getMinZoomLevel();
    }

    public void addOverlay(MapOverlay overlay) {
        mMapDelegate.addOverlay(overlay);
    }

    public void addOverlays(List<MapOverlay> overlays) {
        if (overlays == null) {
            return;
        }
        for (MapOverlay overlay : overlays) {
            mMapDelegate.addOverlay(overlay);
        }
    }

    public void removeOverlay(MapOverlay overlay) {
        mMapDelegate.removeOverlay(overlay);
    }

    public void clearOverlay() {
        mMapDelegate.clearOverlay();
    }

    public void moveOverlay(MapOverlay overlay, MapPoint toPoint) {
        mMapDelegate.moveOverlay(overlay, toPoint);
    }

    public void updateOverlay(MapOverlay overlay, float xOffset, float yOffset) {
        mMapDelegate.updateOverlay(overlay, xOffset, yOffset);
    }

    public MapOverlay getOverlay(MapPoint mapPoint) {
        return mMapDelegate.getOverlay(mapPoint);
    }

    public List<MapOverlay> getOverlays() {
        return mMapDelegate.getOverlays();
    }

    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {
        mMapDelegate.setMarkerVisible(mapMarker, visible);
    }

    public void addPolygon(Polygon polygon) {
        mMapDelegate.addPolygon(polygon);
    }

    public void removePolygon(Polygon polygon) {
        mMapDelegate.removePolygon(polygon);
    }

    public void addPolyline(MapLine mapLine) {
        mMapDelegate.addPolyline(mapLine);
    }

    public void addPolylines(List<MapLine> mapLines) {
        if (mapLines == null) {
            return;
        }
        for (MapLine mapLine : mapLines) {
            mMapDelegate.addPolyline(mapLine);
        }
    }

    public void removePolyline(MapLine mapLine) {
        mMapDelegate.removePolyline(mapLine);
    }

    public void addCircle(Circle circle) {
        mMapDelegate.addCircle(circle);
    }

    public void removeCircle(Circle circle) {
        mMapDelegate.removeCircle(circle);
    }

    public void addMarker(MapMarker mapMarker) {
        mMapDelegate.addMarker(mapMarker);
        mMapMarkers.add(mapMarker);
    }

    public void addMarkers(Collection<MapMarker> mapMarkers) {
        if (mapMarkers == null) {
            return;
        }
        for (MapMarker mapMarker : mapMarkers) {
            mMapDelegate.addMarker(mapMarker);
        }
        mMapMarkers.addAll(mapMarkers);
    }

    public void removeMarker(MapMarker mapMarker) {
        mMapDelegate.removeMarker(mapMarker);
        mMapMarkers.remove(mapMarker);
    }

    public void removeMarkers(Collection<MapMarker> mapMarkers) {
        if (mapMarkers == null) {
            return;
        }
        for (MapMarker mapMarker : mapMarkers) {
            mMapDelegate.removeMarker(mapMarker);
        }
        mMapMarkers.removeAll(mapMarkers);
    }

    public void clearMarkers() {
        clearAllMarkers(mMapDelegate);
        mMapMarkers.clear();
    }

    private void clearAllMarkers(MapDelegate mapDelegate) {
        for (MapMarker markers : mMapMarkers) {
            mapDelegate.removeMarker(markers);
        }
    }

    private void addAllMarkers(MapDelegate mapDelegate) {
        for (MapMarker mapMarker : mMapMarkers) {
            mapDelegate.addMarker(mapMarker);
        }
    }
}
