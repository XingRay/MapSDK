package com.ray.mapsdk.pages.polyline;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ray.mapsdk.R;

import butterknife.ButterKnife;

/**
 * Author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PolyLineActivity extends Activity {
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
        setContentView(R.layout.activity_polyline);
        ButterKnife.bind(this);
    }

    private void loadData() {

    }
}
