package com.ray.lib_map.entity.polyline;

import android.graphics.Bitmap;

/**
 * Author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class BitmapTexture extends PolyLineTexture {
    private Bitmap bitmap;

    public BitmapTexture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public BitmapTexture(PolyLineTexture texture, Bitmap bitmap) {
        super(texture);
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public PolyLineTexture copy() {
        return new BitmapTexture(this, bitmap);
    }
}
