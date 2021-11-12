package com.onex.trending_wallpapers_2021;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;

public class Google_Ads{
    public static AdView adView;
    public static InterstitialAd mInterstitialAd;
    public static NativeAd nativeAd;;
    Context context;
    AdRequest adRequest;

    public Google_Ads(Context context) {
        this.context = context;
      //  AudienceNetworkAds.initialize(context);
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adRequest = new AdRequest.Builder().build();

    }

    void bannerLoad(AdView banner_adview) {
        banner_adview.loadAd(adRequest);
    }

    void loadInterstitial() {
        InterstitialAd.load(context, context.getString(R.string.interstitial_unit_id), adRequest,  new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }
    void showInterstitial(Context context)
    {
        if(mInterstitialAd!=null)
        {
            mInterstitialAd.show((Activity)context);
            loadInterstitial();
        }
    }

}

