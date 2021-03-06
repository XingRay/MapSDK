package com.ray.mapsdk.pages.gesture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ray.lib_map.MapView;
import com.ray.lib_map.extern.MapConfig;
import com.ray.mapsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : leixing
 * Date        : 2017-10-16
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class GestureControlActivity extends AppCompatActivity {
    @BindView(R.id.mv_map)
    MapView mvMap;
    @BindView(R.id.sw_drag)
    Switch swDrag;
    @BindView(R.id.sw_zoom)
    Switch swZoom;
    @BindView(R.id.sw_rotate)
    Switch swRotate;
    @BindView(R.id.sw_overlook)
    Switch swOverlook;

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
        setContentView(R.layout.activity_gesture_control);
        ButterKnife.bind(this);

//        mvMap.initMap(MapConfig.GAODE);
        mvMap.onCreate(null);

        swDrag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mvMap.setScrollGestureEnable(isChecked);
            }
        });

        swZoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mvMap.setZoomGestureEnable(isChecked);
            }
        });

        swRotate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mvMap.setRotateGestureEnable(isChecked);
            }
        });

        swOverlook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mvMap.setOverlookGestureEnable(isChecked);
            }
        });
    }

    private void loadData() {
        boolean scrollGestureEnable = mvMap.isScrollGestureEnable();
        boolean zoomGestureEnable = mvMap.isZoomGestureEnable();
        boolean rotateGestureEnable = mvMap.isRotateGestureEnable();
        boolean overlookGestureEnable = mvMap.isOverlookGestureEnable();

        swDrag.setChecked(scrollGestureEnable);
        swZoom.setChecked(zoomGestureEnable);
        swRotate.setChecked(rotateGestureEnable);
        swOverlook.setChecked(overlookGestureEnable);
    }

    @OnClick({R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_gaode_map:
//                mvMap.switchMapType(MapConfig.GAODE);
                break;
            case R.id.bt_baidu_map:
//                mvMap.switchMapType(MapConfig.BAIDU);
                break;
            case R.id.bt_google_map:
//                mvMap.switchMapType(MapConfig.GOOGLE);
                break;
            default:
        }
    }
}
