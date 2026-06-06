package Root.service;

import Root.model.Maze;
import Root.model.Position;
import javax.swing.Timer;
import java.util.List;

public class MazeAnimator {
    private final Maze mazeCanvas;
    private final List<Position> executionSteps;
    private final List<Position> correctPath;
    private final int delay;

    private Timer playbackTimer;
    private int currentStepIndex;
    private int currentPathIndex;
    private boolean isExplorationPhase;

    // NEW: Callback to notify the controller when the animation finishes
    private final Runnable onCompletionCallback;

    public MazeAnimator(Maze mazeCanvas, List<Position> executionSteps, List<Position> correctPath, int delay, Runnable onCompletionCallback) {
        this.mazeCanvas = mazeCanvas;
        this.executionSteps = executionSteps;
        this.correctPath = correctPath;
        this.delay = delay;
        this.onCompletionCallback = onCompletionCallback;
        this.currentStepIndex = 0;
        this.currentPathIndex = 0;
        this.isExplorationPhase = true;
    }

    /**
     * Spawns a dedicated Swing Timer to safely feed the solution coordinates
     * back into the UI thread sequentially at the specified delay intervals.
     */
    public void startAnimation(boolean isExplorationPhase) {
        this.currentStepIndex = 0;
        this.currentPathIndex = 0;
        this.isExplorationPhase = isExplorationPhase;

        // Clean out any leftover color flags from previous path solution runs
        this.resetMazeGridStates();

        // Instantiate a single Swing Timer cycle running on your config delays
        this.playbackTimer = new Timer(this.delay, e -> {
            this.handleAnimationTick();
        });

        this.playbackTimer.start();
    }

    /**
     * Stops the animation runner prematurely if needed (e.g. if the user clears the screen)
     */
    public void stopAnimation() {
        if (this.playbackTimer != null && this.playbackTimer.isRunning()) {
            this.playbackTimer.stop();
        }
    }

    private void handleAnimationTick() {
        // --- PHASE 1: Animate Search/Exploration Step ---
        if (this.isExplorationPhase) {
            if (this.currentStepIndex < this.executionSteps.size()) {
                Position step = this.executionSteps.get(this.currentStepIndex);
                if (step != null) {
                    step.setIsVisited(true);
                    this.mazeCanvas.repaint(); // Triggers your custom centered paintComponent loop
                }
                this.currentStepIndex++;
            } else {
                // Exploration is completely finished, flip into tracing the actual winner path
                this.isExplorationPhase = false;

                // Speed up the final path reveal slightly (half the delay time) for punchy feedback
                this.playbackTimer.setDelay(Math.max(5, this.delay / 2));
            }
        }
        // --- PHASE 2: Animate the Winning Path Step ---
        else {
            if (this.currentPathIndex < this.correctPath.size()) {
                Position pathNode = this.correctPath.get(this.currentPathIndex);
                if (pathNode != null) {
                    pathNode.setIsFinalPath(true);
                    this.mazeCanvas.repaint();
                }
                this.currentPathIndex++;
            } else {
                // Entire playback sequence fully rendered
                this.playbackTimer.stop();
                System.out.println("Maze solution animation playback fully completed!");

                if (this.onCompletionCallback != null) {
                    this.onCompletionCallback.run();
                }
            }
        }
    }

    /**
     * Loops through the active animation step matrices to scrub clean previous states
     */
    private void resetMazeGridStates() {
        if (this.executionSteps != null) {
            for (Position p : this.executionSteps) {
                if (p != null) {
                    p.resetSolverStates();
                }
            }
        }
        if (this.correctPath != null) {
            for (Position p : this.correctPath) {
                if (p != null) {
                    p.resetSolverStates();
                }
            }
        }
        this.mazeCanvas.repaint();
    }
}