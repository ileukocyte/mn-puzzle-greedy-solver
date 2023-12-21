package io.ileukocyte.ui.mnpuzzle;

import java.util.*;

public class MNPuzzleGreedySolver {
    private final MNPuzzle puzzle;
    private final MNPuzzle.State initialState;
    private final MNPuzzle.State targetState;

    private int iterationLimit = 0;

    public MNPuzzleGreedySolver(MNPuzzle puzzle) {
        this.puzzle = puzzle;
        this.initialState = puzzle.initialState();
        this.targetState = puzzle.targetState();
    }

    public Result solve(HeuristicFunction heuristics) {
        var iterationCount = 0;
        var nodeCount = 0;

        var now = System.nanoTime();

        var usedStates = new HashSet<String>();

        var currentNode = new Node(
                initialState,
                Objects.requireNonNull(heuristics) == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(initialState) : totalDistance(initialState),
                null,
                null
        );
        var nodeQueue = new PriorityQueue<>(Comparator.comparingInt(Node::heuristicValue));

        nodeQueue.add(currentNode);

        nodeCount++;

        while (!nodeQueue.isEmpty()) {
            iterationCount++;

            currentNode = nodeQueue.poll();

            if (currentNode.heuristicValue() == 0 || (iterationLimit > 0 && iterationCount >= iterationLimit)) {
                break;
            }

            usedStates.add(currentNode.currentState().toString());

            var emptyCoords = getEmptyCellIndex(currentNode.currentState());
            var r = emptyCoords[0];
            var c = emptyCoords[1];

            // The empty cell can be moved up
            if (r > 0) {
                var stateCopy = currentNode.currentState().copy();

                move(stateCopy, r, c, Operator.UP);

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode,
                        Operator.UP
                );

                if (!usedStates.contains(node.currentState().toString())) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            // The empty cell can be moved down
            if (r < puzzle.rows() - 1) {
                var stateCopy = currentNode.currentState().copy();

                move(stateCopy, r, c, Operator.DOWN);

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode,
                        Operator.DOWN
                );

                if (!usedStates.contains(node.currentState().toString())) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            // The empty cell can be moved left
            if (c > 0) {
                var stateCopy = currentNode.currentState().copy();

                move(stateCopy, r, c, Operator.LEFT);

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode,
                        Operator.LEFT
                );

                if (!usedStates.contains(node.currentState().toString())) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            // The empty cell can be moved right
            if (c < puzzle.columns() - 1) {
                var stateCopy = currentNode.currentState().copy();

                move(stateCopy, r, c, Operator.RIGHT);

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode,
                        Operator.RIGHT
                );

                if (!usedStates.contains(node.currentState().toString())) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }
        }

        now = System.nanoTime() - now;

        return new Result(
                currentNode.currentState().equals(targetState),
                iterationCount,
                nodeCount,
                now,
                currentNode
        );
    }

    // Heuristic function #1
    protected int cellsOutOfPlace(MNPuzzle.State currentState) {
        var wrongPositionCount = 0;

        for (int i = 0; i < puzzle.rows(); i++) {
            for (int j = 0; j < puzzle.columns(); j++) {
                if (currentState.toArray()[i][j] != 0
                        && currentState.toArray()[i][j] != targetState.toArray()[i][j]) {
                    wrongPositionCount++;
                }
            }
        }

        return wrongPositionCount;
    }

    // Heuristic function #2 (Manhattan distance)
    protected int totalDistance(MNPuzzle.State currentState) {
        var totalDistance = 0;

        var currentCoordsByValue = new int[puzzle.rows() * puzzle.columns()][2];
        var targetCoordsByValue = new int[puzzle.rows() * puzzle.columns()][2];

        for (int i = 0; i < puzzle.rows(); i++) {
            for (int j = 0; j < puzzle.columns(); j++) {
                currentCoordsByValue[currentState.toArray()[i][j]] = new int[] {i, j};
                targetCoordsByValue[targetState.toArray()[i][j]] = new int[] {i, j};
            }
        }

        // The loop starts with 1 in order to exclude the empty cell
        for (int i = 1; i < puzzle.rows() * puzzle.columns(); i++) {
            var currentCoords = currentCoordsByValue[i];
            var targetCoords = targetCoordsByValue[i];

            for (int j = 0; j < 2; j++) {
                totalDistance += Math.abs(targetCoords[j] - currentCoords[j]);
            }
        }

        return totalDistance;
    }

    private int[] getEmptyCellIndex(MNPuzzle.State state) {
        var coords = new int[2];

        for (int i = 0; i < puzzle.rows(); i++) {
            for (int j = 0; j < puzzle.columns(); j++) {
                if (state.toArray()[i][j] == 0) {
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }

        return coords;
    }

    private void swap(MNPuzzle.State state, int r1, int c1, int r2, int c2) {
        var temp = state.toArray()[r1][c1];

        state.toArray()[r1][c1] = state.toArray()[r2][c2];
        state.toArray()[r2][c2] = temp;
    }

    private void move(MNPuzzle.State state, int r, int c, Operator operator) {
        swap(state, r, c, r + operator.getX(), c + operator.getY());
    }

    public MNPuzzle getPuzzle() {
        return puzzle;
    }

    public MNPuzzle.State getInitialState() {
        return initialState;
    }

    public MNPuzzle.State getTargetState() {
        return targetState;
    }

    public int getIterationLimit() {
        return iterationLimit;
    }

    public MNPuzzleGreedySolver setIterationLimit(int iterationLimit) {
        this.iterationLimit = iterationLimit;

        return this;
    }

    public enum Operator {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int x;
        private final int y;

        Operator(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public enum HeuristicFunction {
        OUT_OF_PLACE_COUNT, TOTAL_DISTANCE
    }

    public record Node(
            MNPuzzle.State currentState,
            int heuristicValue,
            Node parent,
            Operator lastOperator
    ) {}

    public record Result(
            boolean isSolved,
            int iterationCount,
            int nodeCount,
            long timeNs,
            Node finalNode
    ) {
        /**
         * @return The solution's node path
         */
        public Set<Node> retrievePath() {
            var nodeSet = new LinkedHashSet<Node>();
            var currentNode = finalNode;

            while (currentNode != null) {
                nodeSet.add(currentNode);

                currentNode = currentNode.parent();
            }

            var nodeList = new ArrayList<>(nodeSet);

            Collections.reverse(nodeList);

            return new LinkedHashSet<>(nodeList);
        }

        /**
         * @return The string containing all the solution nodes and the in-between operators
         */
        public String getPrintablePath() {
            var builder = new StringBuilder();

            for (var node : retrievePath()) {
                if (node.lastOperator() != null) {
                    builder.append("\n|\n");
                    builder.append(node.lastOperator());
                    builder.append("\n|\n");
                }

                builder.append(node);
            }

            return builder.toString();
        }

        /**
         * @return The string containing all the solution states and the in-between operators
         */
        public String getPrintableSteps() {
            var builder = new StringBuilder();

            for (var node : retrievePath()) {
                if (node.lastOperator() != null) {
                    builder.append("\n|\n");
                    builder.append(node.lastOperator());
                    builder.append("\n|\n");
                }

                builder.append(node.currentState().toPuzzleBoardString());
            }

            return builder.toString();
        }
    }
}