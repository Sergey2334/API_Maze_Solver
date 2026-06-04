package Root.view.MainWindow;

import Root.view.ConfigPanel.ConfigPanel;
import Root.view.MazePanel.MazePanel;
import com.formdev.flatlaf.extras.components.FlatSeparator;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private MazePanel mazePanel;
    private ConfigPanel configPanel;

    public MainWindow() {
        this.initialize();
        this.initializeComponents();
    }

    private void initialize() {
        this.setTitle("API Maze Solver");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLayout(new BorderLayout());

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initializeComponents() {
        this.mazePanel = new MazePanel();
        this.configPanel = new ConfigPanel();

        this.add(new FlatSeparator(), BorderLayout.NORTH);
        this.add(this.mazePanel, BorderLayout.CENTER);
        this.add(this.configPanel, BorderLayout.SOUTH);
        this.initializeImageIcon();
    }

    // --- Load and Set Window Icon Image ---
    private void initializeImageIcon() {
        try {
            java.net.URL iconURL = getClass().getResource("/maze-icon.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                this.setIconImage(icon.getImage());
            } else {
                System.err.println("Could not find maze-icon.png resource file!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Getters ---
    public MazePanel getMazePanel() {
        return this.mazePanel;
    }

    public ConfigPanel getConfigPanel() {
        return this.configPanel;
    }
}