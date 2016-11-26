package com.example.lars.vierrotiert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.SharedPreferences;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by maus on 26.09.16.
 */
public class HomeScreen extends GameActivity implements View.OnClickListener {
    private boolean isBtnTextPlayerVsPlayer = true;
    private int level = 5;

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
        /*
      ATTENTION: This was auto-generated to implement the App Indexing API.
      See https://g.co/AppIndexing/AndroidStudio for more information.
     */
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//
//        sharedPreferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.putInt("level", 1);
//        editor.putInt("XP",xp);
//        editor.putBoolean("permission-XP", false);
//        editor.commit();

        //editor.putInt("XP", sharedPreferences.getInt("XP",0)+1);
        //editor.commit();

        FrameLayout frameleft = (FrameLayout) findViewById(R.id.frameleft);
        frameleft.setOnClickListener(this);

        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);

        FrameLayout framelayout4 = (FrameLayout) findViewById(R.id.frameLayout4);
        framelayout4.setOnClickListener(this);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(level);
        seekBar.setOnSeekBarChangeListener(customSeekBarListener);
    }

    private SeekBar.OnSeekBarChangeListener customSeekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    progress = progress + 1;
                    //editor.putInt("level", progress);
                    TextView txt = (TextView) findViewById(R.id.textView2);
                    txt.setText("Computer Level: " + progress);
                    level = progress;
                    //editor.commit();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frameleft) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.test);
            final Animation b = AnimationUtils.loadAnimation(this, R.anim.testtwo);

            final FrameLayout btn2 = (FrameLayout) findViewById(R.id.frameLayout3);
            btn2.startAnimation(a);

            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //main.setBackgroundColor(Color.BLACK);
                    btn2.startAnimation(b);

                }
            });
        }

        if (view.getId() == R.id.button) {
            Button btn1 = (Button) findViewById(R.id.button);
            if (isBtnTextPlayerVsPlayer == true) {
                btn1.setText("PLAYER VS COMPUTER");
                isBtnTextPlayerVsPlayer = false;
                return;
            } else {
                btn1.setText("PLAYER VS PLAYER");
                isBtnTextPlayerVsPlayer = true;
                return;
            }
        }

        if (view.getId() == R.id.frameLayout4) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("playerVsPlayer", isBtnTextPlayerVsPlayer);
            intent.putExtra("level", level);
            startActivity(intent);
        }
    }


}
