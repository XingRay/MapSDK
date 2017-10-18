package com.ray.lib_map.entity.polyline;

import android.graphics.Bitmap;

import com.amap.api.maps.model.Polyline;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.extern.MapType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author      : leixing
 * Date        : 2017-07-14
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : polyline
 */

@SuppressWarnings("WeakerAccess")
public class PolyLine {
    private static final ColorTexture DEFAULT_TEXTURE = new ColorTexture(0xff000000);

    private List<MapPoint> points;
    private List<PolyLineTexture> textures;
    private Map<MapType, List<?>> rawPolyLineMap;

    /**
     * current texture
     */
    private PolyLineTexture mCurrentTexture;

    public PolyLine() {
        points = new ArrayList<>(2);
        textures = new ArrayList<>(1);
    }

    public void addRawPolyLine(MapType mapType, Object rawPolyLine) {
        if (rawPolyLineMap == null) {
            rawPolyLineMap = new HashMap<>();
        }

        switch (mapType) {
            case GAODE:
                Polyline gaodePolyLine = (Polyline) rawPolyLine;
                addRawPloyLine(mapType, gaodePolyLine);
                break;
            case BAIDU:
                com.baidu.mapapi.map.Polyline baiduPolyLine = (com.baidu.mapapi.map.Polyline) rawPolyLine;
                addRawPloyLine(mapType, baiduPolyLine);
                break;
            case GOOGLE:
                com.google.android.gms.maps.model.Polyline googlePoltLine = (com.google.android.gms.maps.model.Polyline) rawPolyLine;
                addRawPloyLine(mapType, googlePoltLine);
        }

    }

    @SuppressWarnings("unchecked")
    private <T> void addRawPloyLine(MapType mapType, T rawPolyLines) {
        List<T> polylines = (List<T>) rawPolyLineMap.get(mapType);
        if (polylines == null) {
            polylines = new ArrayList<>();
            rawPolyLineMap.put(mapType, polylines);
        }
        polylines.add(rawPolyLines);
    }


    public List<?> getRawPolyLines(MapType mapType) {
        if (rawPolyLineMap == null) {
            return null;
        }
        return rawPolyLineMap.get(mapType);
    }


    public PolyLine points(List<MapPoint> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.points.clear();
        this.points.addAll(points);
        return this;
    }

    public PolyLine points(MapPoint[] points) {
        return points(Arrays.asList(points));
    }

    public PolyLine addPoint(MapPoint point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        this.points.add(point);
        return this;
    }

    public PolyLine addPoints(List<MapPoint> points) {
        if (points != null) {
            this.points.addAll(points);
        }
        return this;
    }

    public PolyLine addPoints(MapPoint[] points) {
        return addPoints(Arrays.asList(points));
    }

    /**
     * 清除掉现有的所有的纹理，设置一个新的纹理
     *
     * @param texture 新的纹理
     * @return polyline
     */
    public PolyLine texture(PolyLineTexture texture) {
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
    public PolyLine textures(List<PolyLineTexture> textures) {
        if (textures == null || textures.size() < 1) {
            throw new IllegalArgumentException();
        }
        this.textures.clear();
        this.textures.addAll(textures);
        return this;
    }

    public PolyLine textures(PolyLineTexture[] textures) {
        return textures(Arrays.asList(textures));
    }

    public PolyLine addTexture(PolyLineTexture texture) {
        if (texture == null) {
            throw new IllegalArgumentException();
        }
        this.textures.add(texture);
        return this;
    }

    public PolyLine addTextures(List<PolyLineTexture> textures) {
        if (textures != null) {
            this.textures.addAll(textures);
        }
        return this;
    }

    public PolyLine addTextures(PolyLineTexture[] textures) {
        return addTextures(Arrays.asList(textures));
    }

    public List<MapPoint> getPoints() {
        return this.points;
    }

    public List<PolyLineTexture> getTextures() {
        return this.textures;
    }

    public PolyLine color(int color) {
        if (this.textures.isEmpty()) {
            addTexture(new ColorTexture(color));
            return this;
        }

        PolyLineHelper.sortByIndex(this.textures);
        PolyLineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolyLineHelper.removeByIndex(this.textures, index);

        addTexture(new ColorTexture(lastTexture, color).index(index));
        return this;
    }

    public PolyLine bitmap(Bitmap bitmap) {
        if (this.textures.isEmpty()) {
            addTexture(new BitmapTexture(bitmap));
            return this;
        }

        PolyLineHelper.sortByIndex(this.textures);
        PolyLineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolyLineHelper.removeByIndex(this.textures, index);

        addTexture(new BitmapTexture(lastTexture, bitmap).index(index));
        return this;
    }

    public PolyLine width(int width) {
        if (this.textures.isEmpty()) {
            addTexture(DEFAULT_TEXTURE.width(width));
            return this;
        }

        PolyLineHelper.sortByIndex(this.textures);
        PolyLineTexture lastTexture = this.textures.get(this.textures.size() - 1);

        int index = this.points.isEmpty() ? 0 : (this.points.size() - 1);
        PolyLineHelper.removeByIndex(this.textures, index);

        addTexture(lastTexture.copy().width(width).index(index));
        return this;
    }
}
