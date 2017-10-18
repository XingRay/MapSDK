package com.ray.lib_map.entity.polyline;

/**
 * Author      : leixing
 * Date        : 2017-10-17
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class ColorTexture extends PolylineTexture {
    private int color;

    public ColorTexture(int color) {
        this.color = color;
    }

    public ColorTexture(PolylineTexture texture, int color) {
        super(texture);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void color(int color) {
        this.color = color;
    }

    @Override
    public PolylineTexture copy() {
        return new ColorTexture(this, color);
    }
}
