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

    private void initializeMaze(MazeConfig mazeConfig) {
        this.width = mazeConfig.getWidth();
        this.height = mazeConfig.getHeight();
        this.positions = new Position[this.height][this.width];
        this.wallCellColor = mazeConfig.getWallCellColor();
        this.pathColor = mazeConfig.getPathColor();
        this.gridColor = mazeConfig.getGridColor();
        this.isDrawGrid = mazeConfig.isGridExist();
    }

    public void setWallPositions(boolean[][] wallPositions) {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.positions[i][j] = new Position(i, j, wallPositions[i][j]);
            }
        }
    }
}
