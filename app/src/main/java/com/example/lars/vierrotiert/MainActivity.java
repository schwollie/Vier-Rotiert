package com.example.lars.vierrotiert;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Board.FieldListener {

    private int size = 5;
    private Board board = new Board(size);
    private GridLayout gridLayout;
    private Board.Field player;
    private ImageButton btnleft;
    private ImageButton btnright;
    private List<Animator> dropAnimations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);

        gridLayout = (GridLayout) findViewById(R.id.field);
        btnleft = (ImageButton) findViewById(R.id.rotate_left);
        btnright = (ImageButton) findViewById(R.id.rotate_right);

        player = Board.Field.Black;
        board.addFieldLietener(this);

        setSize();
        showBoard();

        ImageButton rotateLeftButton = (ImageButton) findViewById(R.id.rotate_left);
        rotateLeftButton.setOnClickListener(this);


        ImageButton rotateRightButton = (ImageButton) findViewById(R.id.rotate_right);
        rotateRightButton.setOnClickListener(this);
    }

    void Animation() {

        ObjectAnimator objectAnimator = new ObjectAnimator();


        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btnright, "scaleX", 1.0f, 2.0f)
                        .setDuration(2000),
                ObjectAnimator.ofFloat(btnleft, "scaleX", 1.0f, 2.0f)
                        .setDuration(2000)


        );
        set.start();
    }


    void setSize() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.field);

        ImageButton turn_right = (ImageButton) findViewById(R.id.rotate_right);
        ImageButton turn_left = (ImageButton) findViewById(R.id.rotate_left);


        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = d.getWidth();
        int height = d.getHeight();

        turn_left.getLayoutParams().width = width / 8;
        turn_left.getLayoutParams().height = width / 8;
        turn_right.getLayoutParams().width = width / 8;
        turn_right.getLayoutParams().height = width / 8;

        int min = (int) (Math.min(width, height) * 0.6);
        gridLayout.getLayoutParams().width = min;
        gridLayout.getLayoutParams().height = min;

        gridLayout.setColumnCount(size);
        gridLayout.setRowCount(size);
        gridLayout.removeAllViews();
        gridLayout.setPadding(16, 16, 16, 16);
        gridLayout.setBackgroundColor(Color.BLUE);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                View childView = new ImageView(this);

                GridLayout.Spec rowspan = GridLayout.spec(row, 1, 1);
                GridLayout.Spec colspan = GridLayout.spec(col, 1, 1);
                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(rowspan, colspan);
                gridLayoutParam.setMargins(8, 8, 8, 8);
                gridLayout.addView(childView, gridLayoutParam);

                final int column = col;
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        board.set(column, player);
                        showBoard();
                        switchPlayer();
                    }
                });
            }
        }
    }

    private void switchPlayer() {
        switch (player) {
            case Black:
                player = Board.Field.White;
                break;
            case White:
                player = Board.Field.Black;
                break;
        }
    }

    private ImageView getImageView(int row, int col) {
        int index = size * row + col;
        return (ImageView) gridLayout.getChildAt(index);
    }

    void showBoard() {
        int i = size * size;

        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++, index++) {
                ImageView imageView = getImageView(row, col);
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

                    board.rotateLeft();
                    board.applyGravity();

                    showBoard();
                    board.isWinner();
                    switchPlayer();

                }
            });
            btn2.startAnimation(a);
            gridLayout.startAnimation(b);

        } else if (view.getId() == R.id.rotate_right) {
            GridLayout gridLayout = (GridLayout) findViewById(R.id.field);


            Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
            Animation b = AnimationUtils.loadAnimation(this, R.anim.rotate_right_90);


            ImageButton btn2 = (ImageButton) findViewById(R.id.rotate_right);

            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    board.rotateRight();
                    board.applyGravity();


                    showBoard();
                    board.isWinner();
                    switchPlayer();

                }
            });
            btn2.startAnimation(a);
            gridLayout.startAnimation(b);

        }


    }

    @Override
    public void onStartDrop() {
        dropAnimations = new ArrayList<>();
    }

    @Override
    public void onDrop(int col, int startRow, int endRow) {
        ImageView startView = getImageView(startRow, col);
        ImageView destView = getImageView(endRow, col);
        dropAnimations.add(ObjectAnimator.ofFloat(startView, "y", startView.getY(), destView.getY()).setDuration(1000));
    }

    @Override
    public void onEndDrop() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(dropAnimations);
        animatorSet.start();
    }
}


