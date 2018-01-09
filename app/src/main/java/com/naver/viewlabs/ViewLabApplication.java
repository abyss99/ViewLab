package com.naver.viewlabs;

import android.support.multidex.MultiDexApplication;

import com.naver.viewlabs.log.Ln;

/**
 * Created by abyss on 2017. 12. 5..
 */

public class ViewLabApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Ln.init(this);
    }
}
