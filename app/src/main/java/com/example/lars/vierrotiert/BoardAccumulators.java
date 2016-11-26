package com.example.lars.vierrotiert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maus on 03.10.16.
 */
public class BoardAccumulators {
    private Accumulator[] accumulators;
    private int[][] fieldPosToAccIndexes;

    BoardAccumulators(Board board) {
        int size = board.size;

        // We have 2*size accumulators for all rows and columns, and for the 2 types of diagonals
        // we need 1 for the main axes and (size - 4) on each side, which is 2*(2*(size-4)+1).
        int totalAccCount = 6 * size - 14;
        accumulators = new Accumulator[totalAccCount];
        for (int i = 0; i < totalAccCount; i++) {
            accumulators[i] = new Accumulator();
        }

        fieldPosToAccIndexes = new int[size * size][];
        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++, index++) {
                // We have at least two accumulators per field: one per row and one per column;
                List<Integer> indexes = new ArrayList<>();
                indexes.add(row);
                indexes.add(size + col);

                int base = size * 2;
                int maxDelta = size - 4;
                int delta = row - col;
                if (Math.abs(delta) <= maxDelta) {
                    indexes.add(base + maxDelta + delta);
                }
                delta = (size - 1) - (row + col);
                if (Math.abs(delta) <= maxDelta) {
                    indexes.add(base + 3 * maxDelta + delta + 1);
                }
                fieldPosToAccIndexes[index] = new int[indexes.size()];
                for (int i = 0; i < indexes.size(); i++)
                    fieldPosToAccIndexes[index][i] = indexes.get(i);
            }
        }
    }

    void run(Board board, BoardIteratorListener listener) {
        for (int i = 0; i < accumulators.length; i++) {
            accumulators[i].reset();
        }

        int size = board.size;
        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++, index++) {
                listener.countField(board.field[row][col]);
                for (int accumulatorIndex = 0; accumulatorIndex < fieldPosToAccIndexes[index].length; accumulatorIndex++) {
                    accumulators[fieldPosToAccIndexes[index][accumulatorIndex]].addField(board.field[row][col]);
                }
            }
        }

        for (int i = 0; i < accumulators.length; i++) {
            listener.lineFinished(accumulators[i].redConsecutives, accumulators[i].yellowConsecutives);
        }

        listener.boardFinished();
    }
}
