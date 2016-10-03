package com.example.lars.vierrotiert;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by maus on 02.10.16.
 */
public class GameControllerTest {


    @Test
    public void testGame() {
        GameController controller = new GameController(new GameController.Listener() {
            @Override
            public void gameOver(Board.Field winner) {
                System.out.println("The winner is " + winner);
                assertThat(winner, is(Board.Field.Yellow));
            }
        });

        Player player1 = new RandomPlayer(Board.Field.Red);
        Player player2 = new MinimaxPlayer(Board.Field.Yellow);
        controller.playGame(player1, player2);
    }
}