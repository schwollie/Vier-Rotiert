package com.example.lars.vierrotiert;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Random;

/**
 * Created by maus on 04.09.16.
 */
public class RandomPlayer implements Player {

    private final Board.Field field;
    private final Random rnd = new Random();

    RandomPlayer(Board.Field field) {
        this.field = field;
    }


    @Override
    public ListenableFuture<Move> set(Board board) {
        int whatAction = rnd.nextInt(board.size + 2);
        switch (whatAction) {
            case 0:
                return Futures.immediateFuture(Move.rotateLeft(field));
            case 1:
                return Futures.immediateFuture(Move.rotateRight(field));
            default:
                int column = whatAction - 2;
                while (true) {
                    if (board.isFree(column)) {
                        return Futures.immediateFuture(Move.setColumn(field, column));
                    }

                    column = rnd.nextInt(5);
                }
        }
    }

    @Override
    public Board.Field getField() {
        return field;
    }
}
