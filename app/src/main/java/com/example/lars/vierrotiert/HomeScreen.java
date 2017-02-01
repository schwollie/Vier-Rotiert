package com.example.lars.vierrotiert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
                    progress = progress + 10;
                    if (progress > 50) {
                        progress = 50;
                    }
                    //editor.putInt("level", progress);
                    TextView txt = (TextView) findViewById(R.id.textView3);
                    txt.setText(progress / 10 + "/5");
                    TextView txt2 = (TextView) findViewById(R.id.textView4);
                    level = progress / 10;
                    progress = progress / 10;
                    if (progress == 1) {
                        txt2.setText("(VERY EASY)");
                    } else if (progress == 2) {
                        txt2.setText("(EASY)");
                    } else if (progress == 3) {
                        txt2.setText("(NORMAL)");
                    } else if (progress == 4) {
                        txt2.setText("(HARD)");
                    } else if (progress == 5) {
                        txt2.setText("(WIZARD)");
                    } else if (progress == 6) {
                        txt2.setText("(HARD)");
                    } else if (progress == 7) {
                        txt2.setText("(WIZARD)");
                    }


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
                    //editor.putInt("board-size", progress);


                    //editor.commit(board_size);


                    TextView txt2 = (TextView) findViewById(R.id.textView7);
                    if (progress > 30) {
                        progress = 30;
                    }
                    int x = progress / 10 + 4;
                    board_size = x;

                    TextView txt = (TextView) findViewById(R.id.textView6);
                    txt.setText(x + "x" + x);

                    if (progress >= 30) {
                        txt2.setText("(XXL)");
                    } else if (progress >= 20) {
                        txt2.setText("(XL)");
                    } else if (progress >= 10) {
                        txt2.setText("(NORMAL)");
                    } else if (progress >= 1) {
                        txt2.setText("(SMALL)");
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
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ImageButton imgbtn = (ImageButton) findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(this);


        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(customSeekBarListener);

        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setProgress(10);
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

        if (view.getId() == R.id.button2) {
            Toast.makeText(getBaseContext(), "Coming Soon!",
                    Toast.LENGTH_LONG).show();
        }

        if (view.getId() == R.id.imageButton) {
            if (isBtnTextPlayerVsPlayer == false && board_size > 6 && level > 4) {
                Toast.makeText(getBaseContext(), "So much math! Coming Soon!!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (isBtnTextPlayerVsPlayer == false && board_size > 5 && level > 3) {
                Toast.makeText(getBaseContext(), "Maybe it's too much for your mobile phone! Coming Soon!!",
                        Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("playerVsPlayer", isBtnTextPlayerVsPlayer);
            intent.putExtra("level", level);
            intent.putExtra("board_size", board_size);
            startActivity(intent);
        }
    }
}
