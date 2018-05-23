package data;

import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;

public class GlobalConstants {

    // PROGRAM PARAMETERS
    public static final String VERSION = "1.0.0";
    public static final String APP_ICON_LOCATION = "./resources/icon.png";
    public static final String STYLESHEET_CUSTOM = "./resources/styles.css";
    public static final String STYLESHEET_BOOTSTRAP = "./resources/bootstrap3.css";
    public static final String CUSTOM_FONT = "./resources/robotoFont/Roboto-Regular.ttf";

    // ROBOT PARAMETERS
    public static final double WHEEL_RADIUS = 0;

    // TEXT CONSTANTS
    public static final String APP_NAME = "Dead Reckoning Tool";
    public static final String ABOUT_TEXT = "ArduinoPath Serial" +
                    "\nVersion: " + VERSION +
                    "\nDeveloped by: Jae An" +
                    "\nEmail: jaean37@gmail.com";

    // BUTTON LABELS AND TOOLTIPS
    public static final String ENTER_PORT =  "Enter Port: ";
    public static final String OPEN_SERIAL = "Open Serial";
    public static final String OPEN_SERIAL_TOOLTIP = "Open serial communication at COM port";
    public static String TRANSLATE_TO_PATH = "T";
    public static String ABOUT_TRANSLATE_TO_PATH = "Show path starting point";
    public static final String ZOOM_IN = "+";
    public static final String ZOOM_IN_TOOLTIP = "Zoom In (+ key)";
    public static final String ZOOM_OUT = "-";
    public static final String ZOOM_OUT_TOOLTIP = "Zoom Out (- key)";
    public static final String ABOUT = "About";
    public static final String ABOUT_TOOLTIP = "About Program";
    public static final String CLEAR = "Clear";
    public static final String CLEAR_TOOLTIP = "Clear path drawing and plots";
//    public static final String ;

    // COLORS
    public static final Color ACTUAL_PATH_LINE_COLOR = Color.BLACK;
    public static final Color TARGET_PATH_LINE_COLOR = Color.BLUE;
    public static final Color PATH_PANE_BACKGROUND_COLOR = Color.WHITE;

    public static String getFilePath(String location) {
        String filePath = null;
        try {
            filePath = (new File(location).toURI().toURL().toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
