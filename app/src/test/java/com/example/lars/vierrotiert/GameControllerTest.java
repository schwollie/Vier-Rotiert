package com.example.lars.vierrotiert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class GameControllerTest {
    private static final int RUN_ITERATIONS = 10;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[RUN_ITERATIONS][0]);
    }

    @Test
    public void testRandomVsMinimaxGame() throws Exception {
        Board board = new Board(6);
        GameController controller = new GameController(board, new GameController.Listener() {
            @Override
            public void currentPlayerChanged(Player player) {

            }

            @Override
            public void gameOver(Winner winner) {
                System.out.println("The winner is " + winner);
                assertThat(winner, is(Winner.Yellow));
            }
        }, true);

        Player player1 = new RandomPlayer(Board.Field.Red);
        Player player2 = new MinimaxPlayer(Board.Field.Yellow);
        controller.playGame(player1, player2).get();
    }

    @Test
    public void testMinimaxGame() throws Exception {
        Board board = new Board(6);
        GameController controller = new GameController(board, new GameController.Listener() {
            @Override
            public void currentPlayerChanged(Player player) {

            }

            @Override
            public void gameOver(Winner winner) {
                System.out.println("The winner is " + winner);
                assertThat(winner, not(is(Winner.Red)));
            }
        }, true);

        Player player1 = new MinimaxPlayer(Board.Field.Red, 3);
        Player player2 = new MinimaxPlayer(Board.Field.Yellow, 6);
        controller.playGame(player1, player2).get();
    }
}