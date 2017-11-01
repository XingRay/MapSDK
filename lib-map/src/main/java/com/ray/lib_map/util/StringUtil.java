package com.ray.lib_map.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class StringUtil {
    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static void appendIfNotEmpty(StringBuilder builder, String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            builder.append("&").append(key).append("=").append(value);
        }
    }

    /**
     * 将list的数据转换为单个的String
     * 如： {"aaa", "bbb", "ccc"} -> "aaa, bbb, ccc"
     *
     * @param list element list
     * @param <T>  type of element
     * @return formatted string like "aaa, bbb, ccc"
     */
    @SuppressWarnings("SameParameterValue")
    public static <T> String fromList(List<T> list, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        if (list == null) {
            return stringBuilder.toString();
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            T t = list.get(i);
            if (t == null) {
                continue;
            }

            stringBuilder.append(t.toString());
            if (i < size - 1) {
                stringBuilder.append(separator);
            }
        }

        return stringBuilder.toString();
    }
}
