package io.ileukocyte.ui;

import io.ileukocyte.ui.mnpuzzle.MNPuzzle;
import io.ileukocyte.ui.mnpuzzle.MNPuzzleGreedySolver;

public class Main {
    public static void main(String[] args) {
        // 4x4
        var initialState = new MNPuzzle.State(new int[][] {
                {2, 0, 8, 3},
                {1, 5, 7, 4},
                {9, 6, 10, 11},
                {13, 14, 15, 12}
        });
        var finalState = new MNPuzzle.State(new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}
        });

        // 3x3
        /*var initialState = new MNPuzzle.State(new int[][] {
                {1, 8, 2},
                {0, 4, 3},
                {7, 6, 5}
                //{8, 1, 2},
                //{0, 4, 3},
                //{7, 6, 5}
        });
        var finalState = new MNPuzzle.State(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });*/

        // 4x3
        /*var initialState = new MNPuzzle.State(new int[][] {
                {1, 2, 3},
                {0, 5, 6},
                {4, 11, 8},
                {7, 10, 9}
        });
        var finalState = new MNPuzzle.State(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 0}
        });*/

        var puzzle = new MNPuzzle(4, 4, initialState, finalState);
        var solver = new MNPuzzleGreedySolver(puzzle);//.setIterationLimit(1000000);

        var outOfPlaceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.OUT_OF_PLACE_COUNT);
        var totalDistanceResult = solver.solve(MNPuzzleGreedySolver.HeuristicFunction.TOTAL_DISTANCE);

        System.out.println(outOfPlaceResult);
        System.out.println(totalDistanceResult);

        //System.out.println(outOfPlaceResult.getPrintableSteps());
        //System.out.println(totalDistanceResult.getPrintableSteps());
    }
}
