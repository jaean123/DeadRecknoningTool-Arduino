package mainApp;

import javafx.scene.paint.Color;

/**
 * Class consisting of helper methods and such.
 * todo: add this to GIT
 */
public class Helpers {
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public static Color hexToRGB(String colorStr) {
        java.awt.Color awtColor = java.awt.Color.decode("#FFCCEE");
        int red = awtColor.getRed();
        int green = awtColor.getGreen();
        int blue = awtColor.getBlue();
        return Color.color(red, green, blue);
    }
}
