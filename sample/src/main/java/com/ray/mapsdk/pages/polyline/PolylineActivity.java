package com.ray.mapsdk.pages.polyline;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.ray.lib_map.MapView;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.polyline.Polyline;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.listener.MapClickListener;
import com.ray.mapsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PolylineActivity extends Activity {
    @BindView(R.id.mv_map)
    MapView mvMap;
    @BindView(R.id.et_color)
    EditText etColor;
    @BindView(R.id.et_width)
    EditText etWidth;
    @BindView(R.id.et_style)
    EditText etStyle;
    private boolean mIsDrawingLine;
    private Polyline mPolyline;
    private int mColor;
    private int mWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView();
        loadData();
    }

    private void initVariables() {
        mIsDrawingLine = false;
        mColor = 0xffff0000;
        mWidth = 10;
    }

    private void initView() {
        setContentView(R.layout.activity_polyline);
        ButterKnife.bind(this);

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);

        mvMap.setMapClickListener(new MapClickListener() {
            @Override
            public void onMapClick(MapPoint mapPoint) {
                if (!mIsDrawingLine) {
                    return;
                }
                mPolyline.addPoint(mapPoint);
                if (mPolyline.getPoints().size() >= 2) {
                    mvMap.removePolyline(mPolyline);
                    mvMap.addPolyline(mPolyline);
                }

            }
        });
    }

    private void loadData() {

    }

    @OnClick({R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map, R.id.bt_color, R.id.bt_width, R.id.bt_style, R.id.bt_start, R.id.bt_end, R.id.bt_remove, R.id.bt_reset})
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
            case R.id.bt_color:
                setColor();
                break;
            case R.id.bt_width:
                setWidth();
                break;
            case R.id.bt_style:
                setStyle();
                break;
            case R.id.bt_start:
                startDrawLine();
                break;
            case R.id.bt_end:
                endDrawLine();
                break;
            case R.id.bt_remove:
                removeLastLine();
                break;
            case R.id.bt_reset:
                clearAllLines();
                break;
        }
    }

    private void setColor() {
        mColor = 0xff6495ED;
        mPolyline.color(mColor);
    }

    private void setWidth() {
        mWidth = Integer.parseInt(etWidth.getText().toString().trim());
        mPolyline.width(mWidth);
    }

    private void setStyle() {

    }

    private void startDrawLine() {
        mIsDrawingLine = true;
        mPolyline = new Polyline().color(mColor).width(mWidth).dotted(true);
    }

    private void endDrawLine() {
        mIsDrawingLine = false;
        mvMap.removePolyline(mPolyline);
    }

    private void removeLastLine() {

    }

    private void clearAllLines() {

    }
}
