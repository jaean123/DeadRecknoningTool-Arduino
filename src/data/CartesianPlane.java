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

    public ObservableList<XY> getPoints() {
        return points;
    }
}
