package com.ray.lib_map.entity.graph;


import com.ray.lib_map.extern.MapType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 图形
 */

public abstract class Graph {
    private Map<MapType, Object> rawGraphs;
    private int fillColor;
    private int strokeColor;
    private int strokeWidth;
    private int zIndex;

    public Graph() {
        rawGraphs = new HashMap<>(MapType.values().length);
    }

    public Graph(Graph graph) {
        this();
        this.fillColor = graph.getFillColor();
        this.strokeColor = graph.getStrokeColor();
        this.strokeWidth = graph.getStrokeWidth();
        this.zIndex = graph.getZIndex();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getZIndex() {
        return zIndex;
    }

    public Graph ZIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public Object getRawGraph(MapType mapType) {
        return rawGraphs.get(mapType);
    }

    public void setRawGraph(MapType mapType, Object rawPolygon) {
        rawGraphs.put(mapType, rawPolygon);
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "fillColor=" + fillColor +
                ", strokeColor=" + strokeColor +
                ", strokeWidth=" + strokeWidth +
                ", zIndex=" + zIndex +
                '}';
    }
}
