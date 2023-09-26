package io.ileukocyte.ui.mnpuzzle;

import java.util.Arrays;

public record MNPuzzle(
        int rows,
        int columns,
        State initialState,
        State finalState
) {
    public static class State {
        private final int[][] array;

        public State(int[][] rawArray) {
            this.array = rawArray;
        }

        public int[][] toArray() {
            return array;
        }

        public State copy() {
            return new State(Arrays.stream(array).map(int[]::clone).toArray(int[][]::new));
        }

        @Override
        public String toString() {
            return Arrays.deepToString(array);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof State && Arrays.deepEquals(array, ((State) obj).toArray());
        }
    }
}