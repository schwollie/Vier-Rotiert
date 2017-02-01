package com.example.lars.vierrotiert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements VisualBoard.Listener, GameController.Listener {

//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;

    private int size = 5;
    private Board board = new Board(size);
    private VisualBoard visualBoard;
    private Player playerRed;
    private Player playerYellow;
    private GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);
        Intent intent = this.getIntent();
        int board_size = intent.getIntExtra("board_size", 5);
        size = board_size;

        board = new Board(size);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.field);
        visualBoard = new VisualBoard(board, gridLayout);
        visualBoard.addListener(this);

        playerRed = new HumanPlayer(Board.Field.Red, board, visualBoard);


        boolean isBtnTextPlayerVsPlayer = intent.getBooleanExtra("playerVsPlayer", true);

        if (isBtnTextPlayerVsPlayer == true) {
            final HumanPlayer humanPlayer = new HumanPlayer(Board.Field.Yellow, board, visualBoard);
            playerYellow = humanPlayer;
        } else {
            int level = intent.getIntExtra("level", 0);
            playerYellow = new MinimaxPlayer(Board.Field.Yellow, level);
        }

        gameController = new GameController(board, this, false);
        gameController.playGame(playerRed, playerYellow);

        ImageButton rotateLeftButton = (ImageButton) findViewById(R.id.rotate_left);
        rotateLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HumanPlayer player = (HumanPlayer) gameController.getCurrentPlayer();
                    player.onRotateLeft();
                } catch (Exception e) {

                }
            }
        });

        ImageButton rotateRightButton = (ImageButton) findViewById(R.id.rotate_right);
        rotateRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HumanPlayer player = (HumanPlayer) gameController.getCurrentPlayer();
                    player.onRotateRight();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onColumnClicked(int column) {
    }

    @Override
    public void onAnimationFinished() {
        gameController.nextTurn();
    }

    @Override
    public void gameOver(Winner winner) {
        if (winner != Winner.None && winner != Winner.Both) {
            Toast.makeText(GameActivity.this, "The winner is " + winner.name(), Toast.LENGTH_LONG).show();
//            if(sharedPreferences.getBoolean("permission-XP",false)==true&&winner==Winner.Red) {
//                editor.putInt("XP", sharedPreferences.getInt("XP",0)+1);
//                editor.commit();
//            }
//            Intent intent = new Intent(this, HomeScreen.class);
//            startActivity(intent);
        } else if (winner == Winner.Both) {
            Toast.makeText(GameActivity.this, "Both are winner", Toast.LENGTH_LONG).show();
//            if(sharedPreferences.getBoolean("permission-XP",false)==true) {
//                editor.putInt("XP", sharedPreferences.getInt("XP",0)+1);
//                editor.commit();
//            }
//            Intent intent = new Intent(this, HomeScreen.class);
//            startActivity(intent);
        } else {
            Toast.makeText(GameActivity.this, "No winner - it's a draw", Toast.LENGTH_LONG).show();
//            if(sharedPreferences.getBoolean("permission-XP",false)==true) {
//                editor.putInt("XP", sharedPreferences.getInt("XP",0)+1);
//                editor.commit();
//            }
//            Intent intent = new Intent(this, HomeScreen.class);
//            startActivity(intent);
        }
    }

    @Override
    public void currentPlayerChanged(Player player) {
        if (player.getField() == Board.Field.Red) {
            TextView txt = (TextView) findViewById(R.id.whichplayer);
            txt.setText("Player: Red");
        } else if (player.getField() == Board.Field.Yellow) {
            TextView txt = (TextView) findViewById(R.id.whichplayer);
            txt.setText("Player: Yellow");
        }
    }
}


