package com.ray.lib_map.impl.baidu;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.Circle;
import com.ray.lib_map.entity.MapLine;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapOverlay;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Polygon;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.extern.ZoomStandardization;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MarkerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 百度地图实现类
 */

public class BaiduMapDelegate implements MapDelegate {
    private static final String MAP_VIEW_BUNDLE_KEY = "baidu_map_view_bundle_key";
    private static boolean sHasInited;
    private final Context mContext;
    private final List<MapMarker> mMapMarkers;
    private MapView mMapView;
    private InfoWindowInflater mInfoWindowInflater;
    private InfoWindowClickListener mInfoWindowClickListener;
    private MapMarker mShowingInfoWindowMapMarker;

    public BaiduMapDelegate(Context context) {
        if (!sHasInited) {
            SDKInitializer.initialize(context.getApplicationContext());
            CoordType coordType = getCoordinateType();
            SDKInitializer.setCoordType(coordType);
            sHasInited = true;
        }

        mContext = context;
        mMapView = new MapView(mContext);

        mMapMarkers = new ArrayList<>();
    }

    private CoordType getCoordinateType() {
        CoordType coordType = null;
        switch (MapType.BAIDU.getCoordinateType()) {
            case GCJ02:
                coordType = CoordType.GCJ02;
                break;
            case BD09:
                coordType = CoordType.BD09LL;
                break;
        }
        if (coordType == null) {
            throw new IllegalStateException("unknown coordinate type");
        }
        return coordType;
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    private BaiduMap getMap() {
        return mMapView.getMap();
    }

    @Override
    public void clearMap() {
        getMap().clear();
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
    public void setMapLoadListener(final MapLoadListener listener) {
        getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
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
        getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                if (listener != null) {
                    listener.onCameraMoving(BaiduDataConverter.toCameraPosition(mapStatus));
                }
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (listener != null) {
                    listener.onCameraMoved(BaiduDataConverter.toCameraPosition(mapStatus));
                }
            }
        });
    }

    @Override
    public void setInfoWindowClickListener(InfoWindowClickListener listener) {
        mInfoWindowClickListener = listener;
    }

    @Override
    public void setMapScreenCaptureListener(MapScreenCaptureListener listener) {

    }

    @Override
    public MapPoint getPosition() {
        LatLng target = getMap().getMapStatus().target;
        return new MapPoint(target.latitude, target.longitude, MapType.BAIDU.getCoordinateType());
    }

    @Override
    public void setPosition(MapPoint mapPoint) {
        MapPoint baiduPoint = mapPoint.copy(MapType.BAIDU.getCoordinateType());
        LatLng latLng = new LatLng(baiduPoint.getLatitude(), baiduPoint.getLongitude());
        com.baidu.mapapi.map.MapStatus status = new com.baidu.mapapi.map.MapStatus.Builder(getMap().getMapStatus()).target(latLng).build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        getMap().animateMapStatus(update);
    }

    @Override
    public float getZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMapStatus().zoom, MapType.BAIDU);
    }

    @Override
    public void setZoom(float zoom) {
        float baiduZoom = ZoomStandardization.fromStandardZoom(zoom, MapType.BAIDU);
        com.baidu.mapapi.map.MapStatus status = new com.baidu.mapapi.map.MapStatus.Builder(getMap().getMapStatus()).zoom(baiduZoom).build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        getMap().animateMapStatus(update);
    }

    @Override
    public void zoomOut() {
        MapStatusUpdate u = MapStatusUpdateFactory.zoomOut();
        getMap().animateMapStatus(u);
    }

    @Override
    public void zoomIn() {
        MapStatusUpdate u = MapStatusUpdateFactory.zoomIn();
        getMap().animateMapStatus(u);
    }


    @Override
    public float getMaxZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMaxZoomLevel(), MapType.BAIDU);
    }

    @Override
    public float getMinZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMinZoomLevel(), MapType.BAIDU);
    }

    @Override
    public float getOverlook() {
        return BaiduDataConverter.toStandardOverlook(getMap().getMapStatus().overlook);
    }

    @Override
    public void setOverlook(float overlook) {
        float baiduOverlook = BaiduDataConverter.fromStandardOverlook(overlook);
        com.baidu.mapapi.map.MapStatus status = new com.baidu.mapapi.map.MapStatus.Builder(getMap().getMapStatus()).overlook(baiduOverlook).build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        getMap().animateMapStatus(update);
    }

    @Override
    public float getRotate() {
        return getMap().getMapStatus().rotate;
    }

    @Override
    public void setRotate(float rotate) {
        com.baidu.mapapi.map.MapStatus status = new com.baidu.mapapi.map.MapStatus.Builder(getMap().getMapStatus()).rotate(rotate).build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        getMap().animateMapStatus(update);
    }

    @Override
    public CameraPosition getCameraPosition() {
        MapStatus mapStatus = getMap().getMapStatus();

        CameraPosition position = new CameraPosition();
        position.setPosition(new MapPoint(mapStatus.target.latitude, mapStatus.target.longitude, MapType.BAIDU.getCoordinateType()));
        position.setRotate(mapStatus.rotate);
        position.setZoom(ZoomStandardization.toStandardZoom(mapStatus.zoom, MapType.BAIDU));
        position.setOverlook(BaiduDataConverter.toStandardOverlook(mapStatus.overlook));

        return position;
    }

    @Override
    public void setCameraPosition(CameraPosition position) {
        MapPoint mapPoint = position.getPosition().copy(MapType.BAIDU.getCoordinateType());
        float overlook = BaiduDataConverter.fromStandardOverlook(position.getOverlook());
        float baiduZoom = ZoomStandardization.fromStandardZoom(position.getZoom(), MapType.BAIDU);
        LatLng latLng = new LatLng(mapPoint.getLatitude(), mapPoint.getLongitude());

        com.baidu.mapapi.map.MapStatus status = new com.baidu.mapapi.map.MapStatus
                .Builder(getMap().getMapStatus())
                .target(latLng)
                .rotate(position.getRotate())
                .zoom(baiduZoom)
                .overlook(overlook)
                .build();

        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        getMap().animateMapStatus(update);
    }

    @Override
    public MapPoint graphicPointToMapPoint(Point point) {
        LatLng latLng = getMap().getProjection().fromScreenLocation(point);
        return new MapPoint(latLng.latitude, latLng.longitude, MapType.BAIDU.getCoordinateType());
    }

    @Override
    public Point mapPointToGraphicPoint(MapPoint point) {
        MapPoint copy = point.copy(MapType.BAIDU.getCoordinateType());
        return getMap().getProjection().toScreenLocation(new LatLng(copy.getLatitude(), copy.getLongitude()));
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
        return getMap().getUiSettings().isOverlookingGesturesEnabled();
    }

    @Override
    public void setOverlookGestureEnable(boolean enable) {
        getMap().getUiSettings().setOverlookingGesturesEnabled(enable);
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
    public void screenShotAndSave(String saveFilePath) {

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
    public void setMarkerClickListener(final MarkerClickListener listener) {
        getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
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
    public void setInfoWindowInflater(InfoWindowInflater inflater) {
        mInfoWindowInflater = inflater;
    }

    @Override
    public void addMarker(MapMarker marker) {
        MapPoint point = marker.getMapPoint().copy(MapType.BAIDU.getCoordinateType());
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        Overlay overlay = getMap().addOverlay(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .title(marker.getTitle()));
        marker.setRawMarker(MapType.BAIDU, overlay);

        if (marker.isInfoWindowVisible()) {
            setInfoWindow(marker);
        }

        mMapMarkers.add(marker);
    }

    @Override
    public void removeMarker(MapMarker mapMarker) {
        Overlay overlay = (Overlay) mapMarker.getRawMarker(MapType.BAIDU);
        overlay.remove();
        mMapMarkers.remove(mapMarker);
    }

    @Override
    public List<MapMarker> getMarkers() {
        return mMapMarkers;
    }

    @Override
    public void clearMarkers() {
        for (MapMarker mapMarker : mMapMarkers) {
            Overlay overlay = (Overlay) mapMarker.getRawMarker(MapType.BAIDU);
            overlay.remove();
        }
        mMapMarkers.clear();
    }

    @Override
    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {
        Overlay overlay = (Overlay) mapMarker.getRawMarker(MapType.BAIDU);
        overlay.setVisible(visible);
    }

    @Override
    public void hideInfoWindow() {
        getMap().hideInfoWindow();
        if (mShowingInfoWindowMapMarker != null) {
            mShowingInfoWindowMapMarker.setInfoWindowVisible(false);
        }
    }

    @Override
    public void showInfoWindow(MapMarker mapMarker) {
        mapMarker.setInfoWindowVisible(true);
        setInfoWindow(mapMarker);
    }

    private void setInfoWindow(final MapMarker mapMarker) {
        hideInfoWindow();
        mShowingInfoWindowMapMarker = mapMarker;

        MapPoint point = mapMarker.getMapPoint().copy(MapType.BAIDU.getCoordinateType());
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        View view = mInfoWindowInflater.inflate(mapMarker);
        int y = -mapMarker.getIcon().getHeight();
        InfoWindow infoWindow = new InfoWindow(view, latLng, y);
        getMap().showInfoWindow(infoWindow);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfoWindowClickListener != null) {
                    mInfoWindowClickListener.onInfoWindowClick(mapMarker);
                }
            }
        });
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

    private MapMarker findMapMarker(Marker marker) {
        for (MapMarker mapMarker : mMapMarkers) {
            if (marker.equals(mapMarker.getRawMarker(MapType.BAIDU))) {
                return mapMarker;
            }
        }
        return null;
    }
}
