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
public abstract class PolylineTexture implements CopyableTexture {
    /**
     * start valid index
     */
    private int index;

    private int width;

    // TODO: 2017-10-19 暂不使用
    private IndexMatcher matcher;

    // TODO: 2017-10-19 替换成style枚举
    private boolean dotted;

    public PolylineTexture() {
        width = 5;
    }

    public PolylineTexture(PolylineTexture texture) {
        index = texture.getIndex();
        width = texture.getWidth();
        matcher = texture.getMatcher();
        dotted = texture.isDotted();
    }

    public PolylineTexture index(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        this.index = index;
        return this;
    }

    public PolylineTexture width(int width) {
        this.width = width;
        return this;
    }

    public PolylineTexture matcher(IndexMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public PolylineTexture dotted(boolean dotted) {
        this.dotted = dotted;
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

    public boolean isDotted() {
        return dotted;
    }
}
