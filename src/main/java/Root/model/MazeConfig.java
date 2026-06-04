package Root.model;

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
        this.width = this.height = 30;

        this.wallCellColor = Color.RED;
        this.pathColor = Color.GREEN;
        this.gridColor = Color.BLUE;
        this.isGridExist = true;
        this.animationDelayInMs = 80;
    }

    public String toString() {
        return "\nWall Color: " + this.wallCellColor.toString() +
                "\nPath Color: " + this.pathColor.toString() +
                "\nGrid Color: " + this.gridColor.toString() +
                "\nGrid: " + this.isGridExist +
                "\nAnimation Delay: " + this.animationDelayInMs +
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
        this.width = width;
    }

    public void setHeight(int height) {
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