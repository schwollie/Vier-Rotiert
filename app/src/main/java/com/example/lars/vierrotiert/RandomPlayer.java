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
    public void set(Board board) {
        int whatActionx = rnd.nextInt(board.getSize() + 2);
        switch (whatActionx) {
            case 0:
                board.rotateLeft();
                break;
            case 1:
                board.rotateRight();
                break;
            default:
                //Todo: handle full column
                int column = whatActionx - 2;
                while (true) {
                    if (board.isFree(column)) {
                        board.set(column, getField());
                        break;
                    }

                    column = rnd.nextInt(5);
                }
                break;
        }

    }

    @Override
    public Board.Field getField() {
        return field;
    }
}
