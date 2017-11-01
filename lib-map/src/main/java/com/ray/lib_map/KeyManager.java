package com.ray.lib_map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


/**
 * KeyManager
 */
public class KeyManager {

    private static String BAIDU_M_CODE;
    private static String BAIDU_API_KEY;
    private static String SEARCH_API_KEY;
    private static String GEO_API_KEY;

    private static String getApplicationMeta(Context context, String key) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGoogleSearchApiKey(Context context) {
        if (TextUtils.isEmpty(SEARCH_API_KEY)) {
            SEARCH_API_KEY = getApplicationMeta(context, "google_search_api_key");
        }
        return SEARCH_API_KEY;
    }

    public static String getGoogleGeoApiKey(Context context) {
        if (TextUtils.isEmpty(GEO_API_KEY)) {
            GEO_API_KEY = getApplicationMeta(context, "google_geo_api_key");
        }
        return GEO_API_KEY;
    }

    public static String getBaiduApiKey(Context context) {
        if (TextUtils.isEmpty(BAIDU_API_KEY)) {
            BAIDU_API_KEY = getApplicationMeta(context, "baidu_api_key");
        }
        return BAIDU_API_KEY;
    }

    public static String getBaiduMCode(Context context) {
        if (TextUtils.isEmpty(BAIDU_M_CODE)) {
            BAIDU_M_CODE = getApplicationMeta(context, "baidu_m_code");
        }
        return BAIDU_M_CODE;
    }
}
