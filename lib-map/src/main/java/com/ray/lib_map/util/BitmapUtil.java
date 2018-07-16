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
 * @author      : leixing
 * Date        : 2017-09-26
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings({"WeakerAccess", "unused"})
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
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
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
        Bitmap bitmap;
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
