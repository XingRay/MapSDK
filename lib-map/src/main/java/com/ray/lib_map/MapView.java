package com.ray.lib_map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.GestureSetting;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapDelegateFactory;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.listener.AnimationListener;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MarkerClickListener;
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
        setVisibility(GONE);
        setWillNotDraw(true);

        mCurrentMapView = this;
        mContext = context;
        mMapDelegateFactory = new MapDelegateFactory(context);

        mMapMarkers = new ArrayList<>();
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
        if (mCurrentMapView == null || mCurrentMapView == this) {
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

    private void updateMap() {
    }

    public void switchMapType(MapType mapType) {
        switchMapType(mapType, null);
    }

    public void switchMapType(MapType mapType, Bundle savedInstanceState) {
        if (mMapType == mapType) {
            return;
        }

        final CameraPosition position = getCameraPosition();
        final GestureSetting setting = getGestureSetting();
        clearMarkersInCurrentMap();

        // 地图切换 注意：不同的地图类型切换时执行的生命周期方法不一样
        mMapDelegate.onSwitchOut();
        mMapType = mapType;
        mMapDelegate = mMapDelegateFactory.getMapDelegate(mMapType);

        //新地图控件切换进界面
        mMapDelegate.onSwitchIn(savedInstanceState);
        mCurrentMapView = ViewUtil.replaceView(mCurrentMapView, mMapDelegate.getMapView());

        setCameraPosition(position);
        setGestureSetting(setting);
        addMarkersInCurrentMap();
    }

    public CameraPosition getCameraPosition() {
        return mMapDelegate.saveCameraPosition();
    }

    public void setCameraPosition(CameraPosition cameraPosition) {
        mMapDelegate.restoreCameraPosition(cameraPosition);
    }

    public GestureSetting getGestureSetting() {
        MapDelegate mapDelegate = mMapDelegate;

        GestureSetting setting = new GestureSetting();
        setting.setZoomGestureEnable(mapDelegate.isZoomGestureEnable());
        setting.setScrollGestureEnable(mapDelegate.isScrollGestureEnable());
        setting.setOverlookGestureEnable(mapDelegate.isOverlookGestureEnable());
        setting.setRotateGestureEnable(mapDelegate.isRotateGestureEnable());
        return setting;
    }

    public void setGestureSetting(GestureSetting setting) {
        MapDelegate mapDelegate = mMapDelegate;

        mapDelegate.setZoomGestureEnable(setting.isZoomGestureEnable());
        mapDelegate.setRotateGestureEnable(setting.isRotateGestureEnable());
        mapDelegate.setScrollGestureEnable(setting.isScrollGestureEnable());
        mapDelegate.setOverlookGestureEnable(setting.isOverlookGestureEnable());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        mMapDelegate.onSaveInstanceState(savedInstanceState);
    }

    public void setMapLoadListener(MapLoadListener listener) {
        mMapDelegate.setMapLoadListener(listener);
    }

    public void setCameraMoveListener(CameraMoveListener listener) {
        mMapDelegate.setCameraMoveListener(listener);
    }

    public void setAnimationListener(AnimationListener listener) {
        mMapDelegate.setAnimationListener(listener);
    }

    public void setMarkerClickListener(MarkerClickListener listener) {
        mMapDelegate.setMarkerClickListener(listener);
    }

    public void setMapScreenCaptureListener(MapScreenCaptureListener listener) {
        mMapDelegate.setMapScreenCaptureListener(listener);
    }


    public void clearMap() {
        mMapDelegate.clearMap();
    }

    public void setInfoWindowClickListener(InfoWindowClickListener listener) {
        mMapDelegate.setInfoWindowClickListener(listener);
    }

    public float getZoom() {
        return mMapDelegate.getZoom();
    }

    public void setZoom(float zoom) {
        mMapDelegate.setZoom(zoom);
    }

    public float getRotate() {
        return mMapDelegate.getRotate();
    }

    public void setRotate(float rotate) {
        mMapDelegate.setRotate(rotate);
    }

    public MapPoint getPosition() {
        return mMapDelegate.getPosition();
    }

    public void setPosition(MapPoint mapPoint) {
        mMapDelegate.setPosition(mapPoint);
    }

    public float getOverlook() {
        return mMapDelegate.getOverlook();
    }

    public void setOverlook(float overlook) {
        mMapDelegate.setOverlook(overlook);
    }

    public boolean isZoomGestureEnable() {
        return mMapDelegate.isZoomGestureEnable();
    }

    public void setZoomGestureEnable(boolean enable) {
        mMapDelegate.setZoomGestureEnable(enable);
    }

    public boolean isScrollGestureEnable() {
        return mMapDelegate.isScrollGestureEnable();
    }

    public void setScrollGestureEnable(boolean enable) {
        mMapDelegate.setScrollGestureEnable(enable);
    }

    public boolean isOverlookGestureEnable() {
        return mMapDelegate.isOverlookGestureEnable();
    }

    public void setOverlookGestureEnable(boolean enable) {
        mMapDelegate.setOverlookGestureEnable(enable);
    }

    public boolean isRotateGestureEnable() {
        return mMapDelegate.isRotateGestureEnable();
    }

    public void setRotateGestureEnable(boolean enable) {
        mMapDelegate.setRotateGestureEnable(enable);
    }

    public void screenShotAndSave(String saveFilePath) {
        mMapDelegate.screenShotAndSave(saveFilePath);
    }

    public void zoomOut() {
        mMapDelegate.zoomOut();
    }

    public void zoomIn() {
        mMapDelegate.zoomIn();
    }

    public void animateTo(MapPoint mapPoint, AnimationListener listener) {
        animateTo(mapPoint, 17, listener);
    }

    public void animateTo(MapPoint mapPoint, float zoom, AnimationListener listener) {
        mMapDelegate.animateTo(mapPoint, zoom, listener);
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

    public MapPoint fromScreenLocation(Point point) {
        return mMapDelegate.graphicPointToMapPoint(point);
    }

    public Point toScreenLocation(MapPoint point) {
        return mMapDelegate.mapPointToGraphicPoint(point);
    }

    public float getMaxZoomLevel() {
        return mMapDelegate.getMaxZoom();
    }

    public float getMinZoomLevel() {
        return mMapDelegate.getMinZoom();
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
        clearMarkersInCurrentMap();
        mMapMarkers.clear();
    }

    private void clearMarkersInCurrentMap() {
        MapDelegate mapDelegate = mMapDelegate;
        List<MapMarker> mapMarkers = mMapMarkers;

        for (MapMarker markers : mapMarkers) {
            mapDelegate.removeMarker(markers);
        }
    }

    private void addMarkersInCurrentMap() {
        MapDelegate mapDelegate = mMapDelegate;
        List<MapMarker> mapMarkers = mMapMarkers;

        for (MapMarker mapMarker : mapMarkers) {
            mapDelegate.addMarker(mapMarker);
        }
    }

    public int getMapHeight() {
        if (mCurrentMapView == null || mCurrentMapView == this) {
            return getHeight();
        } else {
            return mCurrentMapView.getHeight();
        }
    }

    public int getMapWidth() {
        if (mCurrentMapView == null || mCurrentMapView == this) {
            return getWidth();
        } else {
            return mCurrentMapView.getWidth();
        }
    }
}
