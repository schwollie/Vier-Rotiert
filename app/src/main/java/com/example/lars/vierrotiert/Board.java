package com.example.lars.vierrotiert;

import java.util.ArrayList;
import java.util.List;

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
                switch (c) {
                    case 'X':
                        field[row][col] = Field.Red;
                        break;
                    case 'O':
                        field[row][col] = Field.Yellow;
                        break;
                    case '-':
                        field[row][col] = Field.Empty;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid character at " + row + "/" + col);
                }
                col++;
            }
            row++;
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

        for(int col = 0;col < size;col++) {
            for(int row = 0;row < size;row++) {
                if(field[col][row]!= Field.Empty) {
                    full++;
                    if(full==size*size) {
                        return true;
                    }

                }
            }
        }


        return false;
    }

    Field isWinner() {
        Accumulator acc = new Accumulator();
        for (int row = 0; row < size; row++) {
            acc.reset();
            for (int col = 0; col < size; col++) {
                acc.addField(field[row][col]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }

        //Next
        for (int col = 0; col < size; col++) {
            acc.reset();
            for (int row = 0; row < size; row++) {
                acc.addField(field[row][col]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }

        // Fall 1: Spalten von oben nach unten links
        for (int col = 0; col < size; col++) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(field[i][col - i]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }
        // Fall 2: Spalten von unten nach oben rechts
        for (int col = size - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i < size - col; i++) {
                acc.addField(field[size - i - 1][col + i]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }

        // Fall 3: Spalten von oben nach unten rechts
        for (int col = 0; col < size; col++) {
            acc.reset();

            for (int i = 0; i < size - col; i++) {
                acc.addField(field[i][col + i]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }
        // Fall 4: Spalten von unten nach oben links
        for (int col = size - 1; col >= 0; col--) {
            acc.reset();

            for (int i = 0; i <= col; i++) {
                acc.addField(field[size - i - 1][col - i]);
                if (acc.hasWinner()) {
                    return acc.getWinner();
                }
            }
        }


        return Field.Empty;
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

    enum Field {
        Empty(0, ' '),
        Red(-1, '0'),
        Yellow(1, 'X');

        private final int value;
        private final char character;

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

    private class Accumulator {
        int red = 0;
        int yellow = 0;

        void addField(Field field) {
            if (field == Field.Yellow) {
                yellow++;
                red = 0;
            } else if (field == Field.Red) {
                red++;
                yellow = 0;
            } else if (field == Field.Empty) {
                yellow = 0;
                red = 0;
            }
        }

        void reset() {
            red = 0;
            yellow = 0;
        }

        boolean hasWinner() {
            return red >= 4 || yellow >= 4;
        }

        Field getWinner() {
            if (red >= 4) return Field.Red;
            if (yellow >= 4) return Field.Yellow;
            return Field.Empty;
        }
    }
}
