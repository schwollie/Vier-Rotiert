package com.example.lars.vierrotiert;

import android.os.Bundle;

/**
 * Created by maus on 02.09.16.
 */
public class Vier_Routiert extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private int[][] field = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };

    public void rechtsdrehen(int[][] field) {


    }


    public void linksdrehen(int[][] field) {

    }

}
