package Root.controller;

import Root.core.Constants;
import Root.core.MyUtils;
import Root.model.Maze;
import Root.model.MazeConfig;
import Root.model.Position;
import Root.service.ApiService;
import Root.service.MazeAnimator;
import Root.service.MazeDecoder;
import Root.service.MazeSolver;
import Root.view.MainWindow.MainWindow;
import Root.view.ViewUtils.MyButton;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class MazeController {
    private ApiService apiService;

    private MainWindow mainWindowView;
    private Maze mazeModel;

    private Position[][] mazePositions;

    // Track the active animator object to control playbacks cleanly
    private MazeAnimator activeAnimator;

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

        // Wire up the Check Solution Button action event
        this.mainWindowView.getMazePanel().getCheckSolutionButton().addActionListener(e -> {
            this.executeWithButtonLock(this.mainWindowView.getMazePanel().getCheckSolutionButton(), this.getSolveMazeThread());
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
            this.mazeModel.setWidth(Integer.parseInt(widthText));
            this.mazeModel.setHeight(Integer.parseInt(heightText));

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
            MazeConfig dynamicConfig = this.apiService.getMazeConfig(this.mazeModel.getMazeWidth(), this.mazeModel.getMazeHeight());

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
                BufferedImage freshlyDownloadedImage = MyUtils.downloadMazeAsBufferedImage(this.apiService, this.mazeModel.getMazeWidth(), this.mazeModel.getMazeHeight());

                if (freshlyDownloadedImage != null) {
                    // Pass fresh image directly to decoder
                    MazeDecoder decoder = new MazeDecoder(this.mazeModel.getMazeHeight(), this.mazeModel.getMazeWidth());
                    Position[][] dynamicPositions = decoder.getPositionsFromImage(freshlyDownloadedImage);

                    // Update model matrices
                    this.mazeModel.setWallPositions(dynamicPositions);
                    this.mazePositions = this.mazeModel.getPositions(); // Added This

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

    private Thread getSolveMazeThread() {
        return new Thread(() -> {
            // Guard check: Ensure a maze has been downloaded first
            if (this.mazePositions == null) {
                SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(
                            this.mainWindowView,
                            "Please request and generate a maze layout before attempting to solve!",
                            "No Maze Loaded",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                });
                return;
            }

            System.out.println("Solving maze using BFS pipeline...");

            // 1. Force stop any running animation before running a new calculation
            if (this.activeAnimator != null) {
                this.activeAnimator.stopAnimation();
            }

            // 2. Initialize the calculation service using the active coordinates grid
            MazeSolver solver = new MazeSolver(this.mazePositions);
            boolean isSolvable = solver.solveBFS();

            // 2. DIALOG BOX FOR NO SOLUTION FOUND
            if (!isSolvable) {
                System.out.println("No solution pathway found for this maze layout config.");
                SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(
                            this.mainWindowView,
                            "This maze is completely trapped! No valid pathway exists from Start to End.",
                            "No Solution Found",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                });
                return;
            }

            // 3. Fallback check for animation delay config value
            int animationDelay = Constants.DEFAULT_ANIMATION_DELAY;
            try {
                animationDelay = this.mainWindowView.getConfigPanel().getAnimationDelayInMs();
            } catch (Exception ex) {
                // fallback remains safely at 80ms
                animationDelay = Constants.DEFAULT_ANIMATION_DELAY;
            }

            if (animationDelay <= 0) {
                animationDelay = Constants.DEFAULT_ANIMATION_DELAY;
            }

            // 4. Initialize and assign our structural animation manager instance field
            this.activeAnimator = new MazeAnimator(
                    this.mazeModel,
                    solver.getHistoryOfSteps(),
                    solver.getFinalPath(),
                    animationDelay,
                    () -> {
                        // This block executes the exact moment the winning path finishes rendering!
                        javax.swing.JOptionPane.showMessageDialog(
                                this.mainWindowView,
                                "The Maze has been solved successfully!",
                                "Solver Finished",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                    }
            );

            // 5. Fire off the visualization ticks safe onto Swing's thread cycle pool
            SwingUtilities.invokeLater(() -> {
                System.out.println("Starting solver route trace animation sequence...");
                this.activeAnimator.startAnimation(false); // DECIDE IF YOU WANT TO DRAW EXPLORATION PHASE
            });
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