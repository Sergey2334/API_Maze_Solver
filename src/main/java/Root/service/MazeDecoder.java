package Root.service;

import Root.model.Position;

import java.awt.image.BufferedImage;

public class MazeDecoder {
    private int width;
    private int height;

    public MazeDecoder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Decodes a BufferedImage directly from memory to prevent disk-caching bugs.
     */
    public Position[][] getPositionsFromImage(BufferedImage mazeImage) {
        Position[][] positions = new Position[this.height][this.width];

        int imgW = mazeImage.getWidth();
        int imgH = mazeImage.getHeight();

        double cellWidthPixels = (double) imgW / this.width;
        double cellHeightPixels = (double) imgH / this.height;

        System.out.println("--- LIVE MEMORY DECODER START ---");
        System.out.println("Image Resolution: " + imgW + "x" + imgH);

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                int pixelX = (int) ((x * cellWidthPixels) + (cellWidthPixels / 2.0));
                int pixelY = (int) ((y * cellHeightPixels) + (cellHeightPixels / 2.0));

                if (pixelX >= imgW) pixelX = imgW - 1;
                if (pixelY >= imgH) pixelY = imgH - 1;

                int rgb = mazeImage.getRGB(pixelX, pixelY);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Path check: if it's black/dark or colorful, it's a wall.
                // Since paths are always WHITE, a cell is a path ONLY if R, G, and B are high.
                boolean isPath = (r > 200 && g > 200 && b > 200);
                boolean isWall = !isPath;

//                System.out.printf("Cell[%d][%d] -> Pixel (%d,%d) -> RGB: (%d,%d,%d) -> Wall: %b%n",
//                        y, x, pixelX, pixelY, r, g, b, isWall);

                positions[y][x] = new Position(x, y, isWall);
            }
        }
        System.out.println("--- LIVE MEMORY DECODER END ---");
        return positions;
    }
}