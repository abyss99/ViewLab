package com.naver.viewlabs.fb;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.facebook.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

/**
 * Created by abyss on 2017. 8. 23..
 */

public class FacebookCustomEventBannerForwarder implements AdListener {
    private CustomEventBannerListener mBannerListener;
    private AdView mAdView;

    public FacebookCustomEventBannerForwarder(
            CustomEventBannerListener listener, AdView adView) {
        mBannerListener = listener;
        mAdView = adView;
    }


    @Override
    public void onError(Ad ad, AdError adError) {
        switch (adError.getErrorCode()) {
            case AdError.INTERNAL_ERROR_CODE:
                mBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                break;
            case AdError.NETWORK_ERROR_CODE:
                mBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                break;
            default:
                mBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                break;
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mBannerListener.onAdLoaded(mAdView);
    }

    @Override
    public void onAdClicked(Ad ad) {
        mBannerListener.onAdClicked();
        mBannerListener.onAdOpened();
        mBannerListener.onAdLeftApplication();
    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }
}
