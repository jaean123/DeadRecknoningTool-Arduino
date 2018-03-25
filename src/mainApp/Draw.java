package mainApp;

import data.CartesianPlane;
import data.XY;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import transmission.EncoderTransmission;

/**
 * This class draws the path on the GUI using the encoder count data.
 */
public class Draw {

    private CartesianPlane actualPath, desiredPath;
    private Pane pathPane;
    private ObservableList<XY> locationHistory;
    double scaleFactor;
    Color lineColor;

    public Draw(Pane pathPane, EncoderTransmission encoderTransmission, Color lineColor) {
        this.pathPane = pathPane;
        this.locationHistory = encoderTransmission.getDeadReckoner().getPlane().getPoints();
        this.lineColor = lineColor;
    }

    public void startDrawing() {
        attachLocationChangeListener();
    }

    /**
     * Listens for changes on the location history data and updates the path drawn.
     */
    private void attachLocationChangeListener() {
         // Listen for changes on the encoder count data.
        locationHistory.addListener((ListChangeListener)(c -> {
            // Add the latest location to the polyline.

        }));
    }

    public void changeScale(double scaleFactor) {

    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
