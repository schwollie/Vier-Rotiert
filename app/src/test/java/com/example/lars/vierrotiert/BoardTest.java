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
        testBoard.set(0, Board.Field.Red);
        testBoard.set(1, Board.Field.Yellow);
        testBoard.set(2, Board.Field.Red);
        testBoard.set(2, Board.Field.Yellow);

        testBoard.rotateLeft();
        testBoard.applyGravity();

        assertEquals(testBoard.get(4, 3), Board.Field.Yellow);
        assertEquals(testBoard.get(4, 4), Board.Field.Red);
        assertEquals(testBoard.get(3, 4), Board.Field.Yellow);
        assertEquals(testBoard.get(2, 4), Board.Field.Red);
    }

    @Test
    public void testRotateRight() throws Exception {
        Board testBoard = new Board(5);
        testBoard.set(0, Board.Field.Red);
        testBoard.set(1, Board.Field.Yellow);
        testBoard.set(2, Board.Field.Red);
        testBoard.set(2, Board.Field.Yellow);

        testBoard.rotateRight();
        testBoard.applyGravity();

        assertEquals(testBoard.get(4, 0), Board.Field.Red);
        assertEquals(testBoard.get(4, 1), Board.Field.Yellow);
        assertEquals(testBoard.get(3, 0), Board.Field.Yellow);
        assertEquals(testBoard.get(2, 0), Board.Field.Red);
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

        assertEquals(testBoard.isWinner(), Winner.Red);
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

        assertEquals(testBoard.isWinner(), Winner.Yellow);
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

        assertEquals(testBoard.isWinner(), Winner.Yellow);
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

        assertEquals(testBoard.isWinner(), Winner.Yellow);
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

        assertEquals(testBoard.isWinner(), Winner.Yellow);
    }

    @Test
    public void testIsWinnerRowCheck() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "--------",
                "--------",
                "----X---",
                "-----X--",
                "------X-",
                "-------X",
                "----OOOO",
                "XXXXOOOO"

        });

        assertEquals(testBoard.isWinner(), Winner.Both);

        testBoard = Board.fromCharacters(new String[]{
                "-OOOO",
                "-----",
                "-----",
                "-----",
                "-----",
        });

        assertEquals(testBoard.isWinner(), Winner.Yellow);
    }


    @Test
    public void testIsFull() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "XXXXX",
                "XXXXX",
                "XXXXX",
                "XXXXX",
                "XXXXX",
        });

        assertEquals(testBoard.isFull(), true);
    }

}