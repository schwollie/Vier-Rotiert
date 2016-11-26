package com.example.lars.vierrotiert;

/**
 * Created by maus on 03.10.16.
 */
public interface BoardIteratorListener {
    void countField(Board.Field field);

    void lineFinished(int redConsecutives, int yellowConsecutives);

    void boardFinished();
}
