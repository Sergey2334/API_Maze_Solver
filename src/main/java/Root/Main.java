package Root;

import Root.controller.MazeController;
import Root.core.MyUtils;
import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World :D");
        long startTime = System.currentTimeMillis();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatDarkLaf.setup();

        // Install the inspector windows explicitly
        FlatInspector.install("ctrl shift alt X");
        FlatUIDefaultsInspector.install("ctrl shift alt Y");

        // Different Main Window Colors :D
//        FlatOneDarkIJTheme.setup();
//        FlatMTAtomOneDarkIJTheme.setup();
//        FlatNordIJTheme.setup();
//        FlatDarkPurpleIJTheme.setup();

        // Runs The Application
        SwingUtilities.invokeLater(() -> {
            MazeController controller = new MazeController();
        });

        // HTTP Tests
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(Constants.GET_MAZE_CONFIG_URL).get().build();
//        Response response = null;
//        try {
//            response = okHttpClient.newCall(request).execute();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Response code: " + response.body());


        // Register a hook that runs right before the JVM completely shuts down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            java.time.LocalTime finishTime = java.time.LocalTime.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println("Whole program finished at: " + finishTime.format(formatter));

            long endTime = System.currentTimeMillis();
            System.out.println("Run Time: " + MyUtils.formatMStoHHMMSSMS(endTime - startTime));
        }));
    }
}