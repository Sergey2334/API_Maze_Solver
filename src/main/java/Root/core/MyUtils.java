package Root.core;

import Root.service.ApiService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;

public final class MyUtils {
    private MyUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Random random = new Random();

    public static int getRandomNumber(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    public static void downloadAndSaveMazeImage(ApiService apiService) {
        // 1. Always execute file and network tasks on a separate working thread
        new Thread(() -> {
            long start = System.currentTimeMillis();

            System.out.println("Downloading maze canvas image from API...");

            // 2. Fetch the image from your ApiService
            BufferedImage mazeImg = apiService.getMazeImage();

            if (mazeImg == null) {
                System.err.println("Aborting save: No valid image data downloaded.");
                return;
            }

            try {
                // 3. Define the destination folder path (e.g., a folder named "saved_mazes")
                File directory = new File("src/main/resources/mazeImage");

                // Automatically build the physical folders on your disk if they don't exist yet
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // 4. Create the target file naming parameter (e.g., "maze.png")
                File outputFile = new File(directory, "maze.png");

                // 5. Write the memory asset disk block cleanly as a high-quality PNG
                // Arguments: (RenderedImage, FormatName, FileDestination)
                boolean success = ImageIO.write(mazeImg, "png", outputFile);

                if (success) {
                    System.out.println("Success! Maze canvas image saved to: " + outputFile.getAbsolutePath());
                } else {
                    System.err.println("Error: No appropriate image writer engine found for PNG.");
                }

            } catch (IOException e) {
                System.err.println("Failed to write image payload to local disk: " + e.getMessage());
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();

            System.out.println("Total Time To Download: " + MyUtils.formatMStoHHMMSSMS(end - start));
        }).start();
    }

    public static String formatMStoHHMMSSMS(long ms) {
        Duration duration = Duration.ofMillis(ms);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        return String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, millis);
    }


    private static String addLeadingZero(int number) {
        String result = "0";

        if (number < 10) {
            result += number;
        } else {
            result = String.valueOf(number);
        }

        return result;
    }
}