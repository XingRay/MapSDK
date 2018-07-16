package com.ray.mapsdk.pages.poi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ray.lib_map.InfoWindowInflater;
import com.ray.lib_map.MapView;
import com.ray.lib_map.data.DataCallback;
import com.ray.lib_map.data.MapDataRepository;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapMarker;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.CoordinateType;
import com.ray.lib_map.extern.MapType;
import com.ray.lib_map.listener.InfoWindowClickListener;
import com.ray.lib_map.listener.MapLoadListener;
import com.ray.lib_map.listener.MarkerClickListener;
import com.ray.lib_map.util.MapHelper;
import com.ray.mapsdk.R;
import com.ray.mapsdk.extern.OnItemClickListener;
import com.ray.mapsdk.extern.ThreadPools;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PoiListActivity extends AppCompatActivity {
    private static final String TAG = "poi_activity";

    @BindView(R.id.mv_map)
    MapView mvMap;

    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private Context mContext;
    private MapDataRepository mMapDataRepository;
    private PoiAdapter mAdapter;
    private Activity mActivity;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        loadData();
    }

    private void initVariables() {
        mContext = getApplicationContext();
        mActivity = this;
        mMapDataRepository = new MapDataRepository(mContext);
        mAdapter = new PoiAdapter(this);
        mInflater = LayoutInflater.from(this);
    }

    private void initView() {
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mAdapter.select(position);
                Poi poi = mAdapter.getSelectedPoi();
                if (poi != null) {
                    mvMap.moveCameraTo(poi.getMapPoint());
                }
            }
        });

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);
        mvMap.setMapLoadListener(new MapLoadListener() {
            @Override
            public void onMapLoaded() {
                location();
            }
        });

        mvMap.setMarkerClickListener(new MarkerClickListener() {
            @Override
            public boolean onMarkClick(MapMarker marker) {
                MapPoint mapPoint = marker.getMapPoint();
                String toast = "(" + mapPoint.getLatitude() + ", " + mapPoint.getLongitude() + ")";
                Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
                mvMap.showInfoWindow(marker);
                return true;
            }
        });

        mvMap.setInfoWindowInflater(new InfoWindowInflater() {
            @Override
            public View inflate(MapMarker marker) {
                @SuppressLint("InflateParams")
                View view = mInflater.inflate(R.layout.view_info_window, null);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                TextView tvContent = view.findViewById(R.id.tv_content);
                TextView tvCoordinate = view.findViewById(R.id.tv_coordinate);

                tvTitle.setText(marker.getTitle());
                tvContent.setText(marker.getContent());

                MapPoint mapPoint = marker.getMapPoint().as(CoordinateType.WGS84);
                tvCoordinate.setText(String.format("(%1$s, %2$s)", String.valueOf(mapPoint.getLatitude()), String.valueOf(mapPoint.getLongitude())));

                return view;
            }
        });

        mvMap.setInfoWindowClickListener(new InfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(MapMarker marker) {
                MapPoint mapPoint = marker.getMapPoint().as(CoordinateType.WGS84);
                String toast = "(" + mapPoint.getLatitude() + ", " + mapPoint.getLongitude() + ")";
                Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        AndPermission
                .with(mActivity)
                .requestCode(200)
                .permission(Permission.LOCATION)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(mActivity, rationale).show();
                    }
                })
                .start();
    }

    @OnClick({R.id.bt_gaode_map, R.id.bt_baidu_map, R.id.bt_google_map})
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
        }
    }

    private void location() {
        Log.i(TAG, "location: location start");
        ThreadPools.getDefault().submit(new Runnable() {
            @Override
            public void run() {
                mMapDataRepository.locate(new DataCallback<Address>() {
                    @Override
                    public void onSuccess(final Address address) {
                        Log.i(TAG, "onSuccess: address = " + address);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MapPoint mapPoint = address.getMapPoint();
                                mvMap.addMarker(MapHelper.createMarker(address, mContext, R.mipmap.icon_location));
                                mvMap.moveCameraTo(mapPoint);
                                queryPoi(mapPoint);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errorCode, String desc) {
                        Log.i(TAG, "onFailure: location failed, errorCode : " + errorCode + ", desc : " + desc);
                    }
                });
            }
        });
    }

    private void queryPoi(final MapPoint mapPoint) {
        Log.i(TAG, "queryPoi: start query pois");
        ThreadPools.getDefault().submit(new Runnable() {
            @Override
            public void run() {
                mMapDataRepository.queryPoi(mapPoint, 200, 1, 20, new MapDataSource.POISearchCallback() {
                    @Override
                    public void onSuggestion(List<PoiSearchSuggestion> suggestions) {
                        Log.i(TAG, "onSuggestion: " + suggestions);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mActivity, "suggestions", Toast.LENGTH_SHORT).show();
                                mAdapter.clear();
                            }
                        });

                    }

                    @Override
                    public void onNoSearchResult() {
                        Log.i(TAG, "onNoSearchResult: ");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mActivity, "no result", Toast.LENGTH_SHORT).show();
                                mAdapter.clear();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<Poi> pois) {
                        Log.i(TAG, "onSuccess: " + pois);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.set(PoiWrapper.fromPois(pois));
                                List<MapMarker> markers = MapHelper.createMarkers(pois, mContext, R.mipmap.icon_location);
                                mvMap.clearMarkers();
                                mvMap.addMarkers(markers);
                            }
                        });
                    }

                    @Override
                    public void onFailure(final int errorCode, final String desc) {
                        Log.i(TAG, "onFailure: errorCode " + errorCode + ", desc " + desc);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mActivity, "failure, errorCode " + errorCode + ", desc " + desc, Toast.LENGTH_SHORT).show();
                                mAdapter.clear();
                            }
                        });
                    }
                });
            }
        });
    }
}
