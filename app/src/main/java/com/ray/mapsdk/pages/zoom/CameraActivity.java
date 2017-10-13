package com.ray.mapsdk.pages.zoom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ray.lib_map.MapView;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;
import com.ray.mapsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author      : leixing
 * Date        : 2017-09-30
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class CameraActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView();
        loadData();
    }

    private void initVariables() {

    }

    private void initView() {
        setContentView(R.layout.activity_map_zoom);
        ButterKnife.bind(this);

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);
    }

    private void loadData() {

    }

    @OnClick({R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map, R.id.bt_set_zoom, R.id.bt_get_zoom, R.id.bt_set_rotate, R.id.bt_get_rotate, R.id.bt_set_overlook, R.id.bt_get_overlook, R.id.bt_set_latitude, R.id.bt_get_latitude, R.id.bt_set_longitude, R.id.bt_get_longitude, R.id.bt_set_all, R.id.bt_get_all})
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
            case R.id.bt_get_zoom:
                getZoom();
                break;
            case R.id.bt_set_rotate:
                setRotate();
                break;
            case R.id.bt_get_rotate:
                getRotate();
                break;
            case R.id.bt_set_overlook:
                setOverlook();
                break;
            case R.id.bt_get_overlook:
                getOverlook();
                break;
            case R.id.bt_set_latitude:
                setPosition();
                break;
            case R.id.bt_get_latitude:
                getPosition();
                break;
            case R.id.bt_set_longitude:
                setPosition();
                break;
            case R.id.bt_get_longitude:
                getPosition();
                break;
            case R.id.bt_set_all:
                setAll();
                break;
            case R.id.bt_get_all:
                getAll();
                break;
        }
    }

    private void getOverlook() {
        etOverlook.setText(String.valueOf(mvMap.getOverlook()));
    }

    private void setOverlook() {
        mvMap.setOverlook(Float.parseFloat(etOverlook.getText().toString().trim()));
    }

    private void getRotate() {
        etRotate.setText(String.valueOf(mvMap.getRotate()));
    }

    private void setRotate() {
        mvMap.setRotate(Float.parseFloat(etRotate.getText().toString().trim()));
    }

    private void getZoom() {
        etZoom.setText(String.valueOf(mvMap.getZoom()));
    }

    private void setZoom() {
        mvMap.setZoom(Float.parseFloat(etZoom.getText().toString().trim()));
    }

    private void getAll() {
        getPosition();
        getZoom();
        getRotate();
        getOverlook();
    }

    private void setAll() {
        setPosition();
        setZoom();
        setOverlook();
        setRotate();
    }

    private void setPosition() {
        double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(etLongitude.getText().toString().trim());

        mvMap.setPosition(new MapPoint(latitude, longitude, MapType.GAODE.getCoordinateType()));
    }

    private void getPosition() {
        MapPoint position = mvMap.getPosition();
        etLatitude.setText(String.valueOf(position.getLatitude()));
        etLongitude.setText(String.valueOf(position.getLongitude()));
    }
}
