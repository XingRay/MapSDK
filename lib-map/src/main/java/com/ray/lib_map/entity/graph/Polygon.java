package com.ray.lib_map.entity.graph;


import com.ray.lib_map.entity.MapPoint;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 多边形
 */

public class Polygon extends Graph {
    private List<MapPoint> points;

    public Polygon(List<MapPoint> points) {
        this.points = points;
    }

    public List<MapPoint> getPoints() {
        return points;
    }

    public void setPoints(List<MapPoint> points) {
        this.points = points;
    }

    public void addPoints(List<MapPoint> points) {
        if (points != null) {
            this.points.addAll(points);
        }
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "points=" + points +
                "} " + super.toString();
    }
}
