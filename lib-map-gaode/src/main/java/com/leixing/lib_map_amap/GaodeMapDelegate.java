package com.leixing.lib_map_amap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
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
import com.ray.lib_map.extern.MapConfig;
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
 * @author : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GaodeMapDelegate implements MapDelegate {
    private static final String MAP_VIEW_BUNDLE_KEY = "gaode_map_view_bundle_key";

    private static final int MIN_POINT_SIZE_IN_POLYLINE = 2;

    private final Context mContext;
    private final List<MapMarker> mMapMarkers;
    private MapView mMapView;
    private MapMarker mShowingInfoWindowMapMarker;
    private List<Polyline> mPolylines;
    private MapLoadListener mMapLoadListener;
    private List<Graph> mGraphs;
    private final String mCoordinateType;
    private final MapConfig mMapConfig;
    private final String mMapType;

    public GaodeMapDelegate(Context context, MapConfig mapConfig) {
        mContext = context;
        mMapConfig = mapConfig;
        mCoordinateType = mapConfig.getCoordinateType();
        mMapType = mapConfig.getName();

        mMapMarkers = new ArrayList<>();
        mPolylines = new ArrayList<>();
        mGraphs = new ArrayList<>();
        mMapView = new MapView(mContext);

        setListeners();
    }

    private void setListeners() {
        getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (mMapLoadListener != null) {
                    mMapLoadListener.onMapLoaded();
                }
            }
        });
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
        mMapLoadListener = listener;
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
        return new MapPoint(cameraPosition.target.latitude, cameraPosition.target.longitude, mCoordinateType);
    }

    @Override
    public void setPosition(MapPoint mapPoint) {
        MapPoint gaodePoint = mapPoint.as(mCoordinateType);
        LatLng latLng = new LatLng(gaodePoint.getLatitude(), gaodePoint.getLongitude());
        getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public float getZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getCameraPosition().zoom, mMapType);
    }

    @Override
    public void setZoom(float zoom) {
        float gaodeZoom = ZoomStandardization.fromStandardZoom(zoom, mMapType);
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
        return ZoomStandardization.toStandardZoom(getMap().getMaxZoomLevel(), mMapType);
    }

    @Override
    public float getMinZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMinZoomLevel(), mMapType);
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
        doAddMarker(marker);
        mMapMarkers.add(marker);
    }

    private void doAddMarker(MapMarker marker) {
        MapPoint point = marker.getMapPoint().as(mMapType);
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();

        Marker rawMarker = getMap().addMarker(new MarkerOptions()
                .anchor(marker.getAnchorX(), marker.getAnchorY())
                .icon(BitmapDescriptorFactory.fromBitmap(marker.getIcon()))
                .title(marker.getTitle())
                .snippet(marker.getContent())
                .position(new LatLng(latitude, longitude)));
        marker.setRawMarker(mMapType, rawMarker);

        if (marker.isInfoWindowVisible()) {
            setInfoWindow(marker);
        } else {
            rawMarker.hideInfoWindow();
        }
    }

    @Override
    public void removeMarker(MapMarker marker) {
        removeRawMarker(marker);
        mMapMarkers.remove(marker);
    }

    private void removeRawMarker(MapMarker mapMarker) {
        Marker rawMarker = (Marker) mapMarker.getRawMarker(mMapType);
        if (rawMarker == null) {
            return;
        }
        rawMarker.remove();
        mapMarker.setRawMarker(mMapType, null);
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
    public void setMarkerVisible(MapMarker marker, boolean visible) {
        Marker rawMarker = (Marker) marker.getRawMarker(mMapType);
        rawMarker.setVisible(visible);
    }

    @Override
    public void clearMap() {
        getMap().clear();
    }

    @Override
    public void showInfoWindow(MapMarker mapMarker) {
        ((Marker) mapMarker.getRawMarker(mMapType)).setToTop();
        mapMarker.setInfoWindowVisible(true);
        setInfoWindow(mapMarker);
    }

    @Override
    public void hideInfoWindow() {
        if (mShowingInfoWindowMapMarker != null) {
            mShowingInfoWindowMapMarker.setInfoWindowVisible(false);
            Marker rawMarker = (Marker) mShowingInfoWindowMapMarker.getRawMarker(mMapType);
            if (rawMarker != null) {
                rawMarker.hideInfoWindow();
            }
            mShowingInfoWindowMapMarker = null;
        }
    }

    private void setInfoWindow(MapMarker mapMarker) {
        hideInfoWindow();
        mShowingInfoWindowMapMarker = mapMarker;
        Marker rawMarker = (Marker) mapMarker.getRawMarker(mMapType);
        rawMarker.showInfoWindow();
    }

    @Override
    public void screenShotAndSave(String saveFilePath, final MapScreenCaptureListener listener) {
        getMap().getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {
                if (listener != null) {
                    listener.onScreenCaptured(bitmap);
                }
            }
        });
    }

    @Override
    public MapPoint graphicPointToMapPoint(Point point) {
        LatLng latLng = getMap().getProjection().fromScreenLocation(point);
        return new MapPoint(latLng.latitude, latLng.longitude, mCoordinateType);
    }

    @Override
    public Point mapPointToGraphicPoint(MapPoint point) {
        MapPoint copy = point.copy(mCoordinateType);
        LatLng latLng = new LatLng(copy.getLatitude(), copy.getLongitude());
        return getMap().getProjection().toScreenLocation(latLng);
    }

    @Override
    public void setZoomControlsEnabled(boolean enable) {
        getMap().getUiSettings().setZoomControlsEnabled(enable);
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
        MapPoint center = circle.getCenter().copy(mCoordinateType);
        LatLng latLng = new LatLng(center.getLatitude(), center.getLongitude());
        CircleOptions options = new CircleOptions()
                .zIndex(circle.getZIndex())
                .strokeColor(circle.getStrokeColor())
                .strokeWidth(circle.getStrokeWidth())
                .fillColor(circle.getFillColor())
                .center(latLng)
                .radius(circle.getRadius());


        com.amap.api.maps.model.Circle rawCircle = getMap().addCircle(options);
        circle.setRawGraph(mMapType, rawCircle);
    }

    private void addPolygon(Polygon polygon) {
        List<LatLng> points = GaodeDataConverter.fromMapPoints(polygon.getPoints());
        PolygonOptions options = new PolygonOptions()
                .zIndex(polygon.getZIndex())
                .strokeColor(polygon.getStrokeColor())
                .strokeWidth(polygon.getStrokeWidth())
                .fillColor(polygon.getFillColor())
                .addAll(points);

        com.amap.api.maps.model.Polygon rawPolygon = getMap().addPolygon(options);
        polygon.setRawGraph(mMapType, rawPolygon);
    }

    @Override
    public void removeGraph(Graph graph) {
        if (graph instanceof Circle) {
            removeCircle((Circle) graph);
        } else if (graph instanceof Polygon) {
            removePolygon((Polygon) graph);
        }

        mGraphs.remove(graph);
    }

    private void removeCircle(Circle circle) {
        com.amap.api.maps.model.Circle rawCircle = (com.amap.api.maps.model.Circle) circle.getRawGraph(mMapType);
        if (rawCircle != null) {
            rawCircle.remove();
            circle.setRawGraph(mMapType, null);
        }
    }

    private void removePolygon(Polygon polygon) {
        com.amap.api.maps.model.Polygon rawPolygon = (com.amap.api.maps.model.Polygon) polygon.getRawGraph(mMapType);
        if (rawPolygon != null) {
            rawPolygon.remove();
            polygon.setRawGraph(mMapType, null);
        }
    }

    @Override
    public void addPolyline(Polyline polyline) {
        List<LatLng> latLngList = GaodeDataConverter.fromMapPoints(polyline.getPoints());
        int pointSize = latLngList.size();
        if (pointSize < MIN_POINT_SIZE_IN_POLYLINE) {
            return;
        }
        int zIndex = polyline.getZIndex();

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
            applyBasePolylineOptions(polylineOptions, points, texture, zIndex);
            if (texture instanceof ColorTexture) {
                applyColorPolylineOptions(polylineOptions, (ColorTexture) texture);
            } else if (texture instanceof BitmapTexture) {
                applyBitmapPolylineOptions(polylineOptions, (BitmapTexture) texture);
            }

            polyline.addRawPolyline(mMapType, getMap().addPolyline(polylineOptions));

        }

        mPolylines.add(polyline);
    }

    private void applyBasePolylineOptions(PolylineOptions polylineOptions, List<LatLng> points, PolylineTexture texture, int zIndex) {
        polylineOptions
                .addAll(points)
                .width(texture.getWidth())
                .setDottedLine(texture.isDotted())
                .zIndex(zIndex);
    }

    private void applyColorPolylineOptions(PolylineOptions polylineOptions, ColorTexture texture) {
        polylineOptions.color(texture.getColor());
    }

    private void applyBitmapPolylineOptions(PolylineOptions polylineOptions, BitmapTexture texture) {
        polylineOptions.setCustomTexture(BitmapDescriptorFactory.fromBitmap(texture.getBitmap()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removePolyline(Polyline polyline) {
        List<com.amap.api.maps.model.Polyline> rawPolylines = (List<com.amap.api.maps.model.Polyline>) polyline.getRawPolylines(mMapType);
        if (rawPolylines == null) {
            return;
        }

        Iterator<com.amap.api.maps.model.Polyline> iterator = rawPolylines.iterator();
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
            if (marker.equals(mapMarker.getRawMarker(mMapType))) {
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

    private void removeRawPolyline(Polyline polyline) {
        @SuppressWarnings("unchecked")
        List<com.amap.api.maps.model.Polyline> rawPolylines = (List<com.amap.api.maps.model.Polyline>) polyline.getRawPolylines(mMapType);
        for (com.amap.api.maps.model.Polyline polyline1 : rawPolylines) {
            polyline1.remove();
        }
        rawPolylines.clear();
    }

    @Override
    public void adjustCamera(List<MapPoint> mapPoints, int padding) {
        if (mapPoints == null) {
            return;
        }

        List<LatLng> latLngs = GaodeDataConverter.fromMapPoints(mapPoints);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();

        getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding * 10));
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
}