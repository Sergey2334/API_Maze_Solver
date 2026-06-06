package Root.model;

import java.awt.*;

public class Maze {
    private int width;
    private int height;
    private Position[][] positions;

    private Color wallCellColor;
    private Color pathColor;
    private Color gridColor;
    private boolean isDrawGrid;

    public Maze(MazeConfig mazeConfig) {
        this.initializeMaze(mazeConfig);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Maze\n");
        for (int i = 0; i < this.positions.length; i++) {
            sb.append("__");
        }
        sb.append("\n");
        for (int i = 0; i < this.positions.length; i++) {
            sb.append("|");
            for (int j = 0; j < this.positions[i].length; j++) {
                if (this.positions[i][j].isWall()) {
                    sb.append("@ ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("|\n"); // Move to the next line after finishing a row
        }
        for (int i = 0; i < this.positions.length; i++) {
            sb.append("__");
        }
        sb.append("\n");

        return sb.toString();
    }

    private void initializeMaze(MazeConfig mazeConfig) {
        this.width = mazeConfig.getWidth();
        this.height = mazeConfig.getHeight();
        this.positions = new Position[this.height][this.width];
        this.wallCellColor = mazeConfig.getWallCellColor();
        this.pathColor = mazeConfig.getPathColor();
        this.gridColor = mazeConfig.getGridColor();
        this.isDrawGrid = mazeConfig.isGridExist();
    }

    public void setWallPositions(Position[][] wallPositions) {
        int rows = wallPositions.length;
        int cols = wallPositions[0].length;

        // 1. Initialize the array structure
        this.positions = new Position[rows][cols];

        // 2. Populate every single index so none of them are null
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.positions[i][j] = new Position(i, j, wallPositions[i][j].isWall());
            }
        }
    }
}