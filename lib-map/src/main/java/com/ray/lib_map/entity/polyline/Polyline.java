package com.ray.lib_map.entity.polyline;

import android.graphics.Bitmap;

import com.ray.lib_map.entity.MapPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : leixing
 * Date        : 2017-07-14
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : polyline
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Polyline {
    private static final ColorTexture DEFAULT_TEXTURE = new ColorTexture(0xff000000);

    private List<MapPoint> points;
    private List<PolylineTexture> textures;
    private int zIndex;

    private Map<String, List<Object>> rawPolylineMap;

    public Polyline() {
        points = new ArrayList<>(2);
        textures = new ArrayList<>(1);
    }

    public void addRawPolyline(String mapType, Object rawPolyline) {
        if (rawPolylineMap == null) {
            rawPolylineMap = new HashMap<>();
        }
        addRawPloyLine(mapType, rawPolyline);
    }

    @SuppressWarnings("unchecked")
    private void addRawPloyLine(String mapType, Object rawPolylines) {
        List<Object> polylines = rawPolylineMap.get(mapType);
        if (polylines == null) {
            polylines = new ArrayList<>();
            rawPolylineMap.put(mapType, polylines);
        }
        polylines.add(rawPolylines);
    }


    public List<?> getRawPolylines(String mapType) {
        if (rawPolylineMap == null) {
            return null;
        }
        return rawPolylineMap.get(mapType);
    }


    public Polyline points(List<MapPoint> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.points.clear();
        this.points.addAll(points);
        return this;
    }

    public Polyline points(MapPoint[] points) {
        return points(Arrays.asList(points));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Polyline addPoint(MapPoint point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        this.points.add(point);
        return this;
    }

    public Polyline addPoints(List<MapPoint> points) {
        if (points != null) {
            this.points.addAll(points);
        }
        return this;
    }

    public Polyline addPoints(MapPoint[] points) {
        return addPoints(Arrays.asList(points));
    }

    /**
     * 清除掉现有的所有的纹理，设置一个新的纹理
     *
     * @param texture 新的纹理
     * @return polyline
     */
    public Polyline texture(PolylineTexture texture) {
        if (texture == null) {
            throw new IllegalArgumentException();
        }
        this.textures.clear();
        this.textures.add(texture);
        return this;
    }

    /**
     * 清除掉现有的所有的纹理，设置一个新的纹理组
     *
     * @param textures 新的纹理组
     * @return polyline
     */
    public Polyline textures(List<PolylineTexture> textures) {
        if (textures == null || textures.size() < 1) {
            throw new IllegalArgumentException();
        }
        this.textures.clear();
        this.textures.addAll(textures);
        return this;
    }

    public Polyline textures(PolylineTexture[] textures) {
        return textures(Arrays.asList(textures));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Polyline addTexture(PolylineTexture texture) {
        if (texture == null) {
            throw new IllegalArgumentException();
        }
        this.textures.add(texture);
        return this;
    }

    public Polyline addTextures(List<PolylineTexture> textures) {
        if (textures != null) {
            this.textures.addAll(textures);
        }
        return this;
    }

    public Polyline addTextures(PolylineTexture[] textures) {
        return addTextures(Arrays.asList(textures));
    }

    public List<MapPoint> getPoints() {
        return this.points;
    }

    public List<PolylineTexture> getTextures() {
        return this.textures;
    }

    public Polyline color(int color) {
        if (this.textures.isEmpty()) {
            addTexture(new ColorTexture(color));
            return this;
        }

        PolylineHelper.sortByIndex(this.textures);
        PolylineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolylineHelper.removeByIndex(this.textures, index);

        addTexture(new ColorTexture(lastTexture, color).index(index));
        return this;
    }

    public Polyline bitmap(Bitmap bitmap) {
        if (this.textures.isEmpty()) {
            addTexture(new BitmapTexture(bitmap));
            return this;
        }

        PolylineHelper.sortByIndex(this.textures);
        PolylineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolylineHelper.removeByIndex(this.textures, index);

        addTexture(new BitmapTexture(lastTexture, bitmap).index(index));
        return this;
    }

    public Polyline width(int width) {
        if (this.textures.isEmpty()) {
            addTexture(DEFAULT_TEXTURE.width(width));
            return this;
        }

        PolylineHelper.sortByIndex(this.textures);
        PolylineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolylineHelper.removeByIndex(this.textures, index);

        addTexture(lastTexture.copy().width(width).index(index));
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    public Polyline dotted(boolean dotted) {
        if (this.textures.isEmpty()) {
            addTexture(DEFAULT_TEXTURE.dotted(dotted));
            return this;
        }

        PolylineHelper.sortByIndex(this.textures);
        PolylineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolylineHelper.removeByIndex(this.textures, index);

        addTexture(lastTexture.copy().dotted(dotted).index(index));
        return this;
    }

    public void clearRawPolyline() {
        rawPolylineMap.clear();
    }

    public Polyline zIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public int getZIndex() {
        return this.zIndex;
    }
}
