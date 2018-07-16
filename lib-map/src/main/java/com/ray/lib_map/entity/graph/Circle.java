package com.ray.lib_map.entity.graph;


import com.ray.lib_map.entity.MapPoint;

/**
 * @author      : leixing
 * Date        : 2017-07-13
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class Circle extends Graph {
    /**
     * 圆心
     */
    private MapPoint center;

    /**
     * 半径
     */
    private double radius;

    public Circle(MapPoint center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public MapPoint getCenter() {
        return center;
    }

    public void setCenter(MapPoint center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
