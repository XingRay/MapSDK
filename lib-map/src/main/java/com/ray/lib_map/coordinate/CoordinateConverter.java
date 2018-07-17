package com.ray.lib_map.coordinate;

import com.ray.lib_map.base.Converter;

/**
 * @author : leixing
 * @date : 2018/7/17 11:24
 * <p>
 * description : xxx
 */
public class CoordinateConverter {
    private String from;
    private String to;
    private Converter<Coordinate, Coordinate> converter;

    public CoordinateConverter(String from, String to, Converter<Coordinate, Coordinate> converter) {
        this.from = from;
        this.to = to;
        this.converter = converter;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Converter<Coordinate, Coordinate> getConverter() {
        return converter;
    }

    public void setConverter(Converter<Coordinate, Coordinate> converter) {
        this.converter = converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoordinateConverter that = (CoordinateConverter) o;

        return from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
