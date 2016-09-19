package com.example.lars.vierrotiert;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int size;
    private Field[][] field;
    private List<FieldListener> fieldListeners = new ArrayList<>();
    Board(int size) {
        this.size = size;
        this.field = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                field[row][col] = Field.Empty;
            }
        }
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
                notifyFieldListenersOnDrop(col, 0, row);
                notifyFieldListenersOnEnd();
                return;
            }
        }

        throw new IllegalArgumentException(String.format("Column %d completely full", col));
    }

    Field isWinner() {
        for (int col = 0; col < size; col++) {
            int white = 0;
            int black = 0;
            for (int row = 0; row < size; row++) {
                if (field[col][row] == Field.White) {
                    white++;
                    black = 0;
                } else if (field[col][row] == Field.Black) {
                    black++;
                    white = 0;
                } else if (field[col][row] == Field.Empty) {
                    white = 0;
                    black = 0;
                }

                if (white >= 4) {
                    return Field.White;
                } else if (black >= 4) {
                    return Field.Black;
                }
            }
        }


        //Next


        for (int col = 0; col < size; col++) {
            int white = 0;
            int black = 0;
            for (int row = 0; row < size; row++) {
                if (field[row][col] == Field.White) {
                    white++;
                    black = 0;
                } else if (field[row][col] == Field.Black) {
                    black++;
                    white = 0;
                } else if (field[row][col] == Field.Empty) {
                    white = 0;
                    black = 0;
                }

                if (white >= 4) {
                    return Field.White;

                } else if (black >= 4) {
                    return Field.Black;

                }
            }
        }


        //Diagonale
        int white = 0;
        int black = 0;

        for(int  col = size;col > 0;col--) {
            if(field[col][size]==Field.White) {
                white++;
                black = 0;
            }  else if(field[col][size]==Field.Black) {
                black++;
                white = 0;
            } else  {
                black = 0;
                white = 0;
            }



            for(int i = 0;i < size;i++) {
                if(field[col+i][size-i]==Field.White) {
                    white++;
                    black = 0;
                } else if(field[col+i][size-i]==Field.Black) {
                    black++;
                    white = 0;
                } else {
                    black = 0;
                    white = 0;
                }
            }


            if(white>=4) {
                return Field.White;
            } else if(black>=4) {
                return  Field.Black;
            }


        }

        return Field.Empty;


    }

    Field get(int row, int col) {
        return field[row][col];
    }

    void rotateLeft() {
        Field[][] newField = new Field[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newField[row][col] = field[col][size - 1 - row];
            }
        }

        field = newField;
    }

    void rotateRight() {
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

                    notifyFieldListenersOnDrop(col, row, row + offset);
                }
            }
        }

        notifyFieldListenersOnEnd();

        field = newField;
    }

    public boolean isFree(int column) {

        return field[0][column] == Field.Empty;
    }

    private void notifyFieldListenersOnStart() {
        for (FieldListener listener : fieldListeners) {
            listener.onStartDrop();
        }
    }

    private void notifyFieldListenersOnDrop(int column, int startRow, int endRow) {
        for (FieldListener listener : fieldListeners) {
            listener.onDrop(column, startRow, endRow);
        }
    }

    private void notifyFieldListenersOnEnd() {
        for (FieldListener listener : fieldListeners) {
            listener.onEndDrop();
        }
    }

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

    interface FieldListener {
        void onStartDrop();

        void onDrop(int col, int startRow, int endRow);

        void onEndDrop();
    }
}
