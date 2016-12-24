package com.example.lars.vierrotiert;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

        assertThat(testBoard.field[4][3], is(Board.Field.Yellow));
        assertThat(testBoard.field[4][4], is(Board.Field.Red));
        assertThat(testBoard.field[3][4], is(Board.Field.Yellow));
        assertThat(testBoard.field[2][4], is(Board.Field.Red));
    }

    @Test
    public void testRotateRight() throws Exception {
        Board testBoard = new Board(5);
        testBoard.set(0, Board.Field.Red);
        testBoard.set(1, Board.Field.Yellow);
        testBoard.set(2, Board.Field.Red);
        testBoard.set(2, Board.Field.Yellow);

        testBoard.rotateRight();

        assertThat(testBoard.field[4][0], is(Board.Field.Red));
        assertThat(testBoard.field[4][1], is(Board.Field.Yellow));
        assertThat(testBoard.field[3][0], is(Board.Field.Yellow));
        assertThat(testBoard.field[2][0], is(Board.Field.Red));
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

        assertThat(testBoard.isWinner(), is(Winner.Red));
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

        assertThat(testBoard.isWinner(), is(Winner.Yellow));
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

        assertThat(testBoard.isWinner(), is(Winner.Yellow));
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

        assertThat(testBoard.isWinner(), is(Winner.Yellow));
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

        assertThat(testBoard.isWinner(), is(Winner.Yellow));
    }

    @Test
    public void testIsWinnerHandlesDrawCheck() throws Exception {
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
        assertThat(testBoard.isWinner(), is(Winner.Both));
    }

    @Test
    public void testIsWinnerRegressionTest() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "------",
                "------",
                "X--X--",
                "OO-X--",
                "XXOO--",
                "OOXO--",

        });
        assertThat(testBoard.isWinner(), is(Winner.None));
    }

    @Test
    public void testIsWinnerRowCheck() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "-OOOO",
                "-----",
                "-----",
                "-----",
                "-----",
        });

        assertThat(testBoard.isWinner(), is(Winner.Yellow));
    }

    @Test
    public void testIsWinnerColCheck() throws Exception {
        Board testBoard = Board.fromCharacters(new String[]{
                "X----",
                "X----",
                "X----",
                "X----",
                "-----",
        });

        assertThat(testBoard.isWinner(), is(Winner.Red));
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

        assertThat(testBoard.isFull(), is(true));
    }

    public void testScore() {
        Board testBoard = Board.fromCharacters(new String [] {
                "------",
                        "O-----",
                        "O-----",
                        "OX----",
                "XXX---",
                        "OXOXXO",
        });
    }
}