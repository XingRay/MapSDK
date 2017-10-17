package com.ray.mapsdk.pages.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ray.lib_map.MapView;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.listener.CameraMoveListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.mapsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class ListenerActivity extends Activity {
    @BindView(R.id.mv_map)
    MapView mvMap;
    @BindView(R.id.et_zoom)
    EditText etZoom;
    @BindView(R.id.et_rotate)
    EditText etRotate;
    @BindView(R.id.et_overlook)
    EditText etOverlook;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView();
        loadData();
    }

    private void initVariables() {
        mActivity = this;
    }

    private void initView() {
        setContentView(R.layout.activity_listener);
        ButterKnife.bind(this);

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);

        setListeners();
    }

    private void setListeners() {
        mvMap.setCameraMoveListener(new CameraMoveListener() {
            @Override
            public void onCameraMoving(CameraPosition position) {
                showCameraPosition(position);
            }

            @Override
            public void onCameraMoved(CameraPosition position) {
                showCameraPosition(position);
                Toast.makeText(mActivity, "camera moved", Toast.LENGTH_SHORT).show();
            }
        });

        mvMap.setMapLoadListener(new MapLoadListener() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(mActivity, "mapLoaded", Toast.LENGTH_SHORT).show();
                getAll();
            }
        });
    }

    private void loadData() {

    }

    @OnClick({R.id.bt_gaode_map,
            R.id.bt_baidu_map,
            R.id.bt_google_map,
            R.id.bt_set_zoom,
            R.id.bt_animate_zoom,
            R.id.bt_set_rotate,
            R.id.bt_animate_rotate,
            R.id.bt_set_overlook,
            R.id.bt_animate_overlook,
            R.id.bt_set_latitude,
            R.id.bt_animate_latitude,
            R.id.bt_set_longitude,
            R.id.bt_animate_longitude,
            R.id.bt_set_all,
            R.id.bt_get_all,
            R.id.bt_animate_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_gaode_map:
                mvMap.switchMapType(MapType.GAODE);
                break;
            case R.id.bt_baidu_map:
                mvMap.switchMapType(MapType.BAIDU);
                break;
            case R.id.bt_google_map:
                mvMap.switchMapType(MapType.GOOGLE);
                break;
            case R.id.bt_set_zoom:
                setZoom();
                break;
            case R.id.bt_animate_zoom:
                animateZoom();
                break;
            case R.id.bt_set_rotate:
                setRotate();
                break;
            case R.id.bt_animate_rotate:
                animateRotate();
                break;
            case R.id.bt_set_overlook:
                setOverlook();
                break;
            case R.id.bt_animate_overlook:
                animateOverlook();
                break;
            case R.id.bt_set_latitude:
                setPosition();
                break;
            case R.id.bt_animate_latitude:
                animatePosition();
                break;
            case R.id.bt_set_longitude:
                setPosition();
                break;
            case R.id.bt_animate_longitude:
                animatePosition();
                break;
            case R.id.bt_set_all:
                setAll();
                break;
            case R.id.bt_get_all:
                getAll();
                break;
            case R.id.bt_animate_all:
                animateAll();
                break;
        }
    }

    private void animateOverlook() {
        etOverlook.setText(String.valueOf(mvMap.getOverlook()));
    }

    private void setOverlook() {
        mvMap.setOverlook(Float.parseFloat(etOverlook.getText().toString().trim()));
    }

    private void animateRotate() {
        etRotate.setText(String.valueOf(mvMap.getRotate()));
    }

    private void setRotate() {
        mvMap.setRotate(Float.parseFloat(etRotate.getText().toString().trim()));
    }

    private void animateZoom() {
        etZoom.setText(String.valueOf(mvMap.getZoom()));
    }

    private void setZoom() {
        mvMap.setZoom(Float.parseFloat(etZoom.getText().toString().trim()));
    }

    private void setPosition() {
        double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(etLongitude.getText().toString().trim());

        mvMap.setPosition(new MapPoint(latitude, longitude, MapType.GAODE.getCoordinateType()));
    }

    private void animatePosition() {
        MapPoint position = mvMap.getPosition();
        etLatitude.setText(String.valueOf(position.getLatitude()));
        etLongitude.setText(String.valueOf(position.getLongitude()));
    }

    private void getAll() {
        CameraPosition cameraPosition = mvMap.getCameraPosition();
        showCameraPosition(cameraPosition);
    }

    private void setAll() {
        double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(etLongitude.getText().toString().trim());
        MapPoint mapPoint = new MapPoint(latitude, longitude, MapType.GAODE.getCoordinateType());
        float zoom = Float.parseFloat(etZoom.getText().toString().trim());
        float overlook = Float.parseFloat(etOverlook.getText().toString().trim());
        float rotate = Float.parseFloat(etRotate.getText().toString().trim());

        CameraPosition cameraPosition = new CameraPosition();
        cameraPosition.setPosition(mapPoint);
        cameraPosition.setZoom(zoom);
        cameraPosition.setOverlook(overlook);
        cameraPosition.setRotate(rotate);
        mvMap.setCameraPosition(cameraPosition);
    }

    private void animateAll() {

    }

    private void showCameraPosition(CameraPosition cameraPosition) {
        MapPoint position = cameraPosition.getPosition();
        etLongitude.setText(String.valueOf(position.getLongitude()));
        etLatitude.setText(String.valueOf(position.getLatitude()));
        etZoom.setText(String.valueOf(cameraPosition.getZoom()));
        etRotate.setText(String.valueOf(cameraPosition.getRotate()));
        etOverlook.setText(String.valueOf(cameraPosition.getOverlook()));
    }
}
