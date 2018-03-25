package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * This class represents a plane in cartesian coordinate system.
 * In this app, it is used as a map for robot location.
 */
public class CartesianPlane {

    ObservableList<XY> points;

    public CartesianPlane() {
        this.points = FXCollections.observableList(new ArrayList<XY>());
    }

    public CartesianPlane(ObservableList<XY> points) {
        this.points = points;
    }

    public ObservableList<XY> getPoints() {
        return points;
    }

    /**
     * Scale the coordinates for zooming purposes.
     * @param scaleFactor
     */
    public void setScale(double scaleFactor) {
        for (int i = 0; i < points.size(); i++) {
            XY p = points.get(i);
            double newX = p.getX()*scaleFactor;
            double newY = p.getY()*scaleFactor;
            points.set(i, new XY(newX, newY));
        }
    }
}
