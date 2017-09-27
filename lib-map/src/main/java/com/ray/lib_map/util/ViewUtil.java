package com.ray.lib_map.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.view.View.NO_ID;

/**
 * Author      : leixing
 * Date        : 2017-09-27
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class ViewUtil {
    private ViewUtil() {
        throw new UnsupportedOperationException();
    }

    public static View replaceView(View oldView, View newView) {
        final ViewParent viewParent = oldView.getParent();
        if (viewParent == null || !(viewParent instanceof ViewGroup)) {
            throw new IllegalStateException("must have a non-null ViewGroup viewParent");
        }
        ViewGroup parent = (ViewGroup) viewParent;

        int id = oldView.getId();
        if (id != NO_ID) {
            newView.setId(id);
        }

        final int index = parent.indexOfChild(oldView);
        parent.removeViewAt(index);

        final ViewGroup.LayoutParams layoutParams = oldView.getLayoutParams();
        if (layoutParams == null) {
            parent.addView(newView, index);
        } else {
            parent.addView(newView, index, layoutParams);
        }

        return newView;
    }
}
