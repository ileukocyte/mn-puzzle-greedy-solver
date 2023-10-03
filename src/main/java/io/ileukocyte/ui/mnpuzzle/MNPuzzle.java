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

        if (rows != initialState.getRows() || rows != finalState.getRows()
                || columns != initialState.getColumns() || columns != finalState.getColumns()) {
            throw new IllegalArgumentException("The state dimensions must be consistent!");
        }
    }

    public static class State {
        private final int[][] array;
        private final int rows;
        private final int columns;

        public State(int[][] array) {
            this(array, true);
        }

        private State(int[][] array, boolean checkSize) {
            if (checkSize) {
                if (array.length == 0) {
                    throw new IllegalArgumentException("The provided array must not be empty!");
                }

                var referenceValue = array[0].length;

                for (var subarray : array) {
                    if (subarray.length != referenceValue) {
                        throw new IllegalArgumentException("The array sizes must be consistent!");
                    }
                }
            }

            this.array = array;
            this.rows = array.length;
            this.columns = array[0].length;
        }

        public int[][] toArray() {
            return array;
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        public State copy() {
            return new State(Arrays.stream(array).map(int[]::clone).toArray(int[][]::new), false);
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