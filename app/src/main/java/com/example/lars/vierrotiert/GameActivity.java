package com.example.lars.vierrotiert;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements View.OnClickListener, Board.FieldListener {

    private static final String TAG = "GameActivity";
    private final List<ImageView> tempViews = new ArrayList<>();
    private int size = 6;
    private Board board = new Board(size);
    private GridLayout gridLayout;
    private Board.Field currentPlayer;
    private Player player;
    private ImageButton btnleft;
    private ImageButton btnright;
    private List<Animator> dropAnimations;
    private FrameLayout animationOverlayLayout;
    private boolean onClickAble = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);

        //animationOverlayLayout = (FrameLayout) findViewById(R.id.animation_overlay);
        gridLayout = (GridLayout) findViewById(R.id.field);
        btnleft = (ImageButton) findViewById(R.id.rotate_left);
        btnright = (ImageButton) findViewById(R.id.rotate_right);

        currentPlayer = Board.Field.Red;
        board.addFieldLietener(this);

        //player = new RandomPlayer(Board.Field.Yellow);
        player = new MinimaxPlayer(Board.Field.Yellow);

        setSize();
        showBoard();

        ImageButton rotateLeftButton = (ImageButton) findViewById(R.id.rotate_left);
        rotateLeftButton.setOnClickListener(this);

        ImageButton rotateRightButton = (ImageButton) findViewById(R.id.rotate_right);
        rotateRightButton.setOnClickListener(this);
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
                        if (onClickAble == true && board.isFree(column)) {
                            onClickAble = false;
                            Log.i(TAG, "set column " + column);
                            board.set(column, currentPlayer);
                        }
                    }
                });
            }
        }
    }

    private void switchPlayer() {
        switch (currentPlayer) {
            case Red:
                currentPlayer = Board.Field.Yellow;
                break;
            case Yellow:
                currentPlayer = Board.Field.Red;
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
                imageView.setImageDrawable(getDrawable(board.get(row, col)));
            }
        }
    }

    Drawable getDrawable(Board.Field field) {
        switch (field) {
            case Red:
                return getDrawable(R.drawable.ic_circle_black_24dp);
            case Yellow:
                return getDrawable(R.drawable.ic_circle_white_24dp);
            case Empty:
            default:
                return getDrawable(R.drawable.ic_empty_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        if (onClickAble == true) {
            onClickAble = false;
            if (view.getId() == R.id.rotate_left) {
                board.rotateLeft();
            } else if (view.getId() == R.id.rotate_right) {
                board.rotateRight();
            }
        }
    }

    @Override
    public void onRotateLeft() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate_left);
        Animation b = AnimationUtils.loadAnimation(this, R.anim.rotate_left_90);

        ImageButton btn2 = (ImageButton) findViewById(R.id.rotate_left);
        b.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                showBoard();
                board.applyGravity();
            }
        });
        btn2.startAnimation(a);
        gridLayout.startAnimation(b);
    }

    @Override
    public void onRotateRight() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
        Animation b = AnimationUtils.loadAnimation(this, R.anim.rotate_right_90);

        ImageButton btn2 = (ImageButton) findViewById(R.id.rotate_right);

        b.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                showBoard();
                board.applyGravity();
            }
        });
        btn2.startAnimation(a);
        gridLayout.startAnimation(b);

    }

    @Override
    public void onStartDrop() {
        onClickAble = false;
        Log.i(TAG, "onStartDrop: ");
        dropAnimations = new ArrayList<>();
    }

    @Override
    public void onDrop(int col, int startRow, int endRow, Board.Field field) {
        Log.i(TAG, "onDrop: " + col + ", " + startRow + ", " + endRow);
        ImageView startView = getImageView(startRow, col);

        ImageView animatedView = new ImageView(this);
        animatedView.setImageDrawable(getDrawable(field));
        startView.setImageDrawable(getDrawable(Board.Field.Empty));
        gridLayout.addView(animatedView, startView.getLayoutParams());
        //animationOverlayLayout.addView(animatedView, new FrameLayout.LayoutParams(startView.getWidth(), startView.getHeight()));
        tempViews.add(animatedView);

        ImageView destView = getImageView(endRow, col);
        dropAnimations.add(ObjectAnimator.ofFloat(animatedView, "y", startView.getY(), destView.getY()).setDuration(1000));
    }

    @Override
    public void onEndDrop() {
        Log.i(TAG, "onEndDrop: ");
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i(TAG, "onAnimationEnd: ");

                for (ImageView view : tempViews) {
                    gridLayout.removeView(view);
                }
                tempViews.clear();
                onClickAble = true;

                showBoard();
                nextTurn();
            }
        });
        animatorSet.playTogether(dropAnimations);
        animatorSet.start();
    }

    private void nextTurn() {
        Winner winner = board.isWinner();
        if (winner != Winner.None && winner != Winner.Both) {
            Toast.makeText(GameActivity.this, "The winner is " + winner.name(), Toast.LENGTH_SHORT).show();
            onClickAble = false;
            return;
        } else if (winner == Winner.Both) {
            Toast.makeText(GameActivity.this, "Both are winner", Toast.LENGTH_SHORT).show();
            onClickAble = false;
        } else {
            switchPlayer();
        }

        if (board.isFull() == true) {
            onClickAble = false;
            Toast.makeText(GameActivity.this, "Every field is full!", Toast.LENGTH_SHORT).show();
            return;

        }

        if (currentPlayer == player.getField()) {
            Move move = player.set(board);
            switch (move.type) {
                case RotateLeft:
                    board.rotateLeft();
                    break;
                case RotateRight:
                    board.rotateRight();
                    break;
                case SetColumn:
                    board.set(move.column, move.player);
                    break;
            }
        } else {
            onClickAble = true;
        }
    }
}


