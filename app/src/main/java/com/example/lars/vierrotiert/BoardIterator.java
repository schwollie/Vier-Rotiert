package com.example.lars.vierrotiert;

public class BoardIterator {
    private BoardIteratorListener listener;

    BoardIterator(BoardIteratorListener listener) {
        this.listener = listener;
    }

    void iterate(Board board) {
        Accumulator acc = new Accumulator();
        for (int row = 0; row < board.size; row++) {
            acc.reset();
            for (int col = 0; col < board.size; col++) {
                acc.addField(board.field[row][col]);
                listener.countField(board.field[row][col]);
            }
            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }

        //Next
        for (int col = 0; col < board.size; col++) {
            acc.reset();
            for (int row = 0; row < board.size; row++) {
                acc.addField(board.field[row][col]);
            }
            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }

        // Fall 1: Spalten von oben nach unten links
        for (int col = 0; col < board.size; col++) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(board.field[i][col - i]);
            }

            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }
        // Fall 2: Spalten von unten nach oben rechts
        for (int col = board.size - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i < board.size - col; i++) {
                acc.addField(board.field[board.size - i - 1][col + i]);
            }

            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }

        // Fall 3: Spalten von oben nach unten rechts
        for (int col = 0; col < board.size; col++) {
            acc.reset();

            for (int i = 0; i < board.size - col; i++) {
                acc.addField(board.field[i][col + i]);
            }

            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }
        // Fall 4: Spalten von unten nach oben links
        for (int col = board.size - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(board.field[board.size - i - 1][col - i]);
            }

            listener.lineFinished(acc.redConsecutives, acc.yellowConsecutives);
        }

        listener.boardFinished();
    }

}
