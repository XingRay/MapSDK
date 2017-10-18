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
public class ColorTexture extends PolyLineTexture {
    private int color;

    public ColorTexture(int color) {
        this.color = color;
    }

    public ColorTexture(PolyLineTexture texture, int color) {
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
    public PolyLineTexture copy() {
        return new ColorTexture(this, color);
    }
}