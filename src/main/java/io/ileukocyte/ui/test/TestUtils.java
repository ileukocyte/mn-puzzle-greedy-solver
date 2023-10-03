package io.ileukocyte.ui.test;

import io.ileukocyte.ui.mnpuzzle.MNPuzzle;

import java.util.ArrayList;
import java.util.Collections;

public class TestUtils {
    public static MNPuzzle.State generateRandomState(int rows, int columns) {
        var stateArray = new int[rows][columns];
        var values = new ArrayList<Integer>();

        for (int i = 0; i < rows * columns; i++) {
            values.add(i);
        }

        Collections.shuffle(values);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                stateArray[i][j] = values.remove(0);
            }
        }

        return new MNPuzzle.State(stateArray);
    }
}
