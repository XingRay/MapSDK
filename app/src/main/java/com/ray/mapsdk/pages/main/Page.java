package com.ray.mapsdk.pages.main;

import android.app.Activity;
import android.content.Intent;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class Page {
    private String name;
    private Class<? extends Activity> cls;

    public Page(String name, Class<? extends Activity> cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends Activity> getCls() {
        return cls;
    }

    public void setCls(Class<? extends Activity> cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "Page{" +
                "name='" + name + '\'' +
                ", cls=" + cls +
                '}';
    }


    public void start(Activity activity) {
        start(activity, 100);
    }

    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
    }
}
