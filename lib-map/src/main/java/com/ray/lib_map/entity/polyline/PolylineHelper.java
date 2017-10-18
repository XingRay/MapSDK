package com.ray.lib_map.entity.polyline;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Author      : leixing
 * Date        : 2017-10-18
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class PolylineHelper {
    private PolylineHelper() {
        throw new UnsupportedOperationException();
    }

    public static void sortByIndex(List<PolylineTexture> textures) {
        if (textures == null) {
            return;
        }

        Collections.sort(textures, new Comparator<PolylineTexture>() {
            @Override
            public int compare(PolylineTexture o1, PolylineTexture o2) {
                if (o1 == null) {
                    if (o2 == null) {
                        return 0;
                    } else {
                        return 1;
                    }
                }

                if (o2 == null) {
                    return -1;
                }

                int index1 = o1.getIndex();
                int index2 = o2.getIndex();

                return index1 < index2 ? -1 : index1 > index2 ? 1 : 0;
            }
        });
    }

    public static boolean removeByIndex(List<PolylineTexture> textures, int index) {
        boolean removed = false;
        if (textures == null) {
            return false;
        }

        Iterator<PolylineTexture> iterator = textures.iterator();
        while (iterator.hasNext()) {
            PolylineTexture texture = iterator.next();
            if (texture.getIndex() >= index) {
                iterator.remove();
                removed = true;
            }
        }

        return removed;
    }
}
