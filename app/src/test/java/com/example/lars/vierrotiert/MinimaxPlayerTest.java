package com.example.lars.vierrotiert;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class MinimaxPlayerTest {
    @Test
    public void testColumnFullCheck() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "O----",
                "X----",
                "O----",
                "O----",
                "XX---",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);
    }

    @Test
    public void testNoMovesAvailable() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "--------",
                "--------",
                "--------",
                "--------",
                "-------X",
                "-------X",
                "-------X",
                "X--OO--O",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);
    }

    @Test
    public void testAvoidRotation() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "------",
                "------",
                "----X-",
                "----X-",
                "XOXOO-",
                "OXOXO-",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);

        assertThat(board.isWinner(), is(Winner.None));
    }


    @Test
    public void testSetColumnToWin() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "-----",
                "----X",
                "X---X",
                "X---X",
                "OO-OO",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board.toString());

        assertThat(board.isWinner(), is(Winner.Yellow));
    }

    @Test
    public void testRotateLeftToWin() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "----X",
                "----O",
                "----O",
                "----O",
                "OXX-X",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        assertThat(board.isWinner(), is(Winner.Yellow));
    }

    @Test
    public void testSetToAvoidLoss() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "-----",
                "-----",
                "O---O",
                "O---O",
                "XX-XX",
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        assertThat(board.isWinner(), not(is(Winner.Red)));
    }

    @Test
    public void testGame() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "------",
                "------",
                "-----O",
                "---XOX",
                "---OXO",
                "--XXOX"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }

    @Test
    public void testGame2() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "X-----",
                "X-----",
                "X-----",
                "O-----",
                "O-O--O",
                "XXO--O"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }

    @Test
    public void testGame3() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "------",
                "------",
                "------",
                "-XXX--",
                "OOOX--",
                "OXXO-O"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }

    @Test
    public void testGame4() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "------",
                "------",
                "----XX",
                "----XO",
                "---OXO",
                "---XOX"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }

    @Test
    public void testGame5() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "O-----",
                "O-----",
                "XO----",
                "XXX-X-",
                "XOO-X-",
                "OXOXOO"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow);
        Move move = player.set(board).get();
        move.apply(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }

    @Test
    public void testGame6() throws Exception {
        Board board = Board.fromCharacters(new String[]{
                "------",
                "------",
                "------",
                "O-----",
                "XXX---",
                "OXOXXO"
        });

        MinimaxPlayer player = new MinimaxPlayer(Board.Field.Yellow, 3);
        Move move = player.set(board).get();
        move.apply(board);

        Move move2 = Move.setColumn(Board.Field.Red, 3);
        move2.apply(board);

        System.out.println(board);
        assertThat(board.isWinner(), is(Winner.None));
    }
}