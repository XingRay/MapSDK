package com.leixing.lib_map_google;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapDelegate;
import com.ray.lib_map.base.FailureCallback;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GoogleMapDelegate implements MapDelegate {
    private static final int CUSTOM_CAP_IMAGE_REF_WIDTH_PX = 50;

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final Dot DOT = new Dot();
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);

    private static final String MAP_VIEW_BUNDLE_KEY = "google_map_view_bundle_key";
    private static final int MAP_LOAD_TIMEOUT_MILLIS = 10000;
    private static boolean sHasInited;
    private final MapView mMapView;
    private MapLoadListener mMapLoadListener;
    private GoogleMap mMap;
    private CameraMoveListener mCameraMoveListener;
    private MarkerClickListener mMarkerClickListener;
    private InfoWindowClickListener mInfoWindowClickListener;
    private MapClickListener mMapClickListener;
    private MapLongClickListener mMapLongClickListener;
    private List<MapMarker> mMapMarkers;
    private List<Polyline> mPolylines;
    private MapMarker mShowingInfoWindowMapMarker;
    private InfoWindowInflater mInfoWindowInflater;
    private MapSwitchListener mMapSwitchListener;
    private boolean mMapLoadedTimeout;
    private List<Graph> mGraphs;
    private boolean mZoomGestureEnable = true;
    private boolean mScrollGestureEnable = true;
    private boolean mRotateGestureEnable = true;
    private boolean mOverlookGestureEnable = true;
    private boolean mZoomControlsEnabled = true;
    private boolean mIsMoving;

    public GoogleMapDelegate(Context context) {
        if (!sHasInited) {
            MapsInitializer.initialize(context);
        }
        sHasInited = true;

        mMapView = new MapView(context);

        mMapMarkers = new ArrayList<>();
        mPolylines = new ArrayList<>();
        mGraphs = new ArrayList<>();
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    @Override
    public void clearMap() {
        getMap().clear();
    }

    @Override
    public void onSwitchOut() {
        onPause();
        onDestroy();
    }

    @Override
    public void onSwitchIn(Bundle savedInstanceState, MapSwitchListener listener) {
        mMapLoadedTimeout = false;
        mMapSwitchListener = listener;
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

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (mMapLoadedTimeout) {
                    return;
                }

                mMap = googleMap;
                onResume();
                setListeners();
                setSettings();
                if (mMapSwitchListener != null) {
                    mMapSwitchListener.onMapSwitch();
                } else if (mMapLoadListener != null) {
                    mMapLoadListener.onMapLoaded();
                }
            }
        });

        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getMap() == null) {
                    mMapLoadedTimeout = true;
                    if (mMapSwitchListener != null) {
                        mMapSwitchListener.onFailure(FailureCallback.ERROR_CODE_MAP_LOAD_TIMEOUT, "��ͼ���볬ʱ");
                    }
                }
            }
        }, MAP_LOAD_TIMEOUT_MILLIS);
    }

    private void setSettings() {
        setZoomControlsEnabled(mZoomControlsEnabled);
        setZoomGestureEnable(mZoomGestureEnable);
        setScrollGestureEnable(mScrollGestureEnable);
        setRotateGestureEnable(mRotateGestureEnable);
        setOverlookGestureEnable(mOverlookGestureEnable);
    }

    private GoogleMap getMap() {
        return mMap;
    }

    private void setListeners() {
//        getMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                if (mMapLoadListener != null) {
//                    mMapLoadListener.onMapLoaded();
//                }
//            }
//        });

//        getMap().setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int reason) {
//
//            }
//        });

        getMap().setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mIsMoving = true;
                if (mCameraMoveListener != null) {
                    mCameraMoveListener.onCameraMoving(getCameraPosition());
                }
            }
        });

        getMap().setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (!mIsMoving) {
                    return;
                }
                mIsMoving = false;
                if (mCameraMoveListener != null) {
                    mCameraMoveListener.onCameraMoved(getCameraPosition());
                }
            }
        });

//        getMap().setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
//            @Override
//            public void onCameraMoveCanceled() {
//
//            }
//        });

        getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                boolean result = false;
                MapMarker mapMarker = findMapMarker(marker);
                if (mMarkerClickListener != null) {
                    if (mapMarker != null) {
                        result = mMarkerClickListener.onMarkClick(mapMarker);
                    }
                }
                if (mapMarker != null) {
                    mShowingInfoWindowMapMarker = mapMarker;
                }
                return result;
            }
        });

        getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMapClickListener != null) {
                    mMapClickListener.onMapClick(GoogleDataConverter.toMapPoint(latLng));
                }
            }
        });

        getMap().setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mMapLongClickListener != null) {
                    MapPoint point = GoogleDataConverter.toMapPoint(latLng);
                    if (latLng != null) {
                        mMapLongClickListener.onMapLongClick(point);
                    }
                }
            }
        });

        getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (mInfoWindowClickListener != null) {
                    MapMarker mapMarker = findMapMarker(marker);
                    if (mapMarker != null) {
                        mInfoWindowClickListener.onInfoWindowClick(mapMarker);
                    }
                }
            }
        });

        getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                MapMarker mapMarker = findMapMarker(marker);
                if (mInfoWindowInflater != null) {
                    return mInfoWindowInflater.inflate(mapMarker);
                }

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
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
        mMapView.onPause();
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
    public void setMapLoadListener(MapLoadListener listener) {
        mMapLoadListener = listener;
    }

    @Override
    public void setCameraMoveListener(CameraMoveListener listener) {
        mCameraMoveListener = listener;
    }

    @Override
    public void setMarkerClickListener(MarkerClickListener listener) {
        mMarkerClickListener = listener;
    }

    @Override
    public void setInfoWindowClickListener(InfoWindowClickListener listener) {
        mInfoWindowClickListener = listener;
    }

    @Override
    public void setMapClickListener(MapClickListener listener) {
        mMapClickListener = listener;
    }

    @Override
    public void setMapLongClickListener(MapLongClickListener listener) {
        mMapLongClickListener = listener;
    }

    @Override
    public boolean isZoomGestureEnable() {
        return mZoomGestureEnable;
//        return getMap().getUiSettings().isZoomGesturesEnabled();
    }

    @Override
    public void setZoomGestureEnable(boolean enable) {
        mZoomGestureEnable = enable;
        if (mMap != null) {
            mMap.getUiSettings().setZoomGesturesEnabled(enable);
        }
    }

    @Override
    public boolean isScrollGestureEnable() {
        return mScrollGestureEnable;
//        return getMap().getUiSettings().isScrollGesturesEnabled();
    }

    @Override
    public void setScrollGestureEnable(boolean enable) {
        mScrollGestureEnable = enable;
        if (mMap != null) {
            mMap.getUiSettings().setScrollGesturesEnabled(enable);
        }
    }

    @Override
    public boolean isRotateGestureEnable() {
        return mRotateGestureEnable;
//        return getMap().getUiSettings().isRotateGesturesEnabled();
    }

    @Override
    public void setRotateGestureEnable(boolean enable) {
        mRotateGestureEnable = enable;
        if (mMap != null) {
            mMap.getUiSettings().setRotateGesturesEnabled(enable);
        }
    }

    @Override
    public boolean isOverlookGestureEnable() {
        return mOverlookGestureEnable;
//        return getMap().getUiSettings().isTiltGesturesEnabled();
    }

    @Override
    public void setOverlookGestureEnable(boolean enable) {
        mOverlookGestureEnable = enable;
        if (mMap != null) {
            mMap.getUiSettings().setTiltGesturesEnabled(enable);
        }
    }

    @Override
    public float getOverlook() {
        return getMap().getCameraPosition().tilt;
    }

    @Override
    public void setOverlook(float overlook) {
        com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition.Builder(getMap().getCameraPosition())
                .tilt(overlook)
                .build();

        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public float getRotate() {
        return getMap().getCameraPosition().bearing;
    }

    @Override
    public void setRotate(float rotate) {
        com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition.Builder(getMap().getCameraPosition())
                .bearing(rotate)
                .build();

        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public MapPoint getPosition() {
        LatLng target = getMap().getCameraPosition().target;
        return GoogleDataConverter.toMapPoint(target);
    }

    @Override
    public void setPosition(MapPoint mapPoint) {
        LatLng latLng = GoogleDataConverter.fromMapPoint(mapPoint);
        com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition.Builder(getMap().getCameraPosition())
                .target(latLng)
                .build();

        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public CameraPosition getCameraPosition() {
        com.google.android.gms.maps.model.CameraPosition cameraPosition = getMap().getCameraPosition();
        MapPoint mapPoint = GoogleDataConverter.toMapPoint(cameraPosition.target);

        CameraPosition position = new CameraPosition();
        position.setPosition(mapPoint);
        position.setZoom(ZoomStandardization.toStandardZoom(cameraPosition.zoom, MapType.GOOGLE));
        position.setRotate(cameraPosition.bearing);
        position.setOverlook(cameraPosition.tilt);

        return position;
    }

    @Override
    public void setCameraPosition(final CameraPosition position) {
        MapPoint mapPoint = position.getPosition();
        LatLng latLng = GoogleDataConverter.fromMapPoint(mapPoint);
        float zoom = ZoomStandardization.fromStandardZoom(position.getZoom(), MapType.GOOGLE);
        float overlook = position.getOverlook();
        float rotate = position.getRotate();

        com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition(latLng, zoom, overlook, rotate);
        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void setInfoWindowInflater(final InfoWindowInflater inflater) {
        mInfoWindowInflater = inflater;
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
    public void screenShotAndSave(String saveFilePath, final MapScreenCaptureListener listener) {
        getMap().snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                listener.onScreenCaptured(bitmap);
            }
        });
    }

    @Override
    public MapPoint graphicPointToMapPoint(Point point) {
        LatLng latLng = getMap().getProjection().fromScreenLocation(point);
        return GoogleDataConverter.toMapPoint(latLng);
    }

    @Override
    public Point mapPointToGraphicPoint(MapPoint point) {
        LatLng latLng = GoogleDataConverter.fromMapPoint(point);
        return getMap().getProjection().toScreenLocation(latLng);
    }

    @Override
    public void setZoomControlsEnabled(boolean enable) {
        mZoomControlsEnabled = enable;
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(enable);
        }
    }

    @Override
    public void zoomOut() {
        getMap().moveCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public void zoomIn() {
        getMap().moveCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public float getZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getCameraPosition().zoom, MapType.GOOGLE);
    }

    @Override
    public void setZoom(float zoom) {
        float googleZoom = ZoomStandardization.fromStandardZoom(zoom, MapType.GOOGLE);
        com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition.Builder(getMap().getCameraPosition())
                .zoom(googleZoom)
                .build();

        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public float getMaxZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMaxZoomLevel(), MapType.GOOGLE);
    }

    @Override
    public float getMinZoom() {
        return ZoomStandardization.toStandardZoom(getMap().getMinZoomLevel(), MapType.GOOGLE);
    }

    @Override
    public void addMarker(MapMarker mapMarker) {
        doAddMarker(mapMarker);
        mMapMarkers.add(mapMarker);
    }

    private void doAddMarker(MapMarker mapMarker) {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(mapMarker.getIcon()))
                .position(GoogleDataConverter.fromMapPoint(mapMarker.getMapPoint()))
                .anchor(mapMarker.getAnchorX(), mapMarker.getAnchorY())
                .title(mapMarker.getTitle());

        Marker marker = getMap().addMarker(options);
        mapMarker.setRawMarker(MapType.GOOGLE, marker);
    }

    @Override
    public void removeMarker(MapMarker mapMarker) {
        removeRawMarker(mapMarker);
        mMapMarkers.remove(mapMarker);
    }

    @Override
    public void setMarkerVisible(MapMarker mapMarker, boolean visible) {
        Marker rawMarker = (Marker) mapMarker.getRawMarker(MapType.GOOGLE);
        rawMarker.setVisible(visible);
    }

    @Override
    public void addPolyline(Polyline polyline) {
        List<LatLng> latLngList = GoogleDataConverter.fromMapPoints(polyline.getPoints());
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

            polyline.addRawPolyline(MapType.GOOGLE, getMap().addPolyline(polylineOptions));

        }

        mPolylines.add(polyline);
    }

    private void applyBaseOptions(PolylineOptions polylineOptions, List<LatLng> points, PolylineTexture texture, int zIndex) {
        polylineOptions
                .addAll(points)
                .width(texture.getWidth())
                .zIndex(zIndex);
        if (texture.isDotted()) {
            polylineOptions.pattern(PATTERN_DOTTED);
        }
    }

    private void applyColorPolylineOptions(PolylineOptions polylineOptions, ColorTexture texture) {
        polylineOptions.color(texture.getColor());
    }

    private void applyBitmapPolylineOptions(PolylineOptions polylineOptions, BitmapTexture texture) {
        CustomCap startCap = new CustomCap(
                BitmapDescriptorFactory.fromBitmap(texture.getBitmap()),
                CUSTOM_CAP_IMAGE_REF_WIDTH_PX);

        CustomCap endCap = new CustomCap(
                BitmapDescriptorFactory.fromBitmap(texture.getBitmap()),
                CUSTOM_CAP_IMAGE_REF_WIDTH_PX);

        polylineOptions.startCap(startCap).endCap(endCap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removePolyline(Polyline polyline) {
        List<com.google.android.gms.maps.model.Polyline> rawPolylines = (List<com.google.android.gms.maps.model.Polyline>) polyline.getRawPolylines(MapType.GOOGLE);
        if (rawPolylines == null) {
            return;
        }

        Iterator<com.google.android.gms.maps.model.Polyline> iterator = rawPolylines.iterator();
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

    @Override
    public void showInfoWindow(MapMarker mapMarker) {
        if (mShowingInfoWindowMapMarker != null) {
            ((Marker) mShowingInfoWindowMapMarker.getRawMarker(MapType.GOOGLE)).setZIndex(0);
        }
        ((Marker) mapMarker.getRawMarker(MapType.GOOGLE)).setZIndex(Integer.MAX_VALUE);
        mapMarker.setInfoWindowVisible(true);
        setInfoWindow(mapMarker);
    }

    @Override
    public void hideInfoWindow() {
        if (mShowingInfoWindowMapMarker != null) {
            mShowingInfoWindowMapMarker.setInfoWindowVisible(false);
            Marker rawMarker = (Marker) mShowingInfoWindowMapMarker.getRawMarker(MapType.GOOGLE);
            if (rawMarker != null) {
                rawMarker.hideInfoWindow();
            }
            mShowingInfoWindowMapMarker = null;
        }
    }

    private void setInfoWindow(MapMarker mapMarker) {
        hideInfoWindow();
        mShowingInfoWindowMapMarker = mapMarker;
        Marker rawMarker = (Marker) mapMarker.getRawMarker(MapType.GOOGLE);
        rawMarker.showInfoWindow();
    }

    private MapMarker findMapMarker(Marker marker) {
        for (MapMarker mapMarker : mMapMarkers) {
            if (marker.equals(mapMarker.getRawMarker(MapType.GOOGLE))) {
                return mapMarker;
            }
        }
        return null;
    }

    private void removeRawMarker(MapMarker mapMarker) {
        Marker rawMarker = (Marker) mapMarker.getRawMarker(MapType.GOOGLE);
        if (rawMarker != null) {
            rawMarker.remove();
            mapMarker.setRawMarker(MapType.GOOGLE, null);
        }
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
        List<com.google.android.gms.maps.model.Polyline> rawPolylines = (List<com.google.android.gms.maps.model.Polyline>) polyline.getRawPolylines(MapType.GOOGLE);
        for (com.google.android.gms.maps.model.Polyline polyline1 : rawPolylines) {
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
        LatLng point = GoogleDataConverter.fromMapPoint(circle.getCenter());
        CircleOptions options = new CircleOptions()
                .zIndex(circle.getZIndex())
                .fillColor(circle.getFillColor())
                .strokeColor(circle.getStrokeColor())
                .strokeWidth(circle.getStrokeWidth())
                .radius(circle.getRadius())
                .center(point);

        com.google.android.gms.maps.model.Circle rawCircle = getMap().addCircle(options);
        circle.setRawGraph(MapType.GOOGLE, rawCircle);
    }

    private void addPolygon(Polygon polygon) {
        List<LatLng> latLngs = GoogleDataConverter.fromMapPoints(polygon.getPoints());
        PolygonOptions options = new PolygonOptions()
                .zIndex(polygon.getZIndex())
                .fillColor(polygon.getFillColor())
                .strokeWidth(polygon.getStrokeWidth())
                .strokeColor(polygon.getStrokeColor())
                .addAll(latLngs);

        com.google.android.gms.maps.model.Polygon rawPolygon = getMap().addPolygon(options);
        polygon.setRawGraph(MapType.GOOGLE, rawPolygon);
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
    public void removeGraph(Graph graph) {
        if (graph instanceof Circle) {
            removeCircle((Circle) graph);
        } else if (graph instanceof Polygon) {
            removePolygon((Polygon) graph);
        }

        mGraphs.remove(graph);
    }

    private void removeCircle(Circle circle) {
        com.google.android.gms.maps.model.Circle rawCircle = (com.google.android.gms.maps.model.Circle) circle.getRawGraph(MapType.GOOGLE);
        rawCircle.remove();
    }

    private void removePolygon(Polygon polygon) {
        com.google.android.gms.maps.model.Polygon rawPolygon = (com.google.android.gms.maps.model.Polygon) polygon.getRawGraph(MapType.GOOGLE);
        rawPolygon.remove();
    }

    @Override
    public void adjustCamera(List<MapPoint> mapPoints, int padding) {
        if (mapPoints == null) {
            return;
        }

        List<LatLng> latLngList = GoogleDataConverter.fromMapPoints(mapPoints);

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (LatLng latLng : latLngList) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();

        getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }
}
