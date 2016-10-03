package com.example.lars.vierrotiert;

/**
 * Created by maus on 04.09.16.
 */
public interface Player {

    Move set(Board board);

    Board.Field getField();
}
