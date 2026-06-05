package Root.core;

import java.awt.*;

public final class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String COOL_FONT1_STRING = "Trebuchet MS";
    public static final Font COOL_FONT1 = new Font(COOL_FONT1_STRING, Font.BOLD, 20);

    public static final int COLOR_BOX_SIZE = 35;

    public static final int MIN_MAZE_SIZE = 5;
    public static final int MAX_MAZE_SIZE = 100;
    public static final int DEFAULT_MAZE_SIZE = 30;
    public static final int DEFAULT_ANIMATION_DELAY = 80;

    public static final String GET_MAZE_CONFIG_URL = "https://backend-qcf9.onrender.com/fm1/get-render-config";
    public static final String GET_MAZE_IMAGE_URL = "https://backend-qcf9.onrender.com/fm1/get-maze-image";
}
