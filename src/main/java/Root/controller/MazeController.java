package Root.controller;

import Root.core.Constants;
import Root.core.MyUtils;
import Root.model.Maze;
import Root.model.MazeConfig;
import Root.model.Position;
import Root.service.ApiService;
import Root.service.MazeDecoder;
import Root.view.MainWindow.MainWindow;
import Root.view.ViewUtils.MyButton;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class MazeController {
    private ApiService apiService;

    private MainWindow mainWindowView;
    private Maze mazeModel;

    private Position[][] mazePositions;
    private int mazeWidth;
    private int mazeHeight;

    public MazeController() {
        this.apiService = new ApiService();
        MazeConfig mazeConfig = this.apiService.getMazeConfig(Constants.DEFAULT_MAZE_SIZE, Constants.DEFAULT_MAZE_SIZE);
        MyUtils.downloadMazeAsBufferedImage(this.apiService, Constants.DEFAULT_MAZE_SIZE, Constants.DEFAULT_MAZE_SIZE);

        this.mazeModel = new Maze(mazeConfig);
        this.mainWindowView = new MainWindow(this.mazeModel);

        this.attachViewListeners();
    }

    private void initializeMaze() {

    }

    private void attachViewListeners() {
        // Wire up the Refresh Configurations Button action event
        this.mainWindowView.getConfigPanel().getRefreshConfigButton().addActionListener(e -> {
            this.executeWithButtonLock(this.mainWindowView.getConfigPanel().getRefreshConfigButton(), this.getRefreshConfigThread());
        });

        // Wire up the Get Maze Button action event
        this.mainWindowView.getConfigPanel().getMazeButton().addActionListener(e -> {
            this.executeWithButtonLock(this.mainWindowView.getConfigPanel().getMazeButton(), this.getMazeThread());
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

    // Gets Refresh Configs In A Separate Thread
    private Thread getRefreshConfigThread() {
        return new Thread(() -> {
            if (!this.updateMazeSize()) {
                return;
            }

            System.out.println("Refresh launching network request task...");

            // Fetch configuration properties from API
            MazeConfig dynamicConfig = this.apiService.getMazeConfig(this.mazeWidth, this.mazeHeight);

            // Update view safe on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                this.mainWindowView.getConfigPanel().setMazeConfig(dynamicConfig);
                System.out.println("Configuration Panel view fully refreshed with new API values!");
            });
        });
    }

    // Gets Maze In A Separate Thread
    private Thread getMazeThread() {
        return new Thread(() -> {
            if (!this.updateMazeSize()) {
                return;
            }

            try {
                System.out.println("Step 1: Fetching configurations sequentially...");
                // Reuse your refresh thread logic right here
                Thread refreshConfigThread = this.getRefreshConfigThread();
                refreshConfigThread.start();
                refreshConfigThread.join(); // Wait here until configuration is fetched and UI updated

                System.out.println("Step 2: Get Maze button clicked! Launching network request task...");

                // Download the live BufferedImage object directly from network stream
                BufferedImage freshlyDownloadedImage = MyUtils.downloadMazeAsBufferedImage(this.apiService, this.mazeWidth, this.mazeHeight);

                if (freshlyDownloadedImage != null) {
                    // Pass fresh image directly to decoder
                    MazeDecoder decoder = new MazeDecoder(this.mazeWidth, this.mazeHeight);
                    Position[][] dynamicPositions = decoder.getPositionsFromImage(freshlyDownloadedImage);

                    // Update model matrices
                    this.mazeModel.setWallPositions(dynamicPositions);

                    // Print and schedule panel repaint on the Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> {
                        System.out.println(this.mazeModel.toString());
                        this.mazeModel.repaint();
                    });
                }
            } catch (InterruptedException ex) {
                System.err.println("Maze generation workflow was interrupted!");
                Thread.currentThread().interrupt();
            }
        });
    }

    // Execute Thread With Button Lock
    private void executeWithButtonLock(MyButton button, Thread targetThread) {
        if (button == null || targetThread == null) return;

        // 1. Lock the button on the UI thread immediately to prevent spam-clicks
        button.setEnabled(false);

        // 2. Create a wrapper orchestrator thread to handle execution sequentially
        new Thread(() -> {
            try {
                targetThread.start();
                targetThread.join(); // Wait for the network task to finish completely
            } catch (InterruptedException ex) {
                System.err.println("Thread execution was interrupted!");
                Thread.currentThread().interrupt();
            } finally {
                // 3. Always re-enable the button back on the UI thread when work finishes
                SwingUtilities.invokeLater(() -> button.setEnabled(true));
            }
        }).start();
    }
}