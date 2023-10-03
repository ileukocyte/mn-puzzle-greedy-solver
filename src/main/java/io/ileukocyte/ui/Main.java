package io.ileukocyte.ui;

import io.ileukocyte.ui.mnpuzzle.MNPuzzle;
import io.ileukocyte.ui.mnpuzzle.MNPuzzleGreedySolver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        var file = new File("src/main/resources/examples.txt");

        try (var reader = new BufferedReader(new FileReader(file))) {
            String line;
            var lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.startsWith("//") && !line.isBlank()) {
                    line = line.split("(?:\\s+)?/{2}")[0];

                    var split = line.split("(?:\\s+)?:(?:\\s+)?");

                    var arraySize = split[0].split("(?:\\s+)?\\|(?:\\s+)?");
                    var initialStateValues = split[1];
                    var finalStateValues = split[2];

                    var rows = Integer.parseInt(arraySize[0]);
                    var columns = Integer.parseInt(arraySize[1]);

                    var initialStateList = new ArrayList<>(Arrays
                            .stream(initialStateValues.split("(?:\\s+)?\\|(?:\\s+)?"))
                            .map(Integer::parseInt)
                            .toList());
                    var finalStateList = new ArrayList<>(Arrays
                            .stream(finalStateValues.split("(?:\\s+)?\\|(?:\\s+)?"))
                            .map(Integer::parseInt)
                            .toList());

                    var initialStateArray = new int[rows][columns];
                    var finalStateArray = new int[rows][columns];

                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            initialStateArray[i][j] = initialStateList.remove(0);
                            finalStateArray[i][j] = finalStateList.remove(0);
                        }
                    }

                    var initialState = new MNPuzzle.State(initialStateArray);
                    var finalState = new MNPuzzle.State(finalStateArray);

                    var puzzle = new MNPuzzle(rows, columns, initialState, finalState);
                    var solver = new MNPuzzleGreedySolver(puzzle);

                    var outOfPlaceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.OUT_OF_PLACE_COUNT);
                    var totalDistanceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.TOTAL_DISTANCE);

                    System.out.printf("---------- #%d ----------:\n", ++lineNumber);
                    System.out.println(outOfPlaceResult);
                    System.out.println(totalDistanceResult);

                    //System.out.println(outOfPlaceResult.getPrintableSteps());
                    //System.out.println(totalDistanceResult.getPrintableSteps());
                }
            }
        }
    }
}
