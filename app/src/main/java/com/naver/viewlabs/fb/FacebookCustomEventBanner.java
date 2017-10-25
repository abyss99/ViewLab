package com.naver.viewlabs.fb;

import android.content.Context;
import android.os.Bundle;

import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

/**
 * Created by abyss on 2017. 8. 23..
 */

public class FacebookCustomEventBanner implements CustomEventBanner {
    private AdView anBanner;

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener customEventBannerListener, String serverParameter, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        anBanner = new AdView(context, serverParameter, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        anBanner.setAdListener(new FacebookCustomEventBannerForwarder(customEventBannerListener, anBanner));
        anBanner.loadAd();
    }

    @Override
    public void onDestroy() {
        if (anBanner != null) {
            anBanner.destroy();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
