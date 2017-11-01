package com.ray.lib_map.impl.baidu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.graph.Circle;
import com.ray.lib_map.entity.graph.Graph;
import com.ray.lib_map.entity.graph.Polygon;
import com.ray.lib_map.entity.polyline.BitmapTexture;
import com.ray.lib_map.entity.polyline.ColorTexture;
import com.ray.lib_map.entity.polyline.Polyline;
import com.ray.lib_map.entity.polyline.PolylineHelper;
import com.ray.lib_map.entity.polyline.PolylineTexture;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.extern.ZoomStandardization;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MapLongClickListener;
import com.ray.lib_map.listener.MapScreenCaptureListener;
import com.ray.lib_map.listener.MapSwitchListener;
import com.ray.lib_map.listener.MarkerClickListener;

import java.util.ArrayList;
import java.util.Iterator;
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
    private final List<Polyline> mPolylines;
    private MapView mMapView;
    private InfoWindowInflater mInfoWindowInflater;
    private InfoWindowClickListener mInfoWindowClickListener;
    private MapMarker mShowingInfoWindowMapMarker;
    private MapLoadListener mMapLoadListener;
    private List<Graph> mGraphs;

    public BaiduMapDelegate(Context context) {
        if (!sHasInited) {
            SDKInitializer.initialize(context.getApplicationContext());
            SDKInitializer.setCoordType(getCoordinateType());
            sHasInited = true;
        }

        mContext = context;

        mMapMarkers = new ArrayList<>();
        mPolylines = new ArrayList<>();
        mGraphs = new ArrayList<>();

        mMapView = new MapView(mContext);
        setListeners();
    }

    private void setListeners() {
        getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (mMapLoadListener != null) {
                    mMapLoadListener.onMapLoaded();
                }
            }
        });
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
    public void onSwitchIn(Bundle savedInstanceState, MapSwitchListener listener) {
        mMapView = new MapView(mContext);
        setListeners();
        onCreate(savedInstanceState);
        onResume();
        listener.onMapSwitch();
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
        mMapLoadListener = listener;
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
    public void setMapClickListener(final MapClickListener listener) {
        getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listener != null) {
                    listener.onMapClick(BaiduDataConverter.toMapPoint(latLng));
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (listener != null) {
                    listener.onMapClick(BaiduDataConverter.toMapPoint(mapPoi.getPosition()));
                }
                return true;
            }
        });
    }

    @Override
    public void setMapLongClickListener(final MapLongClickListener listener) {
        getMap().setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listener != null) {
                    listener.onMapLongClick(BaiduDataConverter.toMapPoint(latLng));
                }
            }
        });
    }

    @Override
    public void setInfoWindowClickListener(InfoWindowClickListener listener) {
        mInfoWindowClickListener = listener;
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
        getMap().setMapStatus(update);
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
    public void setZoomControlsEnabled(boolean enable) {
        mMapView.showZoomControls(enable);
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
    public void screenShotAndSave(String saveFilePath, final MapScreenCaptureListener listener) {
        getMap().snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                if (listener != null) {
                    listener.onScreenCaptured(bitmap);
                }
            }
        });
    }

    @Override
    public void setMarkerClickListener(final MarkerClickListener listener) {
        getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                boolean result = false;
                MapMarker mapMarker = findMapMarker(marker);
                if (listener != null) {
                    if (mapMarker != null) {
                        result = listener.onMarkClick(mapMarker);
                    }
                }
                if (mapMarker != null) {
                    mShowingInfoWindowMapMarker = mapMarker;
                }
                return result;
            }
        });
    }

    @Override
    public void setInfoWindowInflater(InfoWindowInflater inflater) {
        mInfoWindowInflater = inflater;
    }

    @Override
    public void addMarker(MapMarker marker) {
        doAddMarker(marker);
        mMapMarkers.add(marker);
    }

    private void doAddMarker(MapMarker marker) {
        MapPoint point = marker.getMapPoint().copy(MapType.BAIDU.getCoordinateType());
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        Overlay overlay = getMap().addOverlay(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .title(marker.getTitle())
                .zIndex(marker.getZIndex()));
        marker.setRawMarker(MapType.BAIDU, overlay);

        if (marker.isInfoWindowVisible()) {
            setInfoWindow(marker);
        }
    }

    @Override
    public void removeMarker(MapMarker mapMarker) {
        removeRawMarker(mapMarker);
        mMapMarkers.remove(mapMarker);
    }

    private void removeRawMarker(MapMarker mapMarker) {
        Overlay overlay = (Overlay) mapMarker.getRawMarker(MapType.BAIDU);
        if (overlay != null) {
            overlay.remove();
            mapMarker.setRawMarker(MapType.BAIDU, null);
        }
    }

    @Override
    public List<MapMarker> getMarkers() {
        return mMapMarkers;
    }

    @Override
    public void clearMarkers() {
        hideInfoWindow();
        for (MapMarker mapMarker : mMapMarkers) {
            removeRawMarker(mapMarker);
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
            mShowingInfoWindowMapMarker = null;
        }
    }

    @Override
    public void showInfoWindow(MapMarker mapMarker) {
        ((Marker) mapMarker.getRawMarker(MapType.BAIDU)).setToTop();
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
        if (mInfoWindowInflater != null) {
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
    }

    @Override
    public void addPolyline(Polyline polyline) {
        List<LatLng> latLngList = BaiduDataConverter.fromMapPoints(polyline.getPoints());
        int zIndex = polyline.getZIndex();
        int pointSize = latLngList.size();
        if (pointSize < 2) {
            return;
        }

        List<PolylineTexture> textures = polyline.getTextures();
        PolylineHelper.sortByIndex(textures);

        for (int i = 0, size = textures.size(); i < size; i++) {
            PolylineTexture texture = textures.get(i);
            if (texture.getIndex() > pointSize - 2) {
                continue;
            }
            //start index  include
            int start = texture.getIndex();
            //end index  exclude
            int end = (i == size - 1) ? pointSize : textures.get(i + 1).getIndex() + 1;

            List<LatLng> points = latLngList.subList(start, end);

            PolylineOptions polylineOptions = new PolylineOptions();
            applyBaseOptions(polylineOptions, points, texture, zIndex);

            if (texture instanceof ColorTexture) {
                applyColorPolylineOptions(polylineOptions, (ColorTexture) texture);
            } else if (texture instanceof BitmapTexture) {
                applyBitmapPolylineOptions(polylineOptions, (BitmapTexture) texture);
            }

            polyline.addRawPolyline(MapType.BAIDU, getMap().addOverlay(polylineOptions));
        }

        mPolylines.add(polyline);
    }

    private void applyBaseOptions(PolylineOptions polylineOptions, List<LatLng> points, PolylineTexture texture, int zIndex) {
        polylineOptions
                .points(points)
                .width(texture.getWidth())
                .dottedLine(texture.isDotted())
                .zIndex(zIndex);
    }

    private void applyColorPolylineOptions(PolylineOptions polylineOptions, ColorTexture texture) {
        polylineOptions.color(texture.getColor());
    }

    private void applyBitmapPolylineOptions(PolylineOptions polylineOptions, BitmapTexture texture) {
        polylineOptions.customTexture(BitmapDescriptorFactory.fromBitmap(texture.getBitmap()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removePolyline(Polyline polyline) {
        List<com.baidu.mapapi.map.Polyline> rawPolylines = (List<com.baidu.mapapi.map.Polyline>) polyline.getRawPolylines(MapType.BAIDU);
        if (rawPolylines == null) {
            return;
        }

        Iterator<com.baidu.mapapi.map.Polyline> iterator = rawPolylines.iterator();
        while (iterator.hasNext()) {
            iterator.next().remove();
            iterator.remove();
        }

        mPolylines.remove(polyline);
    }

    @Override
    public List<Polyline> getPolylines() {
        return mPolylines;
    }

    private MapMarker findMapMarker(Marker marker) {
        for (MapMarker mapMarker : mMapMarkers) {
            if (marker.equals(mapMarker.getRawMarker(MapType.BAIDU))) {
                return mapMarker;
            }
        }
        return null;
    }

    @Override
    public void updateMarker(MapMarker mapMarker) {
        removeRawMarker(mapMarker);
        doAddMarker(mapMarker);
    }

    @Override
    public void clearPolylines() {
        for (Polyline polyline : mPolylines) {
            removeRawPolyline(polyline);
        }
        mPolylines.clear();
    }

    @SuppressWarnings("unchecked")
    private void removeRawPolyline(Polyline polyline) {
        List<com.baidu.mapapi.map.Polyline> rawPolylines = (List<com.baidu.mapapi.map.Polyline>) polyline.getRawPolylines(MapType.GOOGLE);
        for (com.baidu.mapapi.map.Polyline polyline1 : rawPolylines) {
            polyline1.remove();
        }
        rawPolylines.clear();
    }

    @Override
    public void addGraph(Graph graph) {
        if (graph instanceof Circle) {
            addCircle((Circle) graph);
        } else if (graph instanceof Polygon) {
            addPolygon((Polygon) graph);
        }

        mGraphs.add(graph);
    }

    private void addCircle(Circle circle) {
        LatLng point = BaiduDataConverter.fromMapPoint(circle.getCenter());
        CircleOptions options = new CircleOptions()
                .zIndex(circle.getZIndex())
                .fillColor(circle.getFillColor())
                .stroke(new Stroke(circle.getStrokeWidth(), circle.getStrokeColor()))
                .center(point);

        Overlay overlay = getMap().addOverlay(options);
        circle.setRawGraph(MapType.BAIDU, overlay);
    }

    private void addPolygon(Polygon polygon) {
        List<LatLng> points = BaiduDataConverter.fromMapPoints(polygon.getPoints());
        PolygonOptions options = new PolygonOptions()
                .zIndex(polygon.getZIndex())
                .fillColor(polygon.getFillColor())
                .stroke(new Stroke(polygon.getStrokeWidth(), polygon.getStrokeColor()))
                .points(points);

        Overlay overlay = getMap().addOverlay(options);
        polygon.setRawGraph(MapType.BAIDU, overlay);
    }

    @Override
    public void removeGraph(Graph graph) {
        Overlay overlay = (Overlay) graph.getRawGraph(MapType.BAIDU);
        if (overlay != null) {
            overlay.remove();
        }

        mGraphs.remove(graph);
    }

    @Override
    public List<Graph> getGraphs() {
        return mGraphs;
    }

    @Override
    public void clearGraphs() {
        for (Graph graph : mGraphs) {
            removeGraph(graph);
        }

        mGraphs.clear();
    }

    @Override
    public void adjustCamera(List<MapPoint> mapPoints, int padding) {
        if (mapPoints == null) {
            return;
        }

        List<LatLng> latLngs = BaiduDataConverter.fromMapPoints(mapPoints);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();

        getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(bounds));
    }
}
