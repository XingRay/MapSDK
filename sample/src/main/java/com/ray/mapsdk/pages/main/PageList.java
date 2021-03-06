package com.ray.mapsdk.pages.main;

import com.ray.mapsdk.pages.camera.CameraActivity;
import com.ray.mapsdk.pages.gesture.GestureControlActivity;
import com.ray.mapsdk.pages.listener.ListenerActivity;
import com.ray.mapsdk.pages.poi.PoiListActivity;
import com.ray.mapsdk.pages.polyline.PolylineActivity;

import java.util.Arrays;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

class PageList {
    static List<Page> PAGES = Arrays.asList(
            new Page("poi query", PoiListActivity.class),
            new Page("camera", CameraActivity.class),
            new Page("gesture control", GestureControlActivity.class),
            new Page("listener", ListenerActivity.class),
            new Page("polyline", PolylineActivity.class)
    );
}
