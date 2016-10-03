package com.example.lars.vierrotiert;

/**
 * Created by maus on 03.10.16.
 */
public enum Winner {
    None(Board.Field.Empty),
    Red(Board.Field.Red),
    Yellow(Board.Field.Yellow),
    Both(null);

    private final Board.Field associatedField;

    private Winner(Board.Field associatedField) {
        this.associatedField = associatedField;
    }

    public boolean isPlayer(Board.Field field) {
        return field == this.associatedField;
    }
}
