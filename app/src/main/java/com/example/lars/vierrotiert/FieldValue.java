package com.example.lars.vierrotiert;

public class FieldValue {
    int redValue = 0;
    int yellowValue = 0;

    void set(Board.Field field, int value) {
        switch (field) {
            case Red:
                redValue = value;
                break;
            case Yellow:
                yellowValue = value;
                break;
        }
    }

    void reset() {
        redValue = 0;
        yellowValue = 0;
    }

    public int get(Board.Field field) {
        switch (field) {
            case Red:
                return redValue;
            case Yellow:
                return yellowValue;
        }
        return 0;
    }
}
