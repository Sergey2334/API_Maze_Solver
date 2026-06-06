package Root.model;

public class Position {
    private final int x;
    private final int y;
    private boolean isWall;

    public Position(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    // --- Getters ---
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isWall() {
        return this.isWall;
    }

    // --- Setters ---
    public void setIsWall(boolean isWall) {
        this.isWall = isWall;
    }
}