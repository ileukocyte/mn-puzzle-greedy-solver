package io.ileukocyte.ui.eightpuzzle;

import java.util.Arrays;

public record EightPuzzle(State initialState, State finalState) {
    public static class State {
        private final int[][] array;

        private State(int[][] rawArray) {
            this.array = rawArray;
        }

        public State(int[] firstRow, int[] secondRow, int[] thirdRow) {
            this(new int[][] {firstRow, secondRow, thirdRow});
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