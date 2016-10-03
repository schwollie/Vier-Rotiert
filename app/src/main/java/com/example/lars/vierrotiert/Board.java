package com.example.lars.vierrotiert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {

    private final int size;
    private Field[][] field;
    private List<FieldListener> fieldListeners = new ArrayList<>();

    private Board(Field[][] field, int size) {
        this.field = field;
        this.size = size;
    }

    Board(int size) {
        this.size = size;
        this.field = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                field[row][col] = Field.Empty;
            }
        }
    }

    static Board fromCharacters(String[] map) {
        int size = map.length;
        if (size == 0) {
            throw new IllegalArgumentException("map must contain at least one row");
        }

        Field[][] field = new Field[size][size];
        int row = 0;
        for (String line : map) {
            if (line.length() != size) {
                throw new IllegalArgumentException("each row must have " + size + " entries");
            }
            int col = 0;
            for (char c : line.toCharArray()) {
                if (c == Field.Red.character)
                    field[row][col] = Field.Red;
                else if (c == Field.Yellow.character)
                    field[row][col] = Field.Yellow;
                else if (c == Field.Empty.character)
                    field[row][col] = Field.Empty;
                else
                    throw new IllegalArgumentException("Invalid character at " + row + "/" + col);

                col++;
            }
            row++;
        }

        return new

                Board(field, size);
    }

    public Board clone() {
        Field[][] field = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                field[row][col] = this.field[row][col];
            }
        }
        return new Board(field, size);
    }

    public void addFieldLietener(FieldListener listener) {
        fieldListeners.add(listener);
    }

    public int getSize() {
        return size;
    }

    void set(int col, Field field) {

        for (int row = size - 1; row >= 0; row--) {
            if (this.field[row][col] == Field.Empty) {
                this.field[row][col] = field;
                notifyFieldListenersOnStart();
                notifyFieldListenersOnDrop(col, 0, row, field);
                notifyFieldListenersOnEnd();
                return;
            }
        }

        throw new IllegalArgumentException(String.format("Column %d completely full", col));
    }

    boolean isFull() {
        int full = 0;

        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (field[col][row] != Field.Empty) {
                    full++;
                    if (full == size * size) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    Winner isWinner() {
        WinnerAggregator winner = new WinnerAggregator();
        BoardIterator it = new BoardIterator(winner);
        it.iterate(this);
        return winner.winner;
    }

    Field get(int row, int col) {
        return field[row][col];
    }

    void rotateLeft() {
        notifyFieldListenersOnRotateLeft();

        Field[][] newField = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newField[row][col] = field[col][size - 1 - row];
            }
        }

        field = newField;
    }

    void rotateRight() {
        notifyFieldListenersOnRotateRight();

        Field[][] newField = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newField[row][col] = field[size - 1 - col][row];
            }
        }

        field = newField;
    }

    void applyGravity() {
        Field[][] newField = new Field[size][size];

        notifyFieldListenersOnStart();

        for (int col = 0; col < size; col++) {
            int offset = 0;
            for (int row = size - 1; row >= 0; row--) {
                if (field[row][col] == Field.Empty) {
                    newField[row][col] = Field.Empty;
                    offset++;
                } else if (offset > 0) {
                    newField[row + offset][col] = field[row][col];
                    newField[row][col] = Field.Empty;

                    notifyFieldListenersOnDrop(col, row, row + offset, field[row][col]);
                } else {
                    newField[row][col] = field[row][col];
                }
            }
        }

        notifyFieldListenersOnEnd();

        field = newField;
    }

    public boolean isFree(int column) {

        return field[0][column] == Field.Empty;
    }

    private void notifyFieldListenersOnRotateLeft() {
        for (FieldListener listener : fieldListeners) {
            listener.onRotateLeft();
        }
    }

    private void notifyFieldListenersOnRotateRight() {
        for (FieldListener listener : fieldListeners) {
            listener.onRotateRight();
        }
    }

    private void notifyFieldListenersOnStart() {
        for (FieldListener listener : fieldListeners) {
            listener.onStartDrop();
        }
    }

    private void notifyFieldListenersOnDrop(int column, int startRow, int endRow, Field field) {
        for (FieldListener listener : fieldListeners) {
            listener.onDrop(column, startRow, endRow, field);
        }
    }

    private void notifyFieldListenersOnEnd() {
        for (FieldListener listener : fieldListeners) {
            listener.onEndDrop();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                builder.append(field[row][col].character);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    enum Field {
        Empty(0, '-'),
        Red(-1, 'X'),
        Yellow(1, 'O');

        final char character;
        private final int value;

        Field(int value, char c) {
            this.value = value;
            this.character = c;
        }
    }


    interface FieldListener {
        void onRotateLeft();

        void onRotateRight();

        void onStartDrop();

        void onDrop(int col, int startRow, int endRow, Field field);

        void onEndDrop();

    }

    private class WinnerAggregator implements BoardIterator.Listener {

        Winner winner = Winner.None;
        int red = 0;
        int yellow = 0;

        @Override
        public void countField(Field field) {
        }

        @Override
        public void lineFinished(Map<Field, Integer> maxConsecutives) {


            if (maxConsecutives.get(Field.Red) >= 4) {
                red += 1;
            }
            if (maxConsecutives.get(Field.Yellow) >= 4) {
                yellow += 1;
            }


        }

        @Override
        public void boardFinished() {
            if (yellow > red) {
                winner = Winner.Yellow;
            } else if (red > yellow) {
                winner = Winner.Red;
            } else if (red > 0) {
                winner = Winner.Both;
            } else {
                winner = Winner.None;
            }
        }
    }
}
