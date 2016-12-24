package com.example.lars.vierrotiert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by maus on 26.09.16.
 */
public class HomeScreen extends GameActivity implements View.OnClickListener {
    private boolean isBtnTextPlayerVsPlayer = true;
    private int level = 5;
    private int board_size = 5;
    private SeekBar.OnSeekBarChangeListener customSeekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    progress = progress + 1;
                    //editor.putInt("level", progress);
                    TextView txt = (TextView) findViewById(R.id.textView3);
                    txt.setText(progress + "/7");
                    TextView txt2 = (TextView) findViewById(R.id.textView4);
                    if (progress == 1) {
                        txt2.setText("(You Winn)");
                    } else if (progress == 2) {
                        txt2.setText("(VERY EASY)");
                    } else if (progress == 3) {
                        txt2.setText("(EASY)");
                    } else if (progress == 4) {
                        txt2.setText("(EASY NORMAL)");
                    } else if (progress == 5) {
                        txt2.setText("(HARD NORMAL)");
                    } else if (progress == 6) {
                        txt2.setText("(HARD)");
                    } else if (progress == 7) {
                        txt2.setText("(VERY HARD)");
                    }

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
    private SeekBar.OnSeekBarChangeListener customSeekBarListener2 =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar2, int progress, boolean b) {
                    progress = progress + 4;
                    //editor.putInt("board-size", progress);

                    board_size = progress;
                    //editor.commit(board_size);

                    TextView txt = (TextView) findViewById(R.id.textView6);
                    txt.setText(progress + "x" + progress);

                    TextView txt2 = (TextView) findViewById(R.id.textView7);

                    if (progress == 4) {
                        txt2.setText("(SMALL)");
                    } else if (progress == 5) {
                        txt2.setText("(NORMAL)");
                    } else if (progress == 6) {
                        txt2.setText("(XL)");
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_test);

        /*MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*
      ATTENTION: This was auto-generated to implement the App Indexing API.
      See https://g.co/AppIndexing/AndroidStudio for more information.
     */
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //   sharedPreferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        //  editor = sharedPreferences.edit();
//        editor.putInt("level", 1);
//        editor.putInt("XP",xp);
//        editor.putBoolean("permission-XP", false);
//        editor.commit();

        //editor.putInt("XP", sharedPreferences.getInt("XP",0)+1);
        //editor.commit();

        ImageButton imgbtn = (ImageButton) findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(this);


        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(level);
        seekBar.setOnSeekBarChangeListener(customSeekBarListener);

        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setProgress(board_size);
        seekBar2.setOnSeekBarChangeListener(customSeekBarListener2);

        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setAlpha(200);
    }

    @Override
    public void onClick(View view) {

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

        if (view.getId() == R.id.imageButton) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("playerVsPlayer", isBtnTextPlayerVsPlayer);
            intent.putExtra("level", level);
            intent.putExtra("board_size", board_size);
            startActivity(intent);
        }
    }


}
