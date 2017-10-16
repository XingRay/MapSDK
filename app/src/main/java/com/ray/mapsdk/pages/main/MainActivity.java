package com.ray.mapsdk.pages.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ray.mapsdk.R;
import com.ray.mapsdk.base.OnItemClickListener;
import com.ray.mapsdk.pages.gesture.GestureControlActivity;
import com.ray.mapsdk.pages.poi.PoiListActivity;
import com.ray.mapsdk.pages.zoom.CameraActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private Context mContext;
    private PageListAdapter mAdapter;

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private List<Page> mPages = Arrays.asList(
            new Page("poi query", PoiListActivity.class),
            new Page("camera", CameraActivity.class),
            new Page("gesture control", GestureControlActivity.class)
    );
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        loadData();
    }

    private void initVariables() {
        mContext = getApplicationContext();
        mAdapter = new PageListAdapter(this);
        mActivity = this;
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPages.get(position).start(mActivity);
            }
        });
    }

    private void loadData() {
        mAdapter.set(mPages);
    }
}
