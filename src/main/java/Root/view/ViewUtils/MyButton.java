package Root.view.ViewUtils;

import Root.core.Constants;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    public MyButton(String text) {
        this.initialize(text);
    }

    private void initialize(String text) {
        // FlatLaf
        this.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        this.setFont(Constants.COOL_FONT1); // Set Font For Button

        this.setText(text);
    }
}