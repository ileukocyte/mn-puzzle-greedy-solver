package io.ileukocyte.ui.eightpuzzle;

import java.util.*;
import java.util.stream.Collectors;

public class EightPuzzleGreedySolver {
    private final EightPuzzle puzzle;
    private final EightPuzzle.State initialState;
    private final EightPuzzle.State finalState;

    private int iterationLimit = 0;

    public EightPuzzleGreedySolver(EightPuzzle puzzle) {
        this.puzzle = puzzle;
        this.initialState = puzzle.initialState();
        this.finalState = puzzle.finalState();
    }

    public Result solve(HeuristicFunction heuristics) {
        var iterationCount = 0;
        var nodeCount = 0;

        var currentNode = new Node(
                initialState,
                Objects.requireNonNull(heuristics) == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(initialState) : totalDistance(initialState),
                null
        );
        var nodeQueue = new PriorityQueue<>(Comparator.comparingInt(Node::heuristicValue));
        var usedStates = new HashSet<EightPuzzle.State>();

        nodeQueue.add(currentNode);

        nodeCount++;

        while (!nodeQueue.isEmpty()) {
            iterationCount++;

            currentNode = nodeQueue.poll();

            if (currentNode.heuristicValue() == 0 || (iterationLimit > 0 && iterationCount >= iterationLimit)) {
                break;
            }

            usedStates.add(initialState);

            var emptyCoords = getEmptyCellIndex(currentNode.currentState());
            var r = emptyCoords[0];
            var c = emptyCoords[1];

            if (r > 0) {
                var stateCopy = currentNode.currentState().copy();

                stateCopy.toArray()[r][c] = stateCopy.toArray()[r - 1][c];
                stateCopy.toArray()[r - 1][c] = 0;

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode
                );

                if (usedStates.stream().noneMatch(s -> s.equals(node.currentState()))) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            if (r < 2) {
                var stateCopy = currentNode.currentState().copy();

                stateCopy.toArray()[r][c] = stateCopy.toArray()[r + 1][c];
                stateCopy.toArray()[r + 1][c] = 0;

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode
                );

                if (usedStates.stream().noneMatch(s -> s.equals(node.currentState()))) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            if (c > 0) {
                var stateCopy = currentNode.currentState().copy();

                stateCopy.toArray()[r][c] = stateCopy.toArray()[r][c - 1];
                stateCopy.toArray()[r][c - 1] = 0;

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode
                );

                if (usedStates.stream().noneMatch(s -> s.equals(node.currentState()))) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }

            if (c < 2) {
                var stateCopy = currentNode.currentState().copy();

                stateCopy.toArray()[r][c] = stateCopy.toArray()[r][c + 1];
                stateCopy.toArray()[r][c + 1] = 0;

                var node = new Node(
                        stateCopy,
                        heuristics == HeuristicFunction.OUT_OF_PLACE_COUNT ? cellsOutOfPlace(stateCopy) : totalDistance(stateCopy),
                        currentNode
                );

                if (usedStates.stream().noneMatch(s -> s.equals(node.currentState()))) {
                    nodeQueue.add(node);
                    nodeCount++;
                }
            }
        }

        return new Result(
                currentNode.currentState().equals(finalState),
                iterationCount,
                nodeCount,
                currentNode
        );
    }

    // Heuristic function #1
    protected int cellsOutOfPlace(EightPuzzle.State currentState) {
        var wrongPositionCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState.toArray()[i][j] != 0
                        && currentState.toArray()[i][j] != finalState.toArray()[i][j]) {
                    wrongPositionCount++;
                }
            }
        }

        return wrongPositionCount;
    }

    // Heuristic function #2 (Manhattan Distance)
    protected int totalDistance(EightPuzzle.State currentState) {
        var totalDistance = 0;

        var currentCoordsByValue = new int[9][2];
        var finalCoordsByValue = new int[9][2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentCoordsByValue[currentState.toArray()[i][j]] = new int[] {i, j};
                finalCoordsByValue[finalState.toArray()[i][j]] = new int[] {i, j};
            }
        }

        // The loop starts with 1 in order to exclude the empty cell
        for (int i = 1; i < 9; i++) {
            var currentCoords = currentCoordsByValue[i];
            var finalCoords = finalCoordsByValue[i];

            for (int j = 0; j < 2; j++) {
                totalDistance += Math.abs(finalCoords[j] - currentCoords[j]);
            }
        }

        return totalDistance;
    }

    public int[] getEmptyCellIndex(EightPuzzle.State state) {
        var coords = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state.toArray()[i][j] == 0) {
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }

        return coords;
    }

    public EightPuzzle getPuzzle() {
        return puzzle;
    }

    public EightPuzzle.State getInitialState() {
        return initialState;
    }

    public EightPuzzle.State getFinalState() {
        return finalState;
    }

    public int getIterationLimit() {
        return iterationLimit;
    }

    public EightPuzzleGreedySolver setIterationLimit(int iterationLimit) {
        this.iterationLimit = iterationLimit;

        return this;
    }

    public enum HeuristicFunction {
        OUT_OF_PLACE_COUNT, TOTAL_DISTANCE
    }

    public record Node(
            EightPuzzle.State currentState,
            int heuristicValue,
            Node parent
    ) {}

    public record Result(boolean isSuccessful, int iterationCount, int nodeCount, Node finalNode) {
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

        public String printablePath() {
            return retrievePath().stream().map(Record::toString).collect(Collectors.joining("\n|\n"));
        }

        public String printableSteps() {
            return retrievePath().stream()
                    .map(n -> n.currentState().toString())
                    .collect(Collectors.joining("\n|\n"));
        }
    }
}