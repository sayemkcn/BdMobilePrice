package net.toracode.mobilepricebd.commons;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import net.toracode.mobilepricebd.R;

/**
 * Created by sayemkcn on 10/3/16.
 */

public class Ads {

    private InterstitialAd interstitial;
    private Activity context;

    public Ads(Activity context) {
        this.context = context;
    }

    public void loadInterstitial() {
        // Create the interstitial.
        this.interstitial = new InterstitialAd(this.context);
        interstitial.setAdUnitId(this.context.getResources().getString(R.string.intertitial_ad_unit_id));

        // Create ad request.
        AdRequest intAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(this.context.getResources().getString(R.string.device_id))
                .build();

        // Begin loading your interstitial.
        interstitial.loadAd(intAdRequest);

        // End Loading Interstitial
    }
    public void displayInterstitial(){
        if (this.interstitial.isLoaded())
            interstitial.show();
    }

    public void loadBannerAd(View rootView,int adViewId) {
        // Load Ads
        AdView mAdView = (AdView) rootView.findViewById(adViewId);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(this.context.getResources().getString(R.string.device_id))
                .build();
        mAdView.loadAd(adRequest);
        // End Ad Loading
    }
}
