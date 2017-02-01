package com.example.lars.vierrotiert;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created by maus on 02.10.16.
 */
public class GameController implements FutureCallback<Move> {

    private final Board board;
    Player currentPlayer;
    private Listener listener;
    private boolean autoPlay;
    private Player player1;
    private Player player2;
    private SettableFuture<Void> gameOver = SettableFuture.create();
    GameController(Board board, Listener listener, boolean autoPlay) {
        this.board = board;
        this.listener = listener;
        this.autoPlay = autoPlay;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ListenableFuture<Void> playGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player2; // player 2 because nextTurn switches player immediately

        nextTurn();
        return gameOver;
    }

    public void nextTurn() {
        Winner winner = board.isWinner();
        if (winner != Winner.None) {
            try {
                listener.gameOver(winner);
            } finally {
                gameOver.set(null);
            }
            return;
        } else {
            switchPlayer();
        }

        if (board.isFull() == true) {
            try {
                listener.gameOver(Winner.None);
            } finally {
                gameOver.set(null);
            }
            return;
        }

        Futures.addCallback(currentPlayer.set(board), this);

    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            listener.currentPlayerChanged(player2);
        } else {
            currentPlayer = player1;
            listener.currentPlayerChanged(player1);
        }

    }

    @Override
    public void onSuccess(Move move) {
        System.out.println("Player " + currentPlayer + " moves: " + move);

        move.apply(board);
        System.out.println("New board: " + board);

        if (autoPlay)
            nextTurn();
    }

    @Override
    public void onFailure(Throwable t) {
    }

    public interface Listener {

        void currentPlayerChanged(Player player);

        void gameOver(Winner winner);
    }
}
