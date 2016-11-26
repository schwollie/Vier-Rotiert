package com.example.lars.vierrotiert;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Created by maus on 04.09.16.
 */
public interface Player {

    ListenableFuture<Move> set(Board board);

    Board.Field getField();
}
