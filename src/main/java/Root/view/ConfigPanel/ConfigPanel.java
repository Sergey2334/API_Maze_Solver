package Root.view.ConfigPanel;

import Root.core.Constants;
import Root.model.MazeConfig;
import Root.view.ViewUtils.MyButton;
import Root.view.ViewUtils.ViewUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ConfigPanel extends JPanel {
    private JPanel wallCellColorConfigLabel;
    private JPanel pathColorConfigLabel;
    private JPanel gridColorConfigLabel;
    private JPanel drawGridConfigLabel;
    private JPanel animationInMsConfigLabel;
    private MyButton refreshConfigButton;

    private JPanel widthTextFieldPanel;
    private JPanel heightTextFieldPanel;
    private MyButton getMazeButton;

    // Converted to class fields so your methods can access and update them later
    private JLabel drawGridCheckBox;
    private JLabel delayLabel;
    private JTextField widthTextField;
    private JTextField heightTextField;

    private int animationDelayInMs;

    public ConfigPanel() {
        this.initialize();
        this.initializeComponents();
    }

    private void initialize() {
        this.setLayout(new MigLayout("fillx, insets 20", "[grow][grow][grow][grow][grow][220px!, right]"));

        Border border = ViewUtils.createCustomTitledBorder("Config Panel", true);
        this.setBorder(border);
    }

    private void initializeComponents() {
        this.createComponents();
        this.createBorderForComponents();
        this.addNestedComponents();
        this.addComponentsToPanel();
    }

    // Public setter to update the API label text dynamically
    public void setAnimationDelayText(String delay) {
        this.delayLabel.setText(delay);
    }

    private void createComponents() {
        // Row 1
        this.wallCellColorConfigLabel = new JPanel(new MigLayout("fill, insets 2"));
        this.pathColorConfigLabel = new JPanel(new MigLayout("fill, insets 2"));
        this.gridColorConfigLabel = new JPanel(new MigLayout("fill, insets 2"));
        this.drawGridConfigLabel = new JPanel(new MigLayout("fill, insets 2"));
        this.animationInMsConfigLabel = new JPanel(new MigLayout("fill, insets 2"));
        this.refreshConfigButton = new MyButton("REFRESH CONFIGURATIONS");

        // Row 2
        this.widthTextFieldPanel = new JPanel(new MigLayout("fill, insets 2"));
        this.heightTextFieldPanel = new JPanel(new MigLayout("fill, insets 2"));
        this.getMazeButton = new MyButton("GET MAZE");
    }

    private void createBorderForComponents() {
        // Row 1
        this.wallCellColorConfigLabel.setBorder(ViewUtils.createCustomTitledBorder("Wall Cell Color", false));
        this.pathColorConfigLabel.setBorder(ViewUtils.createCustomTitledBorder("Path Color", false));
        this.gridColorConfigLabel.setBorder(ViewUtils.createCustomTitledBorder("Grid Color", false));
        this.drawGridConfigLabel.setBorder(ViewUtils.createCustomTitledBorder("Draw Grid", false));
        this.animationInMsConfigLabel.setBorder(ViewUtils.createCustomTitledBorder("Animation Delay", false));

        // Row 2
        this.widthTextFieldPanel.setBorder(ViewUtils.createCustomTitledBorder("Width", false));
        this.heightTextFieldPanel.setBorder(ViewUtils.createCustomTitledBorder("Height", false));
    }

    private void addNestedComponents() {
        // Color Boxes
        this.wallCellColorConfigLabel.add(ViewUtils.createColorBox(Color.RED, Constants.COLOR_BOX_SIZE), "push, align center");
        this.pathColorConfigLabel.add(ViewUtils.createColorBox(Color.GREEN, Constants.COLOR_BOX_SIZE), "push, align center");
        this.gridColorConfigLabel.add(ViewUtils.createColorBox(Color.BLUE, Constants.COLOR_BOX_SIZE), "push, align center");

        // Draw Grid Status
        this.drawGridCheckBox = ViewUtils.createDrawGridStatus(true, Constants.COLOR_BOX_SIZE);
        this.drawGridConfigLabel.add(this.drawGridCheckBox, "push, align center");

        // Animation Delay
        this.delayLabel = new JLabel("80" + " ms");
        this.delayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font currentFont = this.delayLabel.getFont();
        this.delayLabel.setFont(currentFont.deriveFont((float) Constants.COLOR_BOX_SIZE));
        this.animationInMsConfigLabel.add(this.delayLabel, "grow x, push x, align center");

        // Width Text Field
        this.widthTextField = new JTextField();
        this.widthTextField.putClientProperty("JTextField.placeholderText", "Enter Maze Width (e.g. 30)");
        this.widthTextFieldPanel.add(this.widthTextField, "grow x, push x");

        // Height Text Field
        this.heightTextField = new JTextField();
        this.heightTextField.putClientProperty("JTextField.placeholderText", "Enter Maze Height (e.g. 30)");
        this.heightTextFieldPanel.add(this.heightTextField, "grow x, push x");
    }

    private void addComponentsToPanel() {
        // Row 1
        this.add(this.wallCellColorConfigLabel, "grow x");
        this.add(this.pathColorConfigLabel, "grow x");
        this.add(this.gridColorConfigLabel, "grow x");
        this.add(this.drawGridConfigLabel, "grow x");
        this.add(this.animationInMsConfigLabel, "grow x");
        this.add(this.refreshConfigButton, "grow, wrap");

        // Row 2
        this.add(this.widthTextFieldPanel, "span 2, grow x");
        this.add(this.heightTextFieldPanel, "span 3, grow x");
        this.add(this.getMazeButton, "grow");
    }

    public void setMazeConfig(MazeConfig mazeConfig) {
        if (mazeConfig == null) return;

        // 1. Update the color boxes (Remove old ones, add fresh ones matching model)
        this.wallCellColorConfigLabel.removeAll();
        this.wallCellColorConfigLabel.add(ViewUtils.createColorBox(mazeConfig.getWallCellColor(), Constants.COLOR_BOX_SIZE), "push, align center");

        this.pathColorConfigLabel.removeAll();
        this.pathColorConfigLabel.add(ViewUtils.createColorBox(mazeConfig.getPathColor(), Constants.COLOR_BOX_SIZE), "push, align center");

        this.gridColorConfigLabel.removeAll();
        this.gridColorConfigLabel.add(ViewUtils.createColorBox(mazeConfig.getGridColor(), Constants.COLOR_BOX_SIZE), "push, align center");

        // 2. Update the Checkmark / Cross status graphic label
        this.drawGridConfigLabel.removeAll();
        this.drawGridCheckBox = ViewUtils.createDrawGridStatus(mazeConfig.isGridExist(), Constants.COLOR_BOX_SIZE);
        this.drawGridConfigLabel.add(this.drawGridCheckBox, "push, align center");

        // 3. Update the text string for the Animation Delay label
        this.animationDelayInMs = mazeConfig.getAnimationDelayInMs(); // PATCH
        this.delayLabel.setText(this.animationDelayInMs + " ms");

        // 4. Update Width And Height Text Field, In Case Of An Invalid Input
        this.widthTextField.setText(Integer.toString(mazeConfig.getWidth()));
        this.heightTextField.setText(Integer.toString(mazeConfig.getHeight()));

        // CRITICAL SWING STEP: Forces the window to recalculate internal alignments and update immediately on screen
        this.revalidate();
        this.repaint();
    }

    // --- Getters ---
    public MyButton getRefreshConfigButton() {
        return this.refreshConfigButton;
    }

    public MyButton getMazeButton() {
        return this.getMazeButton;
    }

    public String getWidthText() {
        return this.widthTextField.getText();
    }

    public String getHeightText() {
        return this.heightTextField.getText();
    }

    public int getAnimationDelayInMs() {
        return this.animationDelayInMs;
    }
}