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
import com.ray.lib_map.entity.GestureSetting;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.graph.Circle;
import com.ray.lib_map.entity.graph.Graph;
import com.ray.lib_map.entity.graph.Polygon;
import com.ray.lib_map.entity.polyline.Polyline;
import com.ray.lib_map.extern.MapDelegateFactory;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapLongClickListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MapSwitchListener;
import com.ray.lib_map.listener.MarkerClickListener;
import com.ray.lib_map.util.MapUtil;
import com.ray.lib_map.util.ViewUtil;

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
    private MapDelegate mMapDelegate;

    private MapDelegateFactory mMapDelegateFactory;
    private MarkerClickListener mMarkerClickListener;
    private InfoWindowInflater mInfoWindowInflater;
    private InfoWindowClickListener mInfoWindowClickListener;
    private CameraMoveListener mCameraMoveListener;
    @SuppressWarnings("FieldCanBeLocal")
    private MapLoadListener mMapLoadListener;
    private MapClickListener mMapClickListener;
    private MapLongClickListener mMapLongClickListener;
    private boolean mZoomControlsEnable = true;

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
        mMapDelegateFactory = new MapDelegateFactory(context);
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
        mMapDelegate = mMapDelegateFactory.createMapDelegate(mMapType);
        mCurrentMapView = ViewUtil.replaceView(mCurrentMapView, mMapDelegate.getMapView());

        setListeners();
    }

    public void switchMapType(MapType mapType) {
        switchMapType(mapType, null, null);
    }

    @SuppressWarnings("SameParameterValue")
    public void switchMapType(final MapType newMapType, Bundle savedInstanceState, final MapSwitchListener listener) {
        if (mMapType == newMapType) {
            if (listener != null) {
                listener.onMapSwitch();
            }
            return;
        }

        final CameraPosition position = getCameraPosition();
        final GestureSetting setting = getGestureSetting();
        final List<MapMarker> mapMarkers = mMapDelegate.getMarkers();
        final List<Polyline> polylines = mMapDelegate.getPolylines();
        final List<Graph> graphs = mMapDelegate.getGraphs();

        final MapDelegate newMapDelegate = mMapDelegateFactory.createMapDelegate(newMapType);

        //新地图控件切换进界面
        newMapDelegate.onSwitchIn(savedInstanceState, new MapSwitchListener() {
            @Override
            public void onMapSwitch() {
                mCurrentMapView = ViewUtil.replaceView(mCurrentMapView, newMapDelegate.getMapView());
                mMapDelegate.onSwitchOut();
                clearRawPolylines(polylines, mMapType);
                clearRawMarker(mapMarkers, mMapType);

                mMapType = newMapType;
                mMapDelegate = newMapDelegate;

                mMapDelegate.setZoomControlsEnabled(mZoomControlsEnable);
                setCameraPosition(position);
                setGestureSetting(setting);
                setMarkers(mapMarkers);
                setPolylines(polylines);
                setGraphs(graphs);

                setListeners();

                if (listener != null) {
                    listener.onMapSwitch();
                }
            }

            @Override
            public void onFailure(int errorCode, String desc) {
                if (listener != null) {
                    listener.onFailure(errorCode, desc);
                }
            }
        });
    }

    private void setListeners() {
        mMapDelegate.setMarkerClickListener(mMarkerClickListener);
        mMapDelegate.setInfoWindowInflater(mInfoWindowInflater);
        mMapDelegate.setInfoWindowClickListener(mInfoWindowClickListener);
        mMapDelegate.setCameraMoveListener(mCameraMoveListener);
        mMapDelegate.setMapClickListener(mMapClickListener);
        mMapDelegate.setMapLongClickListener(mMapLongClickListener);
    }

    private void setGraphs(List<Graph> graphs) {
        clearGraphs();
        addGraphs(graphs);
    }

    private void addGraphs(List<Graph> graphs) {
        if (graphs == null) {
            return;
        }

        for (Graph graph : graphs) {
            mMapDelegate.addGraph(graph);
        }
    }

    public void clearGraphs() {
        mMapDelegate.clearGraphs();
    }

    private void clearRawPolylines(List<Polyline> polylines, MapType mapType) {
        if (polylines == null) {
            return;
        }
        for (Polyline polyline : polylines) {
            polyline.clearRawPolyline();
        }
    }

    private void clearRawMarker(List<MapMarker> mapMarkers, MapType mapType) {
        if (mapMarkers == null) {
            return;
        }
        for (MapMarker mapMarker : mapMarkers) {
            mapMarker.clearRawMarker();
        }
    }

    public CameraPosition getCameraPosition() {
        return mMapDelegate.getCameraPosition();
    }

    public void setCameraPosition(CameraPosition cameraPosition) {
        mMapDelegate.setCameraPosition(cameraPosition);
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
        mMapLoadListener = listener;
        mMapDelegate.setMapLoadListener(listener);
    }

    public void setCameraMoveListener(CameraMoveListener listener) {
        mCameraMoveListener = listener;
        mMapDelegate.setCameraMoveListener(listener);
    }


    public void clearMap() {
        mMapDelegate.clearMap();
    }

    public void setInfoWindowClickListener(InfoWindowClickListener listener) {
        mInfoWindowClickListener = listener;
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

    public void setMarkerClickListener(MarkerClickListener listener) {
        mMarkerClickListener = listener;
        mMapDelegate.setMarkerClickListener(listener);
    }

    public void setMarkers(List<MapMarker> mapMarkers) {
        mMapDelegate.clearMarkers();
        if (mapMarkers != null) {
            for (MapMarker mapMarker : mapMarkers) {
                addMarker(mapMarker);
            }
        }
    }

    public void clearMarkers() {
        if (mMapDelegate.getMarkers() == null) {
            return;
        }
        mMapDelegate.clearMarkers();
    }

    public void addMarker(MapMarker mapMarker) {
        mMapDelegate.addMarker(mapMarker);
    }

    public void addMarkers(Collection<MapMarker> mapMarkers) {
        if (mapMarkers == null) {
            return;
        }
        for (MapMarker mapMarker : mapMarkers) {
            mMapDelegate.addMarker(mapMarker);
        }
    }

    public void removeMarker(MapMarker mapMarker) {
        mMapDelegate.removeMarker(mapMarker);
    }

    public void removeMarkers(Collection<MapMarker> mapMarkers) {
        if (mapMarkers == null) {
            return;
        }
        for (MapMarker mapMarker : mapMarkers) {
            mMapDelegate.removeMarker(mapMarker);
        }
    }

    public void setInfoWindowInflater(InfoWindowInflater inflater) {
        mInfoWindowInflater = inflater;
        mMapDelegate.setInfoWindowInflater(inflater);
    }

    public void screenShotAndSave(String saveFilePath, MapScreenCaptureListener listener) {
        mMapDelegate.screenShotAndSave(saveFilePath, listener);
    }

    public void zoomOut() {
        mMapDelegate.zoomOut();
    }

    public void zoomIn() {
        mMapDelegate.zoomIn();
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

    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {
        mMapDelegate.setMarkerVisible(mapMarker, visible);
    }

    public void addGraph(Graph graph) {
        mMapDelegate.addGraph(graph);
    }

    public void removeGraph(Graph graph) {
        mMapDelegate.removeGraph(graph);
    }

    public void addPolyline(Polyline polyline) {
        mMapDelegate.addPolyline(polyline);
    }

    public void addPolylines(List<Polyline> polylines) {
        if (polylines == null) {
            return;
        }
        for (Polyline polyline : polylines) {
            mMapDelegate.addPolyline(polyline);
        }
    }

    public void setPolylines(List<Polyline> polylines) {
        mMapDelegate.clearPolylines();
        addPolylines(polylines);
    }

    public void removePolyline(Polyline polyline) {
        mMapDelegate.removePolyline(polyline);
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

    public void hideInfoWindow() {
        mMapDelegate.hideInfoWindow();
    }

    public void showInfoWindow(MapMarker mapMarker) {
        mMapDelegate.showInfoWindow(mapMarker);
    }

    public void moveCameraTo(MapPoint mapPoint) {
        setCameraPosition(new CameraPosition(mapPoint));
    }

    public void moveCameraTo(MapPoint mapPoint, float zoom) {
        CameraPosition cameraPosition = new CameraPosition();
        cameraPosition.setPosition(mapPoint);
        cameraPosition.setZoom(zoom);
        setCameraPosition(cameraPosition);
    }

    public void setMapClickListener(MapClickListener listener) {
        mMapClickListener = listener;
        mMapDelegate.setMapClickListener(listener);
    }

    public void setMapLongClickListener(MapLongClickListener listener) {
        mMapLongClickListener = listener;
        mMapDelegate.setMapLongClickListener(listener);
    }

    public void updateMarker(MapMarker mapMarker) {
        mMapDelegate.updateMarker(mapMarker);
    }

    public void focusOnInfoWindowOf(MapMarker marker) {

    }

    public void clearPolylines() {

    }

    public void removePolylines(List<Polyline> polylines) {

    }

    public void setAllGestureEnable(boolean enable) {
        mMapDelegate.setOverlookGestureEnable(enable);
        mMapDelegate.setRotateGestureEnable(enable);
        mMapDelegate.setScrollGestureEnable(enable);
        mMapDelegate.setZoomGestureEnable(enable);
    }

    public void adjustCamera(List<MapPoint> mapPoints, int padding) {
        mMapDelegate.adjustCamera(mapPoints, padding);
    }

    public void adjustCamera(Polygon polygon, int padding) {
        adjustCamera(polygon.getPoints(), padding);
    }

    public void adjustCamera(Circle circle, int padding) {
        List<MapPoint> bounds = MapUtil.getBounds(circle);
        adjustCamera(bounds, padding);
    }

    public void setZoomControlsEnabled(boolean enable) {
        mZoomControlsEnable = enable;
        mMapDelegate.setZoomControlsEnabled(enable);
    }
}
