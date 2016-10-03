package com.example.lars.vierrotiert;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by maus on 26.09.16.
 */
public class HomeScreen extends GameActivity implements View.OnClickListener {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_test);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        FrameLayout frameleft = (FrameLayout) findViewById(R.id.frameleft);
        frameleft.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frameleft) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.test);
            final Animation b = AnimationUtils.loadAnimation(this, R.anim.testtwo);

            final FrameLayout btn2 = (FrameLayout) findViewById(R.id.frameLayout3);
            btn2.startAnimation(a);

            final RelativeLayout main = (RelativeLayout) findViewById(R.id.main);


            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //main.setBackgroundColor(Color.BLACK);
                    btn2.startAnimation(b);

                }
            });
        }
    }


}
