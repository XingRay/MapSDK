package com.ray.lib_map.impl.gaode;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.ray.lib_map.MapHolder;
import com.ray.lib_map.MapViewInterface;
import com.ray.lib_map.MarkerInflater;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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

public class GaodeMapHolder implements MapHolder {
    private Reference<MapView> mapViewReference;
    private List<Marker> mMarkers;
    private List<Polyline> mPolylines;
    private MapViewInterface.AnimationListener mAnimationListener;
    private MapViewInterface.MapScreenCaptureListener mMapScreenCaptureListener;
    private final Context mContext;
    private final AMapOptions mOptions;

    public GaodeMapHolder(Context context) {
        mContext = context;
        mOptions = new AMapOptions();
        mOptions.zoomControlsEnabled(false);
        mMarkers = new ArrayList<>();
        mPolylines = new ArrayList<>();
    }

    @Override
    public View getMapView() {
        if (mapViewReference == null) {
            throw new IllegalStateException("must invoke inflateMapView() first");
        }
        return mapViewReference.get();
    }

    @Nullable
    @Override
    public View inflateMapView() {
        MapView mapView = new MapView(mContext, mOptions);
        mapViewReference = new WeakReference<>(mapView);
        return mapView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MapView mapView = mapViewReference.get();
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        MapView mapView = mapViewReference.get();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        MapView mapView = mapViewReference.get();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        MapView mapView = mapViewReference.get();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        MapView mapView = mapViewReference.get();
        if (mapView != null) {
            mapView.onSaveInstanceState(savedInstanceState);
        }
    }

    private AMap getAMap() {
        final MapView mapView = mapViewReference.get();
        return mapView == null ? null : mapView.getMap();
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
                if (listener != null) {
                    return listener.onMarkClick(marker, getMapPoint(marker));
                }
                return false;
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
        return new MapPoint(latitude, longitude);
    }

    private MapPoint getMapPoint(Marker marker) {
        LatLng position = marker.getPosition();
        return new MapPoint(position.latitude, position.longitude);
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
    public void addMarker(MapMarker marker) {
        AMap aMap = getAMap();
        if (aMap != null) {
            Marker addMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(marker.getAnchor().getX(), marker.getAnchor().getY())
                    .icon(BitmapDescriptorFactory.fromResource(marker.getIconResId()))
                    .title(marker.getTitle())
                    .snippet(marker.getSubTitle())
                    .position(new LatLng(marker.getLatitude(), marker.getLongitude())));
            marker.setRawMarker(addMarker);
        }
    }

    @Override
    public void removeMarker(MapMarker marker) {
        AMap aMap = getAMap();
        if (aMap != null) {
            Object raw = marker.getRawMarker();
            if (raw != null && raw instanceof Marker) {
                Marker rawMarker = (Marker) raw;
                rawMarker.remove();
                marker.setRawMarker(null);
            }
        }
    }

    @Override
    public void clearMarker() {

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
    public void addPolylines(MapLine... polylines) {

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
    public void addMarkers(List<MapMarker> mapMarkers) {

    }
}