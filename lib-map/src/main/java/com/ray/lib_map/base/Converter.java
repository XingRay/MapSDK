package com.ray.lib_map.base;

/**
 * @author : leixing
 * @date : 2018/7/17 14:41
 * <p>
 * description : xxx
 */
public interface Converter<T, E> {
    /**
     * convert
     */
    E convert(T t);
}
