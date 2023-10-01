package io.ileukocyte.ui.mnpuzzle;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MNPuzzle(int rows, int columns, State initialState, State finalState) {
    public MNPuzzle {
        var initialValues = Arrays.stream(initialState.toArray())
                .flatMapToInt(IntStream::of)
                .sorted()
                .toArray();
        var finalValues = Arrays.stream(finalState.toArray())
                .flatMapToInt(IntStream::of)
                .sorted()
                .toArray();

        if (!Arrays.equals(initialValues, finalValues)) {
            throw new IllegalArgumentException("Both the initial and final states must consist of the same elements!");
        }
    }

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

        public String toPuzzleBoardString() {
            var max = String.valueOf(Arrays.stream(array)
                    .flatMapToInt(IntStream::of)
                    .max()
                    .orElseThrow()).length();
            var builder = new StringBuilder();

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    builder.append(String.format("%" + max + "d", array[i][j]));

                    if (j < array[i].length - 1) {
                        builder.append('|');
                    }
                }

                if (i < array.length - 1) {
                    builder.append("\n");
                }
            }

            return builder.toString();
        }

        @Override
        public String toString() {
            return Arrays.stream(array).flatMapToInt(IntStream::of)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining("|"));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof State && Arrays.deepEquals(array, ((State) obj).toArray());
        }
    }
}