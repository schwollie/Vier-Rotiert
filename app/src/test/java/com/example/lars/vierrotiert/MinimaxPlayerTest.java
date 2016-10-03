package com.example.lars.vierrotiert;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class MinimaxPlayerTest {
    @Test
    public void testColumnFullCheck() {
        Board board = Board.fromCharacters(new String[]{
                "O----",
                "X----",
                "O----",
                "O----",
                "XX---",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board);
        move.apply(board);
    }

    @Test
    public void testSetColumnToWin() {
        Board board = Board.fromCharacters(new String[]{
                "-----",
                "----X",
                "X---X",
                "X---X",
                "OO-OO",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board);
        move.apply(board);

        assertThat(board.isWinner(), is(Board.Field.Yellow));
    }

    @Test
    public void testRotateLeftToWin() {
        Board board = Board.fromCharacters(new String[]{
                "----X",
                "----O",
                "----O",
                "----O",
                "OXX-X",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board);
        move.apply(board);

        assertThat(board.isWinner(), is(Board.Field.Yellow));
    }

    @Test
    public void testSetToAvoidLoss() {
        Board board = Board.fromCharacters(new String[]{
                "-----",
                "-----",
                "O---O",
                "O---O",
                "XX-XX",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board);
        move.apply(board);

        assertThat(board.isWinner(), not(is(Board.Field.Red)));
    }
}