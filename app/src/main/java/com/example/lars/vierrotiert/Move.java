package com.example.lars.vierrotiert;

/**
 * Created by maus on 02.10.16.
 */
public class Move {
    Board.Field player;
    Type type;
    int column = 0;

    private Move(Board.Field player, Type type, int column) {
        this.player = player;
        this.type = type;
        this.column = column;
    }

    static Move rotateLeft(Board.Field player) {
        return new Move(player, Type.RotateLeft, 0);
    }

    static Move rotateRight(Board.Field player) {
        return new Move(player, Type.RotateRight, 0);
    }

    static Move setColumn(Board.Field player, int column) {
        return new Move(player, Type.SetColumn, column);
    }

    void apply(Board board) {
        switch (type) {
            case RotateLeft:
                board.rotateLeft();
                break;
            case RotateRight:
                board.rotateRight();
                break;
            case SetColumn:
                board.set(column, player);
                break;
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case RotateLeft:
                return "Player " + player.name() + " rotates left";
            case RotateRight:
                return "Player " + player.name() + " rotates right";
            case SetColumn:
                return "Player " + player.name() + " set column " + column;
        }
        return "invalid move";
    }

    enum Type {
        RotateLeft,
        RotateRight,
        SetColumn
    }
}
