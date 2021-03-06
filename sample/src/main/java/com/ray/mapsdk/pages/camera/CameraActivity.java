package com.ray.mapsdk.pages.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.leixing.lib_map_amap.GaodeMap;
import com.ray.lib_map.MapView;
import com.ray.lib_map.entity.CameraPosition;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapConfig;
import com.ray.lib_map.manager.MapConfigManager;
import com.ray.mapsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : leixing
 * Date        : 2017-09-30
 * Email       : leixing@qq.com
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
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

//        mvMap.initMap(GaodeMap.CONFIG_DEFAULT);
//        mvMap.onCreate(null);
    }

    private void loadData() {

    }

    @OnClick({R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map, R.id.bt_set_zoom, R.id.bt_get_zoom, R.id.bt_set_rotate, R.id.bt_get_rotate, R.id.bt_set_overlook, R.id.bt_get_overlook, R.id.bt_set_latitude, R.id.bt_get_latitude, R.id.bt_set_longitude, R.id.bt_get_longitude, R.id.bt_set_all, R.id.bt_get_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_gaode_map:
//                mvMap.switchMapType(GaodeMap.CONFIG_DEFAULT);
                break;
            case R.id.bt_baidu_map:
//                mvMap.switchMapType(MapConfig.BAIDU);
                break;
            case R.id.bt_google_map:
//                mvMap.switchMapType(MapConfig.GOOGLE);
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
            default:
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

    private void setPosition() {
        double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(etLongitude.getText().toString().trim());

//        String coordinateType = MapConfigManager.getInstance().getMapConfig(GaodeMap.CONFIG_DEFAULT).getCoordinateType();
//        mvMap.setPosition(new MapPoint(latitude, longitude, coordinateType));
    }

    private void getPosition() {
        MapPoint position = mvMap.getPosition();
        etLatitude.setText(String.valueOf(position.getLatitude()));
        etLongitude.setText(String.valueOf(position.getLongitude()));
    }

    private void getAll() {
        CameraPosition cameraPosition = mvMap.getCameraPosition();
        MapPoint position = cameraPosition.getPosition();
        etLongitude.setText(String.valueOf(position.getLongitude()));
        etLatitude.setText(String.valueOf(position.getLatitude()));
        etZoom.setText(String.valueOf(cameraPosition.getZoom()));
        etRotate.setText(String.valueOf(cameraPosition.getRotate()));
        etOverlook.setText(String.valueOf(cameraPosition.getOverlook()));
    }

    private void setAll() {
//        double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
//        double longitude = Double.parseDouble(etLongitude.getText().toString().trim());
//        String coordinateType = MapConfigManager.getInstance().getMapConfig(GaodeMap.CONFIG_DEFAULT).getCoordinateType();
//        MapPoint mapPoint = new MapPoint(latitude, longitude, coordinateType);
//        float zoom = Float.parseFloat(etZoom.getText().toString().trim());
//        float overlook = Float.parseFloat(etOverlook.getText().toString().trim());
//        float rotate = Float.parseFloat(etRotate.getText().toString().trim());
//
//        CameraPosition cameraPosition = new CameraPosition();
//        cameraPosition.setPosition(mapPoint);
//        cameraPosition.setZoom(zoom);
//        cameraPosition.setOverlook(overlook);
//        cameraPosition.setRotate(rotate);
//        mvMap.setCameraPosition(cameraPosition);
    }
}
