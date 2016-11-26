package com.example.lars.vierrotiert;

import android.os.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created by maus on 29.09.16.
 */
public class MinimaxPlayer implements Player {
    private Board.Field player;
    private int lookAhead;

    private final static int DEFAULT_LOOKAHEAD = 5;

    public MinimaxPlayer(Board.Field player) {
        this(player, DEFAULT_LOOKAHEAD);
    }

    public MinimaxPlayer(Board.Field player, int lookAhead) {
        this.player = player;
        this.lookAhead = lookAhead;
    }

    @Override
    public Board.Field getField() {
        return player;
    }

    @Override
    public ListenableFuture<Move> set(Board board) {
        System.out.println("Player " + player + " sees " + board);
        return distributedMinimax(board, player);
    }

    private ListenableFuture<Move> distributedMinimax(Board board, Board.Field player) {
        final List<MinimaxMove> moves = createPossibleMoves(board, player);

        List<MinimaxWalker> walkers = new ArrayList<>();
        List<ListenableFuture<MinimaxMove>> futures = new ArrayList<>();
        for (final MinimaxMove move : moves) {
//            walkers.add(new MinimaxWalker(move, player));
            futures.add(Futures.immediateFuture(new MinimaxWalker(move, player).call()));
        }

//        final ExecutorService executor = Executors.newFixedThreadPool(8);
//        for (MinimaxWalker walker : walkers) {
//            futures.add(JdkFutureAdapters.listenInPoolThread(executor.submit(walker)));
//        }

        return Futures.transform(Futures.successfulAsList(futures), new Function<List<MinimaxMove>, Move>() {
            @Override
            public Move apply(List<MinimaxMove> futures) {
                try {
                    System.out.println("Possible moves " + futures);
                    return selectBestMove(futures).move;
                } finally {
                    //executor.shutdown();
                }
            }
        });
    }

    private class MinimaxWalker implements Callable<MinimaxMove> {

        private final MinimaxMove move;
        private final Board.Field player;
        private BoardAccumulators accumulators = null;
        private MinimaxMove bestMove;

        MinimaxWalker(MinimaxMove move, Board.Field player) {
            this.move = move;
            this.player = player;
        }

        @Override
        public MinimaxMove call() {
            Board startBoard = move.board;

            Winner winner = startBoard.isWinner();
            switch (winner) {
                case None:
                    break;
                case Red:
                case Yellow:
                    if (winner.isPlayer(player)) {
                        System.out.println("Found winner move " + player + ":" + move.board);
                        move.score = 1.;
                        return move;
                    }
                    move.score = -1.; // will be converted to -1 below
                    System.out.println("Found defeat move " + player + ":" + move.board);
                    return move;
                case Both:
                    move.score = 0.0;
                    return move;
            }

            this.accumulators = new BoardAccumulators(move.board);

            MinimaxMove bestMoveForOpponent = selectMinimax(move.board, getOpponent(player), 1);
            move.score = -1 * bestMoveForOpponent.score;

            System.out.println("Best possible move " + move + " yields " + move.board);

            return move;
        }

        private MinimaxMove selectMinimax(Board board, Board.Field player, int depth) {
            List<MinimaxMove> moves = createPossibleMoves(board, player);
            if (depth >= lookAhead) {
                calculateScores(moves, player);
                return selectBestMove(moves);
            }

            Board.Field opponent = getOpponent(player);
            for (MinimaxMove move : moves) {
                Winner winner = move.board.isWinner();
                if (winner == Winner.Both) {
                    move.score = 0.;
                    continue;
                } else if (winner.isPlayer(player)) {
                    System.out.println("Found winner move " + depth + " " + player + ":" + move.board);
                    move.score = 1.;
                    return move;
                } else if (winner.isPlayer(opponent)) {
                    System.out.println("Found defeat move " + depth + " " + player + ":" + move.board);
                    move.score = -1.;
                    continue;
                }

                MinimaxMove bestMove = selectMinimax(move.board, opponent, depth + 1);
                move.score = bestMove.score * -1;
            }

            MinimaxMove bestMove = selectBestMove(moves);
            System.out.println("Best move depth " + depth + " " + player + ":" + bestMove.board + " with score " + bestMove.score);
            return bestMove;
        }

        private void calculateScores(List<MinimaxMove> moves, Board.Field player) {
            for (MinimaxMove move : moves) {
                move.score = calculateScore(move.board, player);
            }
        }

        private double calculateScore(Board board, Board.Field player) {
            Counter counter = new Counter();
            accumulators.run(board, counter);

            switch (counter.winner) {
                case None:
                    break;
                case Red:
                    return player == Board.Field.Red ? 1 : -1;
                case Yellow:
                    return player == Board.Field.Yellow ? 1 : -1;
                case Both:
                    return 0;
            }

            Board.Field opponent = getOpponent(player);
            if (!Objects.equals(counter.lineWinner.get(player), counter.lineWinner.get(opponent))) {
                return (double) counter.lineWinner.get(player) / counter.lineCount;
            }

            return (double) counter.fieldCount.get(player) / (counter.fieldCount.get(player) + counter.fieldCount.get(opponent));
        }
    }


    private static List<MinimaxMove> createPossibleMoves(Board board, Board.Field player) {
        List<MinimaxMove> moves = new ArrayList<>();

        for (int i = 0; i < board.size; i++) {
            if (!board.isFree(i)) continue;

            MinimaxMove setMove = new MinimaxMove();
            setMove.move = Move.setColumn(player, i);
            setMove.board = board.clone();
            setMove.move.apply(setMove.board);
            moves.add(setMove);
        }

        MinimaxMove leftRotation = new MinimaxMove();
        leftRotation.move = Move.rotateLeft(player);
        leftRotation.board = board.clone();
        leftRotation.move.apply(leftRotation.board);
        moves.add(leftRotation);

        MinimaxMove rightRotation = new MinimaxMove();
        rightRotation.move = Move.rotateRight(player);
        rightRotation.board = board.clone();
        rightRotation.move.apply(rightRotation.board);
        moves.add(rightRotation);

        //Collections.shuffle(moves);
        return moves;
    }

    private static MinimaxMove selectBestMove(Iterable<MinimaxMove> moves) {
        MinimaxMove bestMove = null;
        Double maxScore = null;
        for (MinimaxMove move : moves) {
            if (bestMove == null || move.score > maxScore) {
                maxScore = move.score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static Board.Field getOpponent(Board.Field player) {
        return player == Board.Field.Red ? Board.Field.Yellow : Board.Field.Red;
    }

    static class MinimaxMove {
        Board board;
        Move move;
        Double score;

        @Override
        public String toString() {
            return "Move " + move + " has score " + score;
        }
    }

    private class Counter implements BoardIteratorListener {
        Winner winner = Winner.None;
        FieldValue lineWinner = new FieldValue();
        FieldValue fieldCount = new FieldValue();
        int lineCount;
        int redWinner = 0;
        int yellowWinner = 0;

        @Override
        public void countField(Board.Field field) {
            if (field != Board.Field.Empty) {
                fieldCount.set(field, fieldCount.get(field) + 1);
            }
        }

        @Override
        public void lineFinished(int redConsecutives, int yellowConsecutives) {
            if (redConsecutives > yellowConsecutives) {
                lineWinner.set(Board.Field.Red, lineWinner.get(Board.Field.Red) + 1);
                lineCount++;
            }
            if (yellowConsecutives > redConsecutives) {
                lineWinner.set(Board.Field.Yellow, lineWinner.get(Board.Field.Yellow) + 1);
                lineCount++;
            }
            if (redConsecutives >= 4) {
                redWinner += 1;
            }
            if (yellowConsecutives >= 4) {
                yellowWinner += 1;
            }
        }

        @Override
        public void boardFinished() {
            if (yellowWinner > redWinner) {
                winner = Winner.Yellow;
            } else if (redWinner > yellowWinner) {
                winner = Winner.Red;
            } else if (redWinner > 0) {
                winner = Winner.Both;
            } else {
                winner = Winner.None;
            }
        }
    }


}
