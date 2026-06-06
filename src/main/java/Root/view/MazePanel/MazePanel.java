package Root.view.MazePanel;

import Root.model.Maze;
import Root.view.ViewUtils.MyButton;
import Root.view.ViewUtils.ViewUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MazePanel extends JPanel {
    private Maze mazeDrawing;
    private MyButton checkSolutionButton;

    public MazePanel(Maze maze) {
        this.initialize();
        this.initializeComponents(maze);
    }

    private void initialize() {
//        // 1. "fill" stretches the whole grid vertically and horizontally
//        String layoutConstraints = "fill, insets 0";
//
//        // 2. Lock columns into exact proportions: 35%, 30%, 35%
//        String columnConstraints = "[35%, fill, grow][30%, fill, grow][35%, fill, grow]";
//
//        // 3. Stretches the row vertically to fill the full height
//        String rowConstraints = "[fill]";
//
//        this.setLayout(new MigLayout(layoutConstraints, columnConstraints, rowConstraints));
        this.setLayout(new MigLayout("wrap 1, fill"));
        this.setBorder(ViewUtils.createCustomTitledBorder("Maze Panel", true));
    }

    private void initializeComponents(Maze maze) {
        this.mazeDrawing = maze;
//        this.mazeDrawing.setBorder(ViewUtils.createCustomTitledBorder("Maze Drawing", true));
        this.checkSolutionButton = new MyButton("CHECK SOLUTION");

        this.add(mazeDrawing, "grow, push");
        this.add(checkSolutionButton, "align center");
    }
}