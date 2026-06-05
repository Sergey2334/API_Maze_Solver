package Root.controller;

import Root.core.Constants;
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
    private int mazeWidth;
    private int mazeHeight;

    public MazeController() {
        this.apiService = new ApiService();
        MazeConfig mazeConfig = this.apiService.getMazeConfig(Constants.DEFAULT_MAZE_SIZE, Constants.DEFAULT_MAZE_SIZE);
        MyUtils.downloadAndSaveMazeImage(this.apiService, Constants.DEFAULT_MAZE_SIZE, Constants.DEFAULT_MAZE_SIZE);

        this.mazeModel = new Maze(mazeConfig);
        this.mainWindowView = new MainWindow();

        this.attachViewListeners();
    }

    private void initializeMaze() {

    }

    private void attachViewListeners() {
        // Wire up the Refresh Configurations button action event
        this.mainWindowView.getConfigPanel().getRefreshConfigButton().addActionListener(e -> {
            // 1. Run the API call on a background thread so the FlatLaf UI doesn't freeze
            new Thread(() -> {
                // 2. Update Maze Size From Text Fields
                if (!this.updateMazeSize()) {
                    return;
                }

                System.out.println("Refresh button clicked! Launching network request task...");

                // 3. Update the config model
                MazeConfig dynamicConfig = this.apiService.getMazeConfig(this.mazeWidth, this.mazeHeight);

                // 4. Safely jump back onto the Swing interface loop thread to change properties
                SwingUtilities.invokeLater(() -> {
                    this.mainWindowView.getConfigPanel().setMazeConfig(dynamicConfig);
                    System.out.println("Configuration Panel view fully refreshed with new API values!");
                });
            }).start();
        });

        this.mainWindowView.getConfigPanel().getMazeButton().addActionListener(e -> {
            if (!this.updateMazeSize()) {
                return;
            }
            System.out.println("Get Maze button clicked! Launching network request task...");

            // 1. Run the API call on a background thread so the FlatLaf UI doesn't freeze
            new Thread(() -> {
                this.updateMazeSize();
                MyUtils.downloadAndSaveMazeImage(this.apiService, this.mazeWidth, this.mazeHeight);
            }).start();
        });
    }

    private boolean updateMazeSize() {
        try {
            // 1. Get raw text from the fields
            String widthText = this.mainWindowView.getConfigPanel().getWidthText().trim();
            String heightText = this.mainWindowView.getConfigPanel().getHeightText().trim();

            // 2. Validate that they are not empty before parsing
            if (widthText.isEmpty() || heightText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                        this.mainWindowView,
                        "Width and Height fields cannot be empty!",
                        "Input Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            // 3. Attempt to convert strings to integers
            int width = Integer.parseInt(widthText);
            this.mazeWidth = width;
            int height = Integer.parseInt(heightText);
            this.mazeHeight = height;

            return true;
        } catch (NumberFormatException ex) {
            // Triggers if the user types letters (e.g., "abc") or symbols
            javax.swing.JOptionPane.showMessageDialog(
                    this.mainWindowView,
                    "Please enter valid whole numbers for width and height.",
                    "Invalid Input",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        } catch (IllegalArgumentException ex) {
            // Triggers if your MazeConfig setters reject the size (e.g., out of 5-100 range)
            javax.swing.JOptionPane.showMessageDialog(
                    this.mainWindowView,
                    ex.getMessage(),
                    "Size Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}
