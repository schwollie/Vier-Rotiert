package com.example.lars.vierrotiert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.test);


            Button btn = (Button) findViewById(R.id.button);
            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                }
            });
            btn.startAnimation(a);
        }

    }
}
