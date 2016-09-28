package com.example.lars.vierrotiert;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * Created by maus on 26.09.16.
 */
public class HomeScreen extends GameActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_test);


        FrameLayout test = (FrameLayout) findViewById(R.id.frameLayout3);
        test.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frameLayout3) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.test);

            FrameLayout btn2 = (FrameLayout) findViewById(R.id.frameLayout3);
            btn2.startAnimation(a);
        }
    }





}
