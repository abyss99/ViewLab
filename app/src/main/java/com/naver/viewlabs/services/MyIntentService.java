package com.naver.viewlabs.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.naver.viewlabs.log.Ln;

/**
 * Created by abyss on 2018. 3. 27..
 */

public class MyIntentService extends IntentService {
    public MyIntentService() {
        super("default");

    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Ln.d("onHandleIntent");
        SystemClock.sleep(3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Ln.d("onDestroy");
    }
}
