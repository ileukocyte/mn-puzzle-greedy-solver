package io.ileukocyte.ui;

import io.ileukocyte.ui.mnpuzzle.MNPuzzle;
import io.ileukocyte.ui.mnpuzzle.MNPuzzleGreedySolver;

public class Main {
    public static void main(String[] args) {
        /*var initialState = new MNPuzzle.State(new int[][] {
                {12, 1, 2, 15},
                {11, 6, 5, 8},
                {7, 10, 9, 4},
                {0, 13, 14, 3}
        });
        var finalState = new MNPuzzle.State(new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}
        });*/
        var initialState = new MNPuzzle.State(new int[][] {
                /*{1, 8, 2},
                {0, 4, 3},
                {7, 6, 5}*/
                {8, 1, 2},
                {0, 4, 3},
                {7, 6, 5}
        });
        var finalState = new MNPuzzle.State(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });

        var puzzle = new MNPuzzle(3, 3, initialState, finalState);
        var solver = new MNPuzzleGreedySolver(puzzle);//.setIterationLimit(1000000);

        var outOfPlaceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.OUT_OF_PLACE_COUNT);
        //var totalDistanceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.TOTAL_DISTANCE);

        System.out.println(outOfPlaceResult);
        //System.out.println(totalDistanceResult);

        System.out.println(outOfPlaceResult.printableSteps());
    }
}
