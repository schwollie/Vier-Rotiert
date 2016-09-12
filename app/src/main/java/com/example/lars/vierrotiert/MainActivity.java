package com.example.lars.vierrotiert;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int size = 5;
    private Board board = new Board(size);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);

        setSize();

        board = board.set(0, Board.Field.Black);
        board = board.set(1, Board.Field.White);
        board = board.set(1, Board.Field.Black);
        board = board.set(0, Board.Field.White);
        board = board.set(2, Board.Field.Black);
        board = board.set(0, Board.Field.White);
        board = board.set(3, Board.Field.Black);

        showBoard();

        ImageButton rotateLeftButton = (ImageButton) findViewById(R.id.rotate_left);
        rotateLeftButton.setOnClickListener(this);


        ImageButton rotateRightButton = (ImageButton) findViewById(R.id.rotate_right);
        rotateRightButton.setOnClickListener(this);
    }

    void setSize() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.field);

        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = d.getWidth();
        int height = d.getHeight();

        int min = (int) (Math.min(width, height) * 0.6);
        gridLayout.getLayoutParams().width = min;
        gridLayout.getLayoutParams().height = min;

        gridLayout.setColumnCount(size);
        gridLayout.setRowCount(size);
        gridLayout.removeAllViews();
        gridLayout.setPadding(64, 64, 64, 64);
        gridLayout.setBackgroundColor(Color.BLUE);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                View childView = new ImageView(this);


                GridLayout.Spec rowspan = GridLayout.spec(row, 1, 1);
                GridLayout.Spec colspan = GridLayout.spec(col, 1, 1);
                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(rowspan, colspan);
                gridLayoutParam.setMargins(8, 8, 8, 8);
                gridLayout.addView(childView, gridLayoutParam);
            }
        }
    }

    void showBoard() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.field);
        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++, index++) {
                ImageView imageView = (ImageView) gridLayout.getChildAt(index);
                switch (board.get(row, col)) {
                    case Black:
                        imageView.setImageDrawable(getDrawable(R.drawable.ic_circle_black_24dp));

                        break;
                    case White:
                        imageView.setImageDrawable(getDrawable(R.drawable.ic_circle_white_24dp));

                        break;
                    case Empty:

                        imageView.setImageDrawable(getDrawable(R.drawable.ic_empty_24dp));

                        break;

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rotate_left) {
            GridLayout gridLayout = (GridLayout) findViewById(R.id.field);


            Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate_left);
            Animation b = AnimationUtils.loadAnimation(this, R.anim.rotate_left_90);

            ImageButton btn2 = (ImageButton) findViewById(R.id.rotate_left);
            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    board = board.rotateLeft();
                    showBoard();

                }
            });
            btn2.startAnimation(a);
            gridLayout.startAnimation(b);

        } else if (view.getId() == R.id.rotate_right) {
            GridLayout gridLayout = (GridLayout) findViewById(R.id.field);



            Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
            Animation b = AnimationUtils.loadAnimation(this, R.anim.rotate_right_90);
            Animation c = AnimationUtils.loadAnimation(this, R.anim.smaller);


            ImageButton btn2 = (ImageButton) findViewById(R.id.rotate_right);

            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {


                    board = board.rotateRight();
                    showBoard();

                }
            });
            btn2.setAnimation(c);
            btn2.startAnimation(a);
            gridLayout.startAnimation(b);

        }


    }
}


