package Root.service;

import Root.core.Constants;
import Root.model.MazeConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ApiService {
    // Reusing a single static instance prevents connection memory leaks
    private static final OkHttpClient client = new OkHttpClient();

    public ApiService() {

    }

    public MazeConfig getMazeConfig() {
        MazeConfig mazeConfig = new MazeConfig();
        String mazeConfigString = "";

        try {
            Request request = new Request.Builder()
                    .url(Constants.GET_MAZE_CONFIG_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Server error code: " + response.code());
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new IOException("Server returned empty data body content.");
                }

                mazeConfigString = responseBody.string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject mazeConfigJson = new JSONObject(mazeConfigString);
            System.out.println("Received JSON: " + mazeConfigJson);

            // Get Values From JSONObject
            Color wallCellColor = Color.decode(mazeConfigJson.getString("wallCellColor"));
            Color pathColor = Color.decode(mazeConfigJson.getString("pathColor"));
            Color gridColor = Color.decode(mazeConfigJson.getString("gridColor"));

            boolean drawGrid = mazeConfigJson.getBoolean("drawGrid");
            int animationDelay = mazeConfigJson.getInt("animationDelayMs");

            // Put Values To MazeConfig
            mazeConfig.setWallCellColor(wallCellColor);
            mazeConfig.setPathColor(pathColor);
            mazeConfig.setGridColor(gridColor);
            mazeConfig.setGridExist(drawGrid);
            mazeConfig.setAnimationDelayInMs(animationDelay);

            System.out.println("Successfully built model: " + mazeConfig);
        } catch (Exception e) {
            System.err.println("Failed to parse maze config payload: " + e.getMessage());
            e.printStackTrace();
        }

        return mazeConfig;
    }

    public BufferedImage getMazeImage() {
        // Using BufferedImage instead of the raw abstract Image class is best for Swing painting
        BufferedImage mazeImage = null;

        Request request = new Request.Builder()
                .url(Constants.GET_MAZE_IMAGE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Server error code: " + response.code());
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Server returned empty data body content.");
            }

            // CHANGED: Stream the incoming download raw bytes directly into the ImageIO parser engine
            try (InputStream inputStream = responseBody.byteStream()) {
                mazeImage = ImageIO.read(inputStream);
            }

            if (mazeImage == null) {
                throw new IOException("Downloaded data stream could not be decoded as a valid image format.");
            }

        } catch (IOException e) {
            System.err.println("Failed to download maze canvas layout: " + e.getMessage());
            e.printStackTrace();
        }

        return mazeImage;
    }
}