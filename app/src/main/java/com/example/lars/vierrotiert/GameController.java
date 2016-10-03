package com.example.lars.vierrotiert;

/**
 * Created by maus on 02.10.16.
 */
public class GameController {

    private static final int SIZE = 5;
    private Board board;
    private Listener listener;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean gameOver = false;

    GameController(Listener listener) {
        this.listener = listener;
    }

    public void playGame(Player player1, Player player2) {
        this.board = new Board(SIZE);
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;

        while (!gameOver) {
            System.out.println("Next turn: " + currentPlayer.getField());

            nextTurn();

            System.out.println("New board:\n" + board.toString());
        }
    }

    private void nextTurn() {
        Winner winner = board.isWinner();
        if (winner != Winner.None) {
            gameOver = true;
            listener.gameOver(winner);
            return;
        } else {
            switchPlayer();
        }

        if (board.isFull() == true) {
            gameOver = true;
            listener.gameOver(Winner.None);
            return;
        }

        Move move = currentPlayer.set(board);
        move.apply(board);
    }

    private void switchPlayer() {
        if (currentPlayer == player1)
            currentPlayer = player2;
        else
            currentPlayer = player1;

    }

    public interface Listener {

        void gameOver(Winner winner);
    }
}
