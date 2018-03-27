package mainApp;

import data.CartesianPlane;
import data.XY;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * This class draws the path on the GUI using the encoder count data.
 */
public class Draw {

    private CartesianPlane path;
    private ObservableList<Double> polyPoints;
    private Polyline polyline;

    // Scale factor scales the points drawn for zooming purposes.
    private double scaleFactor;
    private double transX;
    private double transY;
    private Color lineColor;

    /**
     * Constructor
     * @param pathPane JavaFX pane to draw on.
     * @param path CartesianPlane points to draw.
     * @param lineColor Color of the Polyline drawn.
     */
    public Draw(Pane pathPane, CartesianPlane path, Color lineColor) {
        this.path = path;
        this.lineColor = lineColor;
        this.polyline = new Polyline();
        this.polyline.setStroke(lineColor);
        this.polyPoints = polyline.getPoints();
        this.scaleFactor = 1;
        this.transX = 0;
        this.transY = 0;

        pathPane.getChildren().add(polyline);
        refreshDrawing();
        attachPlaneListener();

        double width = pathPane.getPrefWidth();
        double height = pathPane.getPrefHeight();

        // Translate so that the path drawing does not start at the edge, but at the center of the path pane.
        translate(width/2, height/2);
    }

    private void refreshDrawing() {
        polyPoints.clear();
        ObservableList<XY> pointsToDraw = path.getPoints();
        for (int i = 0; i < pointsToDraw.size(); i++) {
            XY p = pointsToDraw.get(i);
            double x = p.getX()*scaleFactor + transX;
            double y = p.getY()*scaleFactor + transY;
            polyPoints.addAll(x, y);
        }
    }

    /**
     * Listens for new points added on the location history data and updates the path drawn.
     * NOTE: Code does not work for other changes such as changing an existing point.
     */
    private void attachPlaneListener() {
        // Listen for changes on coordinate data to be drawn.
        ObservableList<XY> pointsToDraw = path.getPoints();
        pointsToDraw.addListener((ListChangeListener)(c -> {
            // Add the latest location to the polyline.
            if (c.wasAdded() && c.getAddedSize() == 1) {
                XY latest = pointsToDraw.get(pointsToDraw.size());
                double x = latest.getX()*scaleFactor;
                double y = latest.getY()*scaleFactor;
                polyPoints.addAll(x, y);
            }
            else {
                refreshDrawing();
            }
        }));
    }

    /**
     * Translates the Polyline drawing by dX and dY.
     * @param dX
     * @param dY
     */
    public void translate(double dX, double dY) {
        double prevTransX = transX;
        double prevTransY = transY;
        transX += dX;
        transY += dY;
        refreshDrawing();
    }

    /**
     * Changes the scale of the Polyline drawn.
     * Used for zooming purposes
     * @param scaleFactor
     */
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;

        // Find xmax, ymax
        double xmax = 0, ymax = 0, x, y;
        for (int i = 0; i < polyPoints.size()-1; i += 2) {
            x = polyPoints.get(i);
            y = polyPoints.get(i+1);
            if (x > xmax) xmax = x;
            if (y > ymax) ymax = y;
        }

        double dX = xmax - xmax*scaleFactor;
        double dY = ymax - ymax*scaleFactor;

        // Refresh drawing to apply scale factor.
        refreshDrawing();

        // Translate so that the center of the square bounded by the polyline remains at the original location.
        // TODO Fix this
        //translate(dX, dY);
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Clears the poly points.
     */
    public void clear() {
        polyPoints.clear();
    }
}
