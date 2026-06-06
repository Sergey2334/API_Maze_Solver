package Root.model;

import Root.core.Constants;

import javax.swing.*;
import java.awt.*;

public class Maze extends JLabel {
    private int width;
    private int height;
    private Position[][] positions;

    private Color wallCellColor;
    private Color pathColor;
    private Color solvedPathColor;
    private Color gridColor;
    private boolean isDrawGrid;

    public Maze(MazeConfig mazeConfig) {
        this.initialize();
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

    private void initialize() {
        this.pathColor = Constants.MAZE_PATH_COLOR;
    }

    private void initializeMaze(MazeConfig mazeConfig) {
        this.width = mazeConfig.getWidth();
        this.height = mazeConfig.getHeight();
        this.positions = new Position[this.height][this.width];
        this.wallCellColor = mazeConfig.getWallCellColor();
        this.solvedPathColor = mazeConfig.getPathColor();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Guard check to make sure the maze array is decoded and populated
        if (this.positions == null || this.positions.length == 0 || this.positions[0].length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // 2. Extract dimensions directly from the live position array
        int rows = this.positions.length;
        int cols = this.positions[0].length;

        // 3. Get the real-time panel dimensions directly
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();

        // 4. Force cells to be perfect squares by finding the limiting dimension
        // Dynamic Scaling :D
        int cellWidth = panelWidth / cols;
        int cellHeight = panelHeight / rows;
        int cellSize = Math.min(cellWidth, cellHeight); // Ensure uniform scale

        // 5. Calculate total grid pixel dimensions based on our locked cell size
        int totalMazeWidth = cols * cellSize;
        int totalMazeHeight = rows * cellSize;

        // 6. Calculate centering padding offsets for both axes
        int offsetX = (panelWidth - totalMazeWidth) / 2;
        int offsetY = (panelHeight - totalMazeHeight) / 2;

        // 7. Render loop
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position currentPos = this.positions[i][j];

                // Check for safety in case an array slot is somehow null
                if (currentPos == null) continue;

                // Determine pixel coordinates incorporating the centering offsets
                int x = offsetX + (j * cellSize);
                int y = offsetY + (i * cellSize);

                // 8. Select color based on wall state and solver animation flags
                if (currentPos.isWall()) {
                    g2d.setColor(this.wallCellColor);
                } else if (currentPos.isFinalPath()) {
                    g2d.setColor(this.solvedPathColor);
                } else if (currentPos.isVisited()) {
                    g2d.setColor(Color.YELLOW); // Clear yellow for the exploration search phase
                } else {
                    g2d.setColor(this.pathColor); // Default background track pathway color
                }

                // Fill block tile using uniform sizing
                g2d.fillRect(x, y, cellSize, cellSize);

                // 9. Render grid overlay lines if requested
                if (this.isDrawGrid) {
                    g2d.setColor(this.gridColor);
                    g2d.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    // --- Getters ---
    public Position[][] getPositions() {
        return this.positions;
    }

    public int getMazeWidth() {
        return this.width;
    }

    public int getMazeHeight() {
        return this.height;
    }

    // --- Setters ---
    public void setWidth(int width) {
        if (width < Constants.MIN_MAZE_SIZE || width > Constants.MAX_MAZE_SIZE) {
            System.out.println("Invalid Width, Set Default Width 30");
            width = Constants.DEFAULT_MAZE_SIZE;
        }
        this.width = width;
    }

    public void setHeight(int height) {
        if (height < Constants.MIN_MAZE_SIZE || height > Constants.MAX_MAZE_SIZE) {
            System.out.println("Invalid Height, Set Default Height 30");
            height = Constants.DEFAULT_MAZE_SIZE;
        }
        this.height = height;
    }
}