package com.example.lars.vierrotiert;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

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
    }

    void setSize() {


        GridLayout gridLayout = (GridLayout) findViewById(R.id.field);

        RelativeLayout right = (RelativeLayout) findViewById(R.id.rechts);

        RelativeLayout left = (RelativeLayout) findViewById(R.id.links);


        gridLayout.setColumnCount(size);
        gridLayout.setRowCount(size);
        gridLayout.removeAllViews();


        Display display = getWindowManager().getDefaultDisplay();
        int stageWidth = display.getWidth();
        int stageheight = display.getHeight();





        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                View childView = new ImageView(this);

                GridLayout.Spec rowspan = GridLayout.spec(row, 1);
                GridLayout.Spec colspan = GridLayout.spec(col, 1);
                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(rowspan, colspan);


                int height = (stageheight / 7) / 2;


                gridLayoutParam.width = stageWidth / 7;
                gridLayoutParam.height = gridLayoutParam.width;
                gridLayout.setBackgroundColor(Color.BLUE);
                gridLayout.setPadding(gridLayoutParam.width, height, 0, 0);



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
}


