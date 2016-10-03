package com.example.lars.vierrotiert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maus on 29.09.16.
 */
public class MinimaxPlayer implements Player {

    private static final int MAX_DEPTH = 4;

    private Board.Field player;
    private boolean debug = false;

    public MinimaxPlayer(Board.Field player) {
        this.player = player;
    }

    @Override
    public Board.Field getField() {
        return player;
    }

    @Override
    public Move set(Board board) {
        System.out.println("Find move for board\n" + board);
        MinimaxMove bestMove = selectMinimax(board, player, 0);
        return bestMove.move;
    }

    private MinimaxMove selectMinimax(Board board, Board.Field player, int depth) {
        List<MinimaxMove> moves = createPossibleMoves(board, player);
        if (depth == MAX_DEPTH) {
            calculateScores(moves, player);
            return selectBestMove(moves);
        }

        Board.Field opponent = getOpponent(player);
        Map<MinimaxMove, MinimaxMove> minimaxMoves = new HashMap<>();
        for (MinimaxMove move : moves) {
            if (move.board.isWinner().isPlayer(player)) {
                move.score = 1.;
                return move;
            }

            MinimaxMove bestMove = selectMinimax(move.board, opponent, depth + 1);
            bestMove.score *= -1;
            move.score = bestMove.score;
            minimaxMoves.put(bestMove, move);
        }

        return minimaxMoves.get(selectBestMove(minimaxMoves.keySet()));
    }

    private Board.Field getOpponent(Board.Field player) {
        return player == Board.Field.Red ? Board.Field.Yellow : Board.Field.Red;
    }

    private MinimaxMove selectBestMove(Iterable<MinimaxMove> moves) {
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

    private void calculateScores(List<MinimaxMove> moves, Board.Field player) {
        for (MinimaxMove move : moves) {
            move.score = calculateScore(move.board, player);
            if (debug) System.out.println(move);
        }
    }

    private double calculateScore(Board board, Board.Field player) {
        Winner winner = board.isWinner();
        switch (winner) {
            case None:
                break;
            case Red:
                return player == Board.Field.Red ? 1 : -1;
            case Yellow:
                return player == Board.Field.Yellow ? 1 : -1;
            case Both:
                return 0;
        }

        Counter counter = new Counter();
        BoardIterator it = new BoardIterator(counter);
        it.iterate(board);

        Board.Field opponent = getOpponent(player);
        if (counter.lineWinner.get(player) != counter.lineWinner.get(opponent)) {
            return (double) counter.lineWinner.get(player) / counter.lineCount;
        }

        return (double) counter.fieldCount.get(player) / (counter.fieldCount.get(player) + counter.fieldCount.get(opponent));
    }

    private List<MinimaxMove> createPossibleMoves(Board board, Board.Field player) {
        List<MinimaxMove> moves = new ArrayList<>();

        MinimaxMove leftRotation = new MinimaxMove();
        leftRotation.move = Move.rotateLeft(player);
        leftRotation.board = board.clone();
        leftRotation.move.apply(leftRotation.board);
        moves.add(leftRotation);

        MinimaxMove rightRotation = new MinimaxMove();
        rightRotation.move = Move.rotateRight(player);
        rightRotation.board = board.clone();
        rightRotation.move.apply(leftRotation.board);
        moves.add(rightRotation);

        for (int i = 0; i < board.getSize(); i++) {
            if (!board.isFree(i)) continue;

            MinimaxMove setMove = new MinimaxMove();
            setMove.move = Move.setColumn(player, i);
            setMove.board = board.clone();
            setMove.move.apply(setMove.board);
            moves.add(setMove);
        }

        Collections.shuffle(moves);
        return moves;
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

    private class Counter implements BoardIterator.Listener {
        Map<Board.Field, Integer> lineWinner = new HashMap<>();
        Map<Board.Field, Integer> fieldCount = new HashMap<>();
        int lineCount;

        Counter() {
            lineWinner.put(Board.Field.Red, 0);
            lineWinner.put(Board.Field.Yellow, 0);
            fieldCount.put(Board.Field.Red, 0);
            fieldCount.put(Board.Field.Yellow, 0);
        }

        @Override
        public void countField(Board.Field field) {
            if (field != Board.Field.Empty) {
                fieldCount.put(field, fieldCount.get(field) + 1);
            }
        }

        @Override
        public void lineFinished(Map<Board.Field, Integer> maxConsecutives) {
            int red = maxConsecutives.get(Board.Field.Red);
            int yellow = maxConsecutives.get(Board.Field.Yellow);
            if (red > yellow) {
                lineWinner.put(Board.Field.Red, lineWinner.get(Board.Field.Red) + 1);
                lineCount++;
            }
            if (yellow > red) {
                lineWinner.put(Board.Field.Yellow, lineWinner.get(Board.Field.Yellow) + 1);
                lineCount++;
            }
        }

        @Override
        public void boardFinished() {
        }
    }
}
