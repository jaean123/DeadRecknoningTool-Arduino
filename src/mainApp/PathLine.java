package mainApp;

import data.CartesianPlane;
import data.XY;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class PathLine extends Polyline {

    private CartesianPlane path;

    public PathLine(CartesianPlane path, Color lineColor) {
        this.path = path;
        setStroke(lineColor);
        attachDataChangeListener();
    }

    private void refreshDrawing() {
        getPoints().clear();
        ObservableList<XY> pointsToDraw = path.getPoints();
        for (int i = 0; i < pointsToDraw.size(); i++) {
            XY p = pointsToDraw.get(i);
            double x = p.getX();
            double y = p.getY();
            getPoints().addAll(x, y);
        }
    }

    private void attachDataChangeListener() {
        // Listen for changes on coordinate data to be drawn.
        ObservableList<XY> pointsToDraw = path.getPoints();
        pointsToDraw.addListener((ListChangeListener)(c -> {
            // Add the latest location to the polyline.
            if (c.wasAdded() && c.getAddedSize() == 1) {
                XY latest = pointsToDraw.get(pointsToDraw.size());
                double x = latest.getX();
                double y = latest.getY();
                getPoints().addAll(x, y);
            }
            else {
                refreshDrawing();
            }
        }));
    }
}
