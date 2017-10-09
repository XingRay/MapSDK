package com.ray.mapsdk.pages.zoom;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ray.lib_map.MapView;
import com.ray.lib_map.MapViewInterface;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.util.MapUtil;
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

public class MapZoomActivity extends AppCompatActivity {
    @BindView(R.id.mv_map)
    MapView mvMap;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_zoom)
    TextView tvZoom;
    @BindView(R.id.et_zoom)
    EditText etZoom;
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
        setContentView(R.layout.activity_map_zoom);
        ButterKnife.bind(this);

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);

        mvMap.setAnimationListener(new MapViewInterface.AnimationListener() {
            @Override
            public void onFinished() {
                Toast.makeText(mActivity, "onFinished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(mActivity, "onCanceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {

    }

    @OnClick({R.id.bt_refresh, R.id.bt_set, R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_refresh:
                tvZoom.setText(String.valueOf(mvMap.getZoom()));
                tvDistance.setText(calcDistance() + "米");
                break;
            case R.id.bt_set:
                mvMap.zoomTo(Float.parseFloat(etZoom.getText().toString().trim()));
                break;
            case R.id.bt_gaode_map:
                mvMap.switchMapType(MapType.GAODE);
                break;
            case R.id.bt_baidu_map:
                mvMap.switchMapType(MapType.BAIDU);
                break;
            case R.id.bt_google_map:
                mvMap.switchMapType(MapType.GOOGLE);
                break;
        }
    }

    private double calcDistance() {
        MapPoint p1 = mvMap.fromScreenLocation(new Point(0, 0));
        MapPoint p2 = mvMap.fromScreenLocation(new Point(100, 0));
        return MapUtil.getDistance(p1, p2);
    }
}
