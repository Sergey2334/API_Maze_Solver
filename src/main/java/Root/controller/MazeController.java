package Root.controller;

import Root.core.MyUtils;
import Root.model.Maze;
import Root.model.MazeConfig;
import Root.service.ApiService;
import Root.view.MainWindow.MainWindow;

import javax.swing.*;

public class MazeController {
    private ApiService apiService;

    private MainWindow mainWindowView;
    private Maze mazeModel;

    private boolean[][] mazePositions;
    private int width;
    private int height;

    public MazeController() {
        this.apiService = new ApiService();
        MazeConfig mazeConfig = this.apiService.getMazeConfig();
        MyUtils.downloadAndSaveMazeImage(this.apiService);

        this.mazeModel = new Maze(mazeConfig);
        this.mainWindowView = new MainWindow();

        this.attachViewListeners();
    }

    private void initializeMaze() {

    }

    private void attachViewListeners() {
        // Wire up the Refresh Configurations button action event
        this.mainWindowView.getConfigPanel().getRefreshConfigButton().addActionListener(e -> {
            System.out.println("Refresh button clicked! Launching network request task...");

            // 1. Run the API call on a background thread so the FlatLaf UI doesn't freeze
            new Thread(() -> {
                MazeConfig dynamicConfig = this.apiService.getMazeConfig();

                // 2. Safely jump back onto the Swing interface loop thread to change properties
                SwingUtilities.invokeLater(() -> {
                    // This calls your method to clear old elements and update colors/text
                    this.mainWindowView.getConfigPanel().setMazeConfig(dynamicConfig);
                    System.out.println("Configuration Panel view fully refreshed with new API values!");
                });
            }).start();
        });

        this.mainWindowView.getConfigPanel().getMazeButton().addActionListener(e -> {
            System.out.println("Get Maze button clicked! Launching network request task...");

            // 1. Run the API call on a background thread so the FlatLaf UI doesn't freeze
            new Thread(() -> {
                MyUtils.downloadAndSaveMazeImage(this.apiService);
            }).start();
        });
    }
}
