package mainApp;

import data.CartesianPlane;
import data.XY;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class PathPolyLine extends Polyline {

    private CartesianPlane path;
    private double scaleFactor;

    public PathPolyLine(CartesianPlane path, Color lineColor) {
        this.path = path;
        this.scaleFactor = 1;
        setStroke(lineColor);
        refreshDrawing();
        attachDataChangeListener();
    }

    /**
     * Refreshes the drawing with the correct scale factor.
     */
    private void refreshDrawing() {
        getPoints().clear();
        ObservableList<XY> pointsToDraw = path.getPoints();
        for (int i = 0; i < pointsToDraw.size(); i++) {
            XY p = pointsToDraw.get(i);
            double x = p.getX()*scaleFactor;
            double y = p.getY()*scaleFactor;
            getPoints().addAll(x, y);
        }
    }

    /**
     * Attaches listener to listen for changes on the CartesianPlane path.
     * When changes are detected, the PathPolyLine updates accordingly.
     */
    private void attachDataChangeListener() {
        // Listen for changes on coordinate data to be drawn.
        ObservableList<XY> pointsToDraw = path.getPoints();
        pointsToDraw.addListener((ListChangeListener)(c -> {
            // Add the latest location to the polyline.
            if (c.wasAdded() && c.getAddedSize() == 1) {
                XY latest = pointsToDraw.get(pointsToDraw.size());
                double x = latest.getX()*scaleFactor;
                double y = latest.getY()*scaleFactor;
                getPoints().addAll(x, y);
            }
            else {
                refreshDrawing();
            }
        }));
    }

    /**
     * Sets the scale factor and applies the scaling by calling refreshDrawing().
     * @param scaleFactor
     */
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor > 1 ? scaleFactor : 1;
        refreshDrawing();
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
