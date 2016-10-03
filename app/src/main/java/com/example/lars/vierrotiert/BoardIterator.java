package com.example.lars.vierrotiert;

import java.util.HashMap;
import java.util.Map;

public class BoardIterator {

    private final Listener listener;

    BoardIterator(Listener listener) {
        this.listener = listener;
    }

    void iterate(Board board) {

        Accumulator acc = new Accumulator();
        for (int row = 0; row < board.getSize(); row++) {
            acc.reset();
            for (int col = 0; col < board.getSize(); col++) {
                acc.addField(board.get(row, col));
                listener.countField(board.get(row, col));
            }
            listener.lineFinished(acc.getMaxConsecutives());
        }

        //Next
        for (int col = 0; col < board.getSize(); col++) {
            acc.reset();
            for (int row = 0; row < board.getSize(); row++) {
                acc.addField(board.get(row, col));
            }
            listener.lineFinished(acc.getMaxConsecutives());
        }

        // Fall 1: Spalten von oben nach unten links
        for (int col = 0; col < board.getSize(); col++) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(board.get(i, col - i));
            }

            listener.lineFinished(acc.getMaxConsecutives());
        }
        // Fall 2: Spalten von unten nach oben rechts
        for (int col = board.getSize() - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i < board.getSize() - col; i++) {
                acc.addField(board.get(board.getSize() - i - 1, col + i));
            }

            listener.lineFinished(acc.getMaxConsecutives());
        }

        // Fall 3: Spalten von oben nach unten rechts
        for (int col = 0; col < board.getSize(); col++) {
            acc.reset();

            for (int i = 0; i < board.getSize() - col; i++) {
                acc.addField(board.get(i, col + i));
            }

            listener.lineFinished(acc.getMaxConsecutives());
        }
        // Fall 4: Spalten von unten nach oben links
        for (int col = board.getSize() - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(board.get(board.getSize() - i - 1, col - i));
            }

            listener.lineFinished(acc.getMaxConsecutives());
        }

        listener.boardFinished();
    }

    public interface Listener {
        void countField(Board.Field field);

        void lineFinished(Map<Board.Field, Integer> maxConsecutives);

        void boardFinished();
    }

    private class Accumulator {
        int red = 0;
        int yellow = 0;
        Map<Board.Field, Integer> maxConsecutives = new HashMap<>();

        void addField(Board.Field field) {
            if (field == Board.Field.Yellow) {
                yellow++;
                red = 0;
                maxConsecutives.put(Board.Field.Yellow, Math.max(maxConsecutives.get(Board.Field.Yellow), yellow));
            } else if (field == Board.Field.Red) {
                red++;
                yellow = 0;
                maxConsecutives.put(Board.Field.Red, Math.max(maxConsecutives.get(Board.Field.Red), red));
            } else if (field == Board.Field.Empty) {
                yellow = 0;
                red = 0;
            }
        }

        void reset() {
            red = 0;
            yellow = 0;
            maxConsecutives.clear();
            maxConsecutives.put(Board.Field.Red, 0);
            maxConsecutives.put(Board.Field.Yellow, 0);
        }

        public Map<Board.Field, Integer> getMaxConsecutives() {
            return maxConsecutives;
        }
    }

}
