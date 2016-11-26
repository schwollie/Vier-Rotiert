package com.example.lars.vierrotiert;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maus on 05.10.16.
 */
public class VisualBoard implements Board.FieldListener {

    interface Listener {
        void onColumnClicked(int col);

        void onAnimationFinished();
    }

    private final Board board;
    private final GridLayout gridLayout;
    private final List<Listener> listeners = new ArrayList<>();

    private final List<ImageView> tempViews = new ArrayList<>();
    private Animator animator = null;

    public VisualBoard(Board board, GridLayout gridLayout) {
        this.board = board;
        board.addFieldListener(this);

        this.gridLayout = gridLayout;

        setSize();
        showBoard(board);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private Context getContext() {
        return gridLayout.getContext();
    }

    void setSize() {
        Display d = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = d.getWidth();
        int height = d.getHeight();

        int min = (int) (Math.min(width, height) * 0.6);
        gridLayout.getLayoutParams().width = min;
        gridLayout.getLayoutParams().height = min;

        gridLayout.setColumnCount(board.size);
        gridLayout.setRowCount(board.size);
        gridLayout.removeAllViews();
        gridLayout.setPadding(16, 16, 16, 16);
        gridLayout.setBackgroundColor(Color.BLUE);

        for (int row = 0; row < board.size; row++) {
            for (int col = 0; col < board.size; col++) {
                View childView = new ImageView(getContext());

                GridLayout.Spec rowspan = GridLayout.spec(row, 1, 1);
                GridLayout.Spec colspan = GridLayout.spec(col, 1, 1);
                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(rowspan, colspan);
                gridLayoutParam.setMargins(8, 8, 8, 8);
                gridLayout.addView(childView, gridLayoutParam);

                final int column = col;
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        raiseColumnChanged(column);
                    }
                });
            }
        }
    }

    private void showBoard(Board board) {
        int index = 0;
        for (int row = 0; row < board.size; row++) {
            for (int col = 0; col < board.size; col++, index++) {
                ImageView imageView = getImageView(row, col);
                imageView.setImageDrawable(getDrawable(board.field[row][col]));
            }
        }


    }


    private Drawable getDrawable(Board.Field field) {
        switch (field) {
            case Red:
                return gridLayout.getContext().getDrawable(R.drawable.ic_circle_black_24dp);
            case Yellow:
                return gridLayout.getContext().getDrawable(R.drawable.ic_circle_white_24dp);
            case Empty:
            default:
                return gridLayout.getContext().getDrawable(R.drawable.ic_empty_24dp);
        }
    }

    private ImageView getImageView(int row, int col) {
        int index = board.size * row + col;
        return (ImageView) gridLayout.getChildAt(index);
    }

    @Override
    public void onModificationStart() {
        dropAnimations = new ArrayList<>();
        animator = new AnimatorSet();
    }

    @Override
    public void onRotateLeft() {
        animator = ObjectAnimator.ofFloat(gridLayout, "rotation", 0f, -90f).setDuration(500);
    }

    @Override
    public void onRotateRight() {
        animator = ObjectAnimator.ofFloat(gridLayout, "rotation", 0f, 90f).setDuration(500);
    }

    @Override
    public void onRotationEnd(final Board intermediateState) {
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                gridLayout.setRotation(0);
                showBoard(intermediateState);
            }
        });
    }

    private class DropAnimation {
        int col;
        int startRow;
        int endRow;
        Board.Field field;

        public DropAnimation(int col, int startRow, int endRow, Board.Field field) {
            this.col = col;
            this.startRow = startRow;
            this.endRow = endRow;
            this.field = field;
        }
    }

    private List<DropAnimation> dropAnimations;

    @Override
    public void onDrop(final int col, final int startRow, final int endRow, final Board.Field field) {
        dropAnimations.add(new DropAnimation(col, startRow, endRow, field));
    }

    @Override
    public void onModificationEnd() {
        new Handler(getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                List<Animator> animations = new ArrayList<>();

                for (DropAnimation dropAnimation : dropAnimations) {
                    ImageView startView = getImageView(dropAnimation.startRow, dropAnimation.col);

                    ImageView animatedView = new ImageView(gridLayout.getContext());
                    animatedView.setImageDrawable(getDrawable(dropAnimation.field));
                    startView.setImageDrawable(getDrawable(Board.Field.Empty));
                    gridLayout.addView(animatedView, startView.getLayoutParams());
                    tempViews.add(animatedView);

                    ImageView destView = getImageView(dropAnimation.endRow, dropAnimation.col);

                    animations.add(ObjectAnimator.ofFloat(animatedView, "y", startView.getY(), destView.getY()).setDuration(500));
                }

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animations);
                animatorSet.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        for (ImageView view : tempViews) {
                            gridLayout.removeView(view);
                        }
                        tempViews.clear();

                        showBoard(board);
                        raiseAnimationFinished();
                    }
                });
                animatorSet.start();
            }
        });

        animator.start();
    }

    private void raiseColumnChanged(int col) {
        for (Listener listener : listeners) {
            listener.onColumnClicked(col);
        }
    }

    private void raiseAnimationFinished() {
        for (Listener listener : listeners) {
            listener.onAnimationFinished();
        }
    }
}