package com.ray.lib_map.impl.gaode;

import android.content.Context;
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
import com.ray.lib_map.extern.CoordinateType;
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
    private MapViewInterface.AnimationListener mAnimationListener;
    private MapViewInterface.MapScreenCaptureListener mMapScreenCaptureListener;
    private MapView mMapView;

    public GaodeMapDelegate(Context context) {
        mContext = context;
        mMapView = new MapView(mContext);
    }

    private AMap getAMap() {
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
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
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
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
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
        mAnimationListener = listener;
    }

    @Override
    public void setMarkerClickListener(final MapViewInterface.MarkerClickListener listener) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return listener != null && listener.onMarkClick(marker, getMapPoint(marker));
            }
        });
    }

    @Override
    public void setInfoWindowClickListener(final MapViewInterface.InfoWindowClickListener listener) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
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
        mMapScreenCaptureListener = listener;
    }

    private MapPoint getMapPoint(CameraPosition cameraPosition) {
        double latitude = cameraPosition.target.latitude;
        double longitude = cameraPosition.target.longitude;
        return new MapPoint(latitude, longitude, CoordinateType.GCJ02);
    }

    private MapPoint getMapPoint(Marker marker) {
        LatLng position = marker.getPosition();
        return new MapPoint(position.latitude, position.longitude, CoordinateType.GCJ02);
    }

    @Override
    public void clearMap() {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.clear();
    }

    @Override
    public void screenShotAndSave(String saveFilePath) {

    }

    @Override
    public void animateTo(MapPoint mapPoint, final MapViewInterface.AnimationListener listener) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }

        LatLng latLng = new LatLng(mapPoint.getLatitude(), mapPoint.getLongitude());
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), new AMap.CancelableCallback() {
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
        return null;
    }

    @Override
    public void setGestureEnable(boolean enable) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.getUiSettings().setAllGesturesEnabled(enable);
    }

    @Override
    public void setZoomControlsEnabled(boolean enable) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.getUiSettings().setZoomControlsEnabled(enable);
    }

    @Override
    public void setZoomGestureEnable(boolean enable) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }
        aMap.getUiSettings().setZoomGesturesEnabled(enable);
    }

    @Override
    public void zoomTo(float zoom) {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }

        aMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    @Override
    public void zoomOut() {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }

        aMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public void zoomIn() {
        AMap aMap = getAMap();
        if (aMap == null) {
            return;
        }

        aMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public float getCurrentZoom() {
        AMap aMap = getAMap();
        if (aMap == null) {
            return -1;
        }

        CameraPosition cameraPosition = aMap.getCameraPosition();
        if (cameraPosition == null) {
            return -1;
        }

        return cameraPosition.zoom;
    }

    @Override
    public float getMaxZoomLevel() {
        return 19.0f;
    }

    @Override
    public float getMinZoomLevel() {
        return 3.0f;
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
        MapPoint point = marker.getMapPoint().as(CoordinateType.GCJ02);
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();

        Marker addMarker = getAMap().addMarker(new MarkerOptions()
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