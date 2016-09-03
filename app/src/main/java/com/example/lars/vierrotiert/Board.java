package com.example.lars.vierrotiert;

/**
 * Created by Lars on 03.09.2016.
 */
public class Board {

    private final int size;
    private Field[][] field;

    enum Field {
        Empty(0, ' '),
        Black(-1, '0'),
        White(1, 'X');

        private final int value;
        private final char character;

        Field(int value, char c) {
            this.value = value;
            this.character = c;
        }
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

    private Board(Field[][] field, int size) {
        this.size= size;
        this.field = field;
    }

    Board set(int col, Field field) {
        Field[][] copy = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                copy[row][column] = this.field[row][column];
            }
        }

        for (int row = size-1; row>=0; row--) {
            if (copy[row][col] == Field.Empty) {
                copy[row][col] = field;
                return new Board(copy, size);
            }
        }

        throw new IllegalArgumentException(String.format("Column %d completely full", col));
    }

    Field get(int row, int col) {
        return field[row][col];
    }

    Board rotateLeft() {
        Field[][] newField = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newField[row][col] = field[col][size - 1 - row];
            }
        }

        applyGravity(newField);

        return new Board(newField, size);
    }

    Board rotateRight() {
        Field[][] newField = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newField[row][col] = field[size - 1 - col][row];
            }
        }

        applyGravity(newField);

        return new Board(newField, size);
    }

    private void applyGravity(Field[][] newField) {
        for (int col = 0; col < size; col++) {
            int offset = 0;
            for (int row = size - 1; row >= 0; row--) {
                if (newField[row][col] == Field.Empty) {
                    offset++;
                } else if (offset > 0) {
                    newField[row + offset][col] = newField[row][col];
                    newField[row][col] = Field.Empty;
                }
            }
        }
    }
}
