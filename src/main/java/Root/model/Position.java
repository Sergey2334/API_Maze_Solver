package Root.model;

public class Position {
    private final int x;
    private final int y;
    private boolean isWall;

    // --- New Tracking States for Solver/Animation ---
    private boolean isVisited;
    private boolean isFinalPath;

    public Position(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
        this.isVisited = false;
        this.isFinalPath = false;
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

    public boolean isVisited() {
        return this.isVisited;
    }

    public boolean isFinalPath() {
        return this.isFinalPath;
    }

    // --- Setters ---
    public void setIsWall(boolean isWall) {
        this.isWall = isWall;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public void setIsFinalPath(boolean isFinalPath) {
        this.isFinalPath = isFinalPath;
    }

    // --- Helper Method to Reset State ---
    // Useful for clearing a previous solution when a user loads a new maze
    public void resetSolverStates() {
        this.isVisited = false;
        this.isFinalPath = false;
    }
}