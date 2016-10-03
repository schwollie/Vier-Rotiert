package com.example.lars.vierrotiert;

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
    public Move set(Board board) {
        int whatActionx = rnd.nextInt(board.getSize() + 2);
        switch (whatActionx) {
            case 0:
                return Move.rotateLeft(field);
            case 1:
                return Move.rotateRight(field);
            default:
                //Todo: handle full column
                int column = whatActionx - 2;
                while (true) {
                    if (board.isFree(column)) {
                        return Move.setColumn(field, column);
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
