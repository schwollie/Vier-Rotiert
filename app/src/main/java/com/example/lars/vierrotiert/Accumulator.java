package com.example.lars.vierrotiert;

/**
 * Created by maus on 03.10.16.
 */
class Accumulator {
    int red = 0;
    int yellow = 0;
    int redConsecutives = 0;
    int yellowConsecutives = 0;

    void addField(Board.Field field) {
        if (field == Board.Field.Yellow) {
            yellow++;
            red = 0;
            yellowConsecutives = Math.max(yellowConsecutives, yellow);
        } else if (field == Board.Field.Red) {
            red++;
            yellow = 0;
            redConsecutives = Math.max(redConsecutives, red);
        } else if (field == Board.Field.Empty) {
            yellow = 0;
            red = 0;
        }
    }

    void reset() {
        red = 0;
        yellow = 0;
        redConsecutives = 0;
        yellowConsecutives = 0;
    }
}
