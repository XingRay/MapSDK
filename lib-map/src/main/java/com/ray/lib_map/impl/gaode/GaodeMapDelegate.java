package com.ray.lib_map.impl.gaode;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
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
 * Date        : 2017-07-13
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GaodeMapDelegate implements MapDelegate {
    private static final String MAP_VIEW_BUNDLE_KEY = "gaode_map_view_bundle_key";
    private final Context mContext;
    private MapView mMapView;

    public GaodeMapDelegate(Context context) {
        mContext = context;
        mMapView = new MapView(mContext);
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
    public void setMapLoadListener(final MapViewInterface.MapLoadListener listener) {
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
    public void setCameraMoveListener(final MapViewInterface.CameraMoveListener listener) {
        getMap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (listener != null) {
                    listener.onCameraMoving(getMapPoint(cameraPosition));
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (listener != null) {
                    listener.onCameraMoved(getMapPoint(cameraPosition));
                }
            }
        });
    }

    @Override
    public void setAnimationListener(MapViewInterface.AnimationListener listener) {
    }

    @Override
    public void setMarkerClickListener(final MapViewInterface.MarkerClickListener listener) {
        getMap().setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return listener != null && listener.onMarkClick(marker, getMapPoint(marker));
            }
        });
    }

    @Override
    public void setInfoWindowClickListener(final MapViewInterface.InfoWindowClickListener listener) {
        getMap().setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (listener != null) {
                    listener.onInfoWindowClick(marker, getMapPoint(marker));
                }
            }
        });
    }

    @Override
    public void setMapScreenCaptureListener(MapViewInterface.MapScreenCaptureListener listener) {
    }

    private MapPoint getMapPoint(CameraPosition cameraPosition) {
        double latitude = cameraPosition.target.latitude;
        double longitude = cameraPosition.target.longitude;
        return new MapPoint(latitude, longitude, MapType.GAODE.getCoordinateType());
    }

    private MapPoint getMapPoint(Marker marker) {
        LatLng position = marker.getPosition();
        return new MapPoint(position.latitude, position.longitude, MapType.GAODE.getCoordinateType());
    }

    @Override
    public void clearMap() {
        getMap().clear();
    }

    @Override
    public void screenShotAndSave(String saveFilePath) {

    }

    @Override
    public void animateTo(MapPoint mapPoint, float zoom, final MapViewInterface.AnimationListener listener) {
        LatLng latLng = new LatLng(mapPoint.getLatitude(), mapPoint.getLongitude());
        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onFinished();
                }
            }

            @Override
            public void onCancel() {
                if (listener != null) {
                    listener.onCanceled();
                }
            }
        });
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
    public MapPoint getCameraPosition() {
        CameraPosition cameraPosition = getMap().getCameraPosition();
        return new MapPoint(cameraPosition.target.latitude, cameraPosition.target.longitude, MapType.GAODE.getCoordinateType());
    }

    @Override
    public MapPoint fromScreenLocation(Point point) {
        LatLng latLng = getMap().getProjection().fromScreenLocation(point);
        return new MapPoint(latLng.latitude, latLng.longitude, MapType.GAODE.getCoordinateType());
    }

    @Override
    public Point toScreenLocation(MapPoint point) {
        MapPoint copy = point.copy(MapType.GAODE.getCoordinateType());
        getMap().getProjection().toScreenLocation(new LatLng(copy.getLatitude(), copy.getLongitude()));
        return null;
    }

    @Override
    public void setGestureEnable(boolean enable) {
        getMap().getUiSettings().setAllGesturesEnabled(enable);
    }

    @Override
    public void setZoomControlEnable(boolean enable) {
        getMap().getUiSettings().setZoomControlsEnabled(enable);
    }

    @Override
    public void setZoomGestureEnable(boolean enable) {
        getMap().getUiSettings().setZoomGesturesEnabled(enable);
    }

    @Override
    public void zoomTo(float zoom) {
        getMap().animateCamera(CameraUpdateFactory.zoomTo(zoom));
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
    public float getCurrentZoom() {
        return getMap().getCameraPosition().zoom;
    }

    @Override
    public float getMaxZoomLevel() {
        return getMap().getMaxZoomLevel();
    }

    @Override
    public float getMinZoomLevel() {
        return getMap().getMinZoomLevel();
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
        MapPoint point = marker.getMapPoint().copy(MapType.GAODE.getCoordinateType());
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();

        Marker addMarker = getMap().addMarker(new MarkerOptions()
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .title(marker.getTitle())
                .snippet(marker.getSubTitle())
                .position(new LatLng(latitude, longitude)));
        marker.setRawMarker(MapType.GAODE, addMarker);
    }

    @Override
    public void removeMarker(MapMarker marker) {
        Marker rawMarker = (Marker) marker.getRawMarker(MapType.GAODE);
        rawMarker.remove();
        marker.removeRawMarker(MapType.GAODE);
    }

    @Override
    public void setMarkerVisible(MapMarker marker, boolean visible) {

    }

    @Override
    public Polygon addPolygon(Polygon polygon) {
        return null;
    }

    @Override
    public void removePolygon(Polygon p) {

    }

    @Override
    public void addPolyline(MapLine polyline) {

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