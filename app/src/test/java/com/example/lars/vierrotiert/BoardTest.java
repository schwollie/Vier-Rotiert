package com.example.lars.vierrotiert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Lars on 03.09.2016.
 */
public class BoardTest {

    @Test
    public void testRotateLeft() throws Exception {
        Board testBoard = new Board(5);
        testBoard.set(0, Board.Field.Black);
        testBoard.set(1, Board.Field.White);
        testBoard.set(2, Board.Field.Black);
        testBoard.set(2, Board.Field.White);

        testBoard.rotateLeft();
        testBoard.applyGravity();

        assertEquals(testBoard.get(4, 3), Board.Field.White);
        assertEquals(testBoard.get(4, 4), Board.Field.Black);
        assertEquals(testBoard.get(3, 4), Board.Field.White);
        assertEquals(testBoard.get(2, 4), Board.Field.Black);
    }

    @Test
    public void testRotateRight() throws Exception {
        Board testBoard = new Board(5);
        testBoard.set(0, Board.Field.Black);
        testBoard.set(1, Board.Field.White);
        testBoard.set(2, Board.Field.Black);
        testBoard.set(2, Board.Field.White);

        testBoard.rotateRight();
        testBoard.applyGravity();

        assertEquals(testBoard.get(4, 0), Board.Field.Black);
        assertEquals(testBoard.get(4, 1), Board.Field.White);
        assertEquals(testBoard.get(3, 0), Board.Field.White);
        assertEquals(testBoard.get(2, 0), Board.Field.Black);
    }

    @Test
    public void testIsWinner1() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-----",
                "--OX-",
                "-OXOO",
                "-XOXO",
                "XOXOX",
        });

        assertEquals(testBoard.isWinner(), Board.Field.Black);
    }

    @Test
    public void testIsWinnerDiagonalCase3() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-O---",
                "--O--",
                "---O-",
                "----O",
                "-----",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);
    }

    @Test
    public void testIsWinnerDiagonalCase2() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-----",
                "----O",
                "---O-",
                "--O--",
                "-O---",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);
    }

    @Test
    public void testIsWinnerDiagonalCase4() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-----",
                "O----",
                "-O---",
                "--O--",
                "---O-",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);
    }

    @Test
    public void testIsWinnerDiagonalCase1() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "---O-",
                "--O--",
                "-O---",
                "O----",
                "-----",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);
    }

    @Test
    public void testIsWinnerRowCheck() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-----",
                "-----",
                "-----",
                "-----",
                "OOOO-",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);

        testBoard = Board.fromCharacters(new String[]{
                "-OOOO",
                "-----",
                "-----",
                "-----",
                "-----",
        });

        assertEquals(testBoard.isWinner(), Board.Field.White);
    }
}