package com.example.lars.vierrotiert;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created by maus on 07.10.16.
 */
public class HumanPlayer implements Player, VisualBoard.Listener {

    private final Board.Field player;
    private final Board board;
    private final VisualBoard visualBoard;
    private SettableFuture<Move> future;

    HumanPlayer(Board.Field player, Board board, VisualBoard visualBoard) {
        this.player = player;
        this.board = board;
        this.visualBoard = visualBoard;
        visualBoard.addListener(this);
    }

    @Override
    public ListenableFuture<Move> set(Board board) {
        future = SettableFuture.create();
        return future;
    }

    @Override
    public Board.Field getField() {
        return player;
    }

    @Override
    public void onColumnClicked(int col) {
        if (future == null) return;
        if (!board.isFree(col)) return;
        future.set(Move.setColumn(player, col));
        future = null;
    }

    public void onRotateLeft() {
        if (future == null) return;
        future.set(Move.rotateLeft(player));
        future = null;
    }

    public void onRotateRight() {
        if (future == null) return;
        future.set(Move.rotateRight(player));
        future = null;
    }

    @Override
    public void onAnimationFinished() {
    }

    @Override
    public String toString() {
        return "Human as " + player.name();
    }
}
