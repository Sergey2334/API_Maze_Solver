package Root.model;

import Root.core.Constants;

import java.awt.*;

public class MazeConfig {
    private int width;
    private int height;

    private Color wallCellColor;
    private Color pathColor;
    private Color gridColor;
    private boolean isGridExist;
    private int animationDelayInMs;

    public MazeConfig() {
        this.width = this.height = Constants.DEFAULT_MAZE_SIZE;

        this.wallCellColor = Color.RED;
        this.pathColor = Color.GREEN;
        this.gridColor = Color.BLUE;
        this.isGridExist = true;
        this.animationDelayInMs = Constants.DEFAULT_ANIMATION_DELAY;
    }

    public String toString() {
        return "\nWall Color: " + this.wallCellColor.toString() +
                "\nPath Color: " + this.pathColor.toString() +
                "\nGrid Color: " + this.gridColor.toString() +
                "\nGrid: " + this.isGridExist +
                "\nAnimation Delay: " + this.animationDelayInMs +
                "\nWidth: " + this.width +
                "\nHeight: " + this.height +
                "\n";
    }

    // --- Getters ---
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getWallCellColor() {
        return this.wallCellColor;
    }

    public Color getPathColor() {
        return this.pathColor;
    }

    public Color getGridColor() {
        return this.gridColor;
    }

    public boolean isGridExist() {
        return this.isGridExist;
    }

    public int getAnimationDelayInMs() {
        return this.animationDelayInMs;
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

    public void setWallCellColor(Color wallCellColor) {
        this.wallCellColor = wallCellColor;
    }

    public void setPathColor(Color pathColor) {
        this.pathColor = pathColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void setGridExist(boolean gridExist) {
        isGridExist = gridExist;
    }

    public void setAnimationDelayInMs(int animationDelayInMs) {
        this.animationDelayInMs = animationDelayInMs;
    }
}