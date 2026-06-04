package Root.model;

public class Position {
    private final int x;
    private final int y;
    private final boolean isWall;
    public Position(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}