package Root.view.ViewUtils;

import Root.core.Constants;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class ViewUtils {
    private ViewUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Border createCustomTitledBorder(String title, boolean isBorderVisible) {
        int borderThickness = 0;
        if (isBorderVisible) {
            borderThickness = 1;
        }
        FlatLineBorder flatLineBorder = new FlatLineBorder(new Insets(1, 1, 1, 1), Color.GRAY, borderThickness, 16);
        Border result = BorderFactory.createTitledBorder(flatLineBorder,
                title,
                2,
                2,
                new Font(Constants.COOL_FONT1_STRING, Font.BOLD, 16),
                Color.WHITE);

        return result;
    }

    /**
     * Helper method to create a clean, solid color box icon inside a JLabel
     */
    public static JLabel createColorBox(Color color, int size) {
        // Create an image in memory and paint it with the chosen color
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);

        // Add a subtle border around the box
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, size - 1, size - 1);
        g.dispose();

        return new JLabel(new ImageIcon(img));
    }

    // Update your class fields declaration to hold a JLabel rather than a JCheckBox
    private JLabel drawGridCheckBoxLabel;

    /**
     * Public setter to update the read-only visual status via your engine/API.
     * This dynamically paints a high-res checked or unchecked box to match your current theme state.
     */
    public static JLabel createDrawGridStatus(boolean drawGrid, int boxSize) {
        JLabel gridCheckBoxLabel = new JLabel();

        // Generate a clean memory canvas matching the theme background layers
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(boxSize, boxSize, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        // Enable high-quality anti-aliased edge smoothing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pick colors that match light or dark styling safely using adaptive runtime layers
        Color boxBorderColor = Color.LIGHT_GRAY;
        Color checkColor = Color.GREEN;
        Color crossColor = Color.RED; // Color for the 'X' cross

        // Draw the checkbox outer box square with slightly rounded corners
        g2.setColor(boxBorderColor);
        g2.drawRoundRect(0, 0, boxSize - 1, boxSize - 1, 6, 6);

        // Set line thickness and smooth caps for vector drawing
        g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int padding = boxSize / 4;

        // If true, paint a clean thick vector check icon inside the container bounds
        if (drawGrid) {
            g2.setColor(checkColor);

            // Draw the checkmark vector paths (relative scale adjustments)
            g2.drawLine(padding, boxSize / 2, boxSize / 2, boxSize - padding);
            g2.drawLine(boxSize / 2, boxSize - padding, boxSize - padding, padding);
        } else {
            // CHANGED: Draws a clean, centered diagonal 'X' cross inside the box border bounds
            g2.setColor(crossColor);

            // Top-Left to Bottom-Right line
            g2.drawLine(padding, padding, boxSize - padding, boxSize - padding);
            // Bottom-Left to Top-Right line
            g2.drawLine(padding, boxSize - padding, boxSize - padding, padding);
        }

        g2.dispose();
        gridCheckBoxLabel.setIcon(new ImageIcon(img));

        return gridCheckBoxLabel;
    }
}