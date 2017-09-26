package com.ray.mapsdk.pages.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ray.lib_map.MapView;
import com.ray.lib_map.MapViewInterface;
import com.ray.lib_map.data.DataCallback;
import com.ray.lib_map.data.MapDataSource;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.entity.PoiSearchSuggestion;
import com.ray.lib_map.extern.MapDataRepository;
import com.ray.lib_map.extern.MapHelper;
import com.ray.lib_map.extern.MapType;
import com.ray.mapsdk.R;
import com.ray.mapsdk.base.OnItemClickListener;
import com.ray.mapsdk.helper.ThreadPools;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class MapActivity extends AppCompatActivity {
    private static final String TAG = "MapActivity";

    @BindView(R.id.mv_map)
    MapView mvMap;

    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private Context mContext;
    private MapDataRepository mMapDataRepository;
    private PoiAdapter mAdapter;
    private Activity mActivity;

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
                    mvMap.animateTo(poi.getPoint(), null);
                }
            }
        });

        mvMap.createMap(MapType.GAODE);
        mvMap.onCreate(null);
        mvMap.setMapLoadListener(new MapViewInterface.MapLoadListener() {
            @Override
            public void onMapLoaded() {
                location();
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
                                mvMap.addMarker(MapHelper.buildLocationMarker(address, mContext, R.mipmap.icon_location));
                                mvMap.animateTo(mapPoint, null);
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
