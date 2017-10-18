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
public abstract class PolyLineTexture implements CopyableTexture {
    /**
     * start valid index
     */
    private int index;

    private int width;

    private IndexMatcher matcher;

    public PolyLineTexture() {
        width = 5;
    }

    public PolyLineTexture(PolyLineTexture texture) {
        index = texture.getIndex();
        width = texture.getWidth();
        matcher = texture.getMatcher();
    }

    public PolyLineTexture index(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        this.index = index;
        return this;
    }

    public PolyLineTexture width(int width) {
        this.width = width;
        return this;
    }

    public PolyLineTexture matcher(IndexMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public IndexMatcher getMatcher() {
        return matcher;
    }
}
