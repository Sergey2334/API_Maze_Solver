package Root.service;

import Root.model.Position;
import java.util.*;

public class MazeSolver {
    private final Position[][] grid;
    private final int rows;
    private final int cols;

    private final List<Position> historyOfSteps;
    private final List<Position> finalPath;

    public MazeSolver(Position[][] grid) {
        this.grid = grid;
        this.rows = this.grid.length;
        this.cols = this.grid[0].length;
        this.historyOfSteps = new ArrayList<>();
        this.finalPath = new ArrayList<>();
    }

    public boolean solveBFS() {
        this.historyOfSteps.clear();
        this.finalPath.clear();

        if (this.grid == null || this.rows == 0 || this.cols == 0) {
            return false;
        }

        // 1. Strictly target the matrix indices directly to completely bypass coordinate flipping bugs
        Position start = this.grid[0][0];
        Position end = this.grid[this.rows - 1][this.cols - 1];

        if (start.isWall() || end.isWall()) {
            System.err.println("Fatal: Start or End index is blocked by a wall!");
            return false;
        }

        // 2. Queue stores a tiny tracking wrapper so we always know the current loop index row and column
        Queue<int[]> queue = new LinkedList<>();
        // Maps a Position node back to its parent Position node for path reconstruction
        Map<Position, Position> parentMap = new HashMap<>();
        Set<Position> visitedSet = new HashSet<>();

        // Add start node coordinates {row, column}
        queue.add(new int[]{0, 0});
        visitedSet.add(start);

        // 3. Define orthogonal direction modifiers: {deltaRow, deltaColumn}
        int[][] directions = {
                {-1,  0}, // Up
                { 1,  0}, // Down
                { 0, -1}, // Left
                { 0,  1}  // Right
        };

        while (!queue.isEmpty()) {
            int[] currentCoords = queue.poll();
            int currRow = currentCoords[0];
            int currCol = currentCoords[1];

            Position currentPos = this.grid[currRow][currCol];
            this.historyOfSteps.add(currentPos);

            // Goal check reached
            if (currentPos == end) {
                this.reconstructPath(parentMap, end);
                return true;
            }

            // 4. Explore neighbors using pure index row/column adjustments
            for (int[] dir : directions) {
                int nextRow = currRow + dir[0];
                int nextCol = currCol + dir[1];

                if (this.isValidMove(nextRow, nextCol, visitedSet)) {
                    Position neighbor = this.grid[nextRow][nextCol];
                    visitedSet.add(neighbor);
                    parentMap.put(neighbor, currentPos);
                    queue.add(new int[]{nextRow, nextCol});
                }
            }
        }

        return false;
    }

    private boolean isValidMove(int r, int c, Set<Position> visited) {
        return r >= 0 && r < this.rows && c >= 0 && c < this.cols
                && !this.grid[r][c].isWall() && !visited.contains(this.grid[r][c]);
    }

    private void reconstructPath(Map<Position, Position> parentMap, Position end) {
        Position current = end;
        while (current != null) {
            this.finalPath.add(0, current);
            current = parentMap.get(current);
        }
    }

    public List<Position> getHistoryOfSteps() {
        return this.historyOfSteps;
    }

    public List<Position> getFinalPath() {
        return this.finalPath;
    }
}