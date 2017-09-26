package com.ray.lib_map.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author      : leixing
 * Date        : 2017-09-26
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    private BitmapUtil() {
        throw new UnsupportedOperationException();
    }

    public static Bitmap fromResource(Context context, int resId) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }

    public static Bitmap fromView(View view) {
        Bitmap bitmap;
        if (view == null) {
            Log.e(TAG, "view is null");
            return null;
        }

        view.clearFocus();
        view.setPressed(false);

        // save config
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        int color = view.getDrawingCacheBackgroundColor();
        float alpha = view.getAlpha();

        //reset config
        view.setWillNotCacheDrawing(false);
        view.setDrawingCacheBackgroundColor(0);
        view.setAlpha(1.0f);

        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e(TAG, "failed get view cache bitmap, view = " + view);
            return null;
        }

        bitmap = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();

        //restore config
        view.setAlpha(alpha);
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Bitmap fromPath(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap fromFile(Context context, String path) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(path);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (Exception e) {
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        return bitmap;
    }

}
