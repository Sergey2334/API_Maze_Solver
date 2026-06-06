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

    public MyButton getCheckSolutionButton() {
        return this.checkSolutionButton;
    }
}