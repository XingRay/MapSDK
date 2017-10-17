package com.ray.lib_map.impl.gaode;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.PolyLine;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.extern.ZoomStandardization;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapLongClickListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MarkerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GaodeMapDelegate implements MapDelegate {
    private static final String MAP_VIEW_BUNDLE_KEY = "gaode_map_view_bundle_key";
    private final Context mContext;
    private final List<MapMarker> mMapMarkers;
    private MapView mMapView;
    private MapMarker mShowingInfoWindowMapMarker;

    public GaodeMapDelegate(Context context) {
        mContext = context;
        mMapView = new MapView(mContext);

        mMapMarkers = new ArrayList<>();

        setListeners();
    }

    private void setListeners() {

    }

    private AMap getMap() {
        return mMapView.getMap();
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    @Override
    public void onSwitchOut() {
        onPause();
        onDestroy();
        mMapView = null;
    }

    @Override
    public void onSwitchIn(Bundle savedInstanceState) {
        mMapView = new MapView(mContext);
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
    public void setMapLoadListener(final MapLoadListener listener) {
        getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (listener != null) {
                    listener.onMapLoaded();
                }
            }
        });
    }

    @Override
    public void setCameraMoveListener(final CameraMoveListener listener) {
        getMap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(com.amap.api.maps.model.CameraPosition cameraPosition) {
                if (listener != null) {
                    listener.onCameraMoving(GaodeDataConverter.toCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onCameraChangeFinish(com.amap.api.maps.model.CameraPosition cameraPosition) {
                if (listener != null) {
                    listener.onCameraMoved(GaodeDataConverter.toCameraPosition(cameraPosition));
                }
            }
        });
    }

    @Override
    public void setInfoWindowClickListener(final InfoWindowClickListener listener) {
        getMap().setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (listener != null) {
                    MapMarker mapMarker = findMapMarker(marker);
                    if (mapMarker != null) {
                        listener.onInfoWindowClick(mapMarker);
                    }
                }
            }
        });
    }

    @Override
    public void setMapLongClickListener(final MapLongClickListener listener) {
        getMap().setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listener != null) {
                    listener.onMapLongClick(GaodeDataConverter.toMapPoint(latLng));
                }
            }
        });
    }

    @Override
    public void setMapScreenCaptureListener(MapScreenCaptureListener listener) {
    }

    @Override
    public boolean isZoomGestureEnable() {
        return getMap().getUiSettings().isZoomGesturesEnabled();
    }

    @Override
    public void setZoomGestureEnable(boolean enable) {
        getMap().getUiSettings().setZoomGesturesEnabled(enable);
    }

    @Override
    public boolean isScrollGestureEnable() {
        return getMap().getUiSettings().isScrollGesturesEnabled();
    }

    @Override
    public void setScrollGestureEnable(boolean enable) {
        getMap().getUiSettings().setScrollGesturesEnabled(enable);
    }

    @Override
    public boolean isRotateGestureEnable() {
        return getMap().getUiSettings().isRotateGesturesEnabled();
    }

    @Override
    public void setRotateGestureEnable(boolean enable) {
        getMap().getUiSettings().setRotateGesturesEnabled(enable);
    }

    @Override
    public boolean isOverlookGestureEnable() {
        return getMap().getUiSettings().isTiltGesturesEnabled();
    }

    @Override
    public void setOverlookGestureEnable(boolean enable) {
        getMap().getUiSettings().setTiltGesturesEnabled(enable);
    }

    @Override
    public MapPoint getPosition() {
        com.amap.api.maps.model.CameraPosition cameraPosition = getMap().getCameraPosition();
        return new MapPoint(cameraPosition.target.latitude, cameraPosition.target.longitude, MapType.GAODE.getCoordinateType());
    }

    @Override
    public void setPosition(MapPoint mapPoint) {
        MapPoint gaodePoint = mapPoint.copy(MapType.GAODE.getCoordinateType());
        LatLng latLng = new LatLng(gaodePoint.getLatitude(), gaodePoint.getLongitude());
        getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public float getZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getCameraPosition().zoom, MapType.GAODE);
    }

    @Override
    public void setZoom(float zoom) {
        float gaodeZoom = ZoomStandardization.fromStandardZoom(zoom, MapType.GAODE);
        getMap().animateCamera(CameraUpdateFactory.zoomTo(gaodeZoom));
    }

    @Override
    public void zoomOut() {
        getMap().animateCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public void zoomIn() {
        getMap().animateCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public float getMaxZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMaxZoomLevel(), MapType.GAODE);
    }

    @Override
    public float getMinZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMinZoomLevel(), MapType.GAODE);
    }

    @Override
    public float getOverlook() {
        return getMap().getCameraPosition().tilt;
    }

    @Override
    public void setOverlook(float overlook) {
        getMap().animateCamera(CameraUpdateFactory.changeTilt(overlook));
    }

    @Override
    public float getRotate() {
        return getMap().getCameraPosition().bearing;
    }

    @Override
    public void setRotate(float rotate) {
        getMap().animateCamera(CameraUpdateFactory.changeBearing(rotate));
    }

    @Override
    public CameraPosition getCameraPosition() {
        return GaodeDataConverter.toCameraPosition(getMap().getCameraPosition());
    }

    @Override
    public void setCameraPosition(CameraPosition position) {
        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(GaodeDataConverter.fromCameraPosition(position)));
    }

    @Override
    public void setMarkerClickListener(final MarkerClickListener listener) {
        getMap().setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (listener != null) {
                    MapMarker mapMarker = findMapMarker(marker);
                    if (mapMarker != null) {
                        return listener.onMarkClick(mapMarker);
                    }
                }

                return false;
            }
        });
    }

    @Override
    public void setMapClickListener(final MapClickListener listener) {
        getMap().setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listener != null) {
                    listener.onMapClick(GaodeDataConverter.toMapPoint(latLng));
                }
            }
        });
    }

    @Override
    public void setInfoWindowInflater(final InfoWindowInflater inflater) {
        getMap().setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (inflater != null) {
                    MapMarker mapMarker = findMapMarker(marker);
                    if (mapMarker != null) {
                        return inflater.inflate(mapMarker);
                    }
                }
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (inflater != null) {
                    MapMarker mapMarker = findMapMarker(marker);
                    if (mapMarker != null) {
                        return inflater.inflate(mapMarker);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void addMarker(MapMarker marker) {
        MapPoint point = marker.getMapPoint().copy(MapType.GAODE.getCoordinateType());
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();

        Marker rawMarker = getMap().addMarker(new MarkerOptions()
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .title(marker.getTitle())
                .snippet(marker.getContent())
                .position(new LatLng(latitude, longitude)));
        marker.setRawMarker(MapType.GAODE, rawMarker);

        if (marker.isInfoWindowVisible()) {
            setInfoWindow(marker);
        } else {
            rawMarker.hideInfoWindow();
        }

        mMapMarkers.add(marker);
    }

    @Override
    public void removeMarker(MapMarker marker) {
        Marker rawMarker = (Marker) marker.getRawMarker(MapType.GAODE);
        rawMarker.remove();

        mMapMarkers.remove(marker);
    }

    @Override
    public List<MapMarker> getMarkers() {
        return mMapMarkers;
    }

    @Override
    public void clearMarkers() {
        for (MapMarker mapMarker : mMapMarkers) {
            Marker rawMarker = (Marker) mapMarker.getRawMarker(MapType.GAODE);
            rawMarker.remove();
        }

        mMapMarkers.clear();
    }

    @Override
    public void setMarkerVisible(MapMarker marker, boolean visible) {
        Marker rawMarker = (Marker) marker.getRawMarker(MapType.GAODE);
        rawMarker.setVisible(visible);
    }

    @Override
    public void clearMap() {
        getMap().clear();
    }

    @Override
    public void hideInfoWindow() {
        if (mShowingInfoWindowMapMarker != null) {
            mShowingInfoWindowMapMarker.setInfoWindowVisible(false);
            Marker rawMarker = (Marker) mShowingInfoWindowMapMarker.getRawMarker(MapType.GAODE);
            rawMarker.hideInfoWindow();
        }
    }

    @Override
    public void showInfoWindow(MapMarker mapMarker) {
        mapMarker.setInfoWindowVisible(true);

        setInfoWindow(mapMarker);
    }

    private void setInfoWindow(MapMarker mapMarker) {
        hideInfoWindow();
        mShowingInfoWindowMapMarker = mapMarker;
        Marker rawMarker = (Marker) mapMarker.getRawMarker(MapType.GAODE);
        rawMarker.showInfoWindow();
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
        LatLng latLng = getMap().getProjection().fromScreenLocation(point);
        return new MapPoint(latLng.latitude, latLng.longitude, MapType.GAODE.getCoordinateType());
    }

    @Override
    public Point mapPointToGraphicPoint(MapPoint point) {
        MapPoint copy = point.copy(MapType.GAODE.getCoordinateType());
        getMap().getProjection().toScreenLocation(new LatLng(copy.getLatitude(), copy.getLongitude()));
        return null;
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
    public Polygon addPolygon(Polygon polygon) {
        return null;
    }

    @Override
    public void removePolygon(Polygon p) {

    }

    @Override
    public void addPolyline(PolyLine polyline) {

    }

    @Override
    public void removePolyline(PolyLine p) {

    }

    @Override
    public void addCircle(Circle circle) {

    }

    @Override
    public void removeCircle(Circle circle) {

    }

    private MapMarker findMapMarker(Marker marker) {
        for (MapMarker mapMarker : mMapMarkers) {
            if (marker.equals(mapMarker.getRawMarker(MapType.GAODE))) {
                return mapMarker;
            }
        }
        return null;
    }
}