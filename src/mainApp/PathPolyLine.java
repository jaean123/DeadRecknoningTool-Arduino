package mainApp;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class PathPolyLine extends Polyline {

    private XYChart.Series<Double, Double> path;
    private double scaleFactor;

    public PathPolyLine(XYChart.Series<Double, Double> path, Color lineColor) {
        this.path = path;
        this.scaleFactor = 1;
        setStroke(lineColor);
        refreshDrawing();
        attachDataChangeListener();
        translateToPath();
    }

    public void translateToPath() {
        /*double x = getPoints().get(getPoints().size()-2).doubleValue();
        double y = getPoints().get(getPoints().size()-1).doubleValue();
        setTranslateX(x);
        setTranslateY(y);*/
    }

    /**
     * Refreshes the drawing with the correct scale factor.
     */
    private void refreshDrawing() {
        getPoints().clear();
        ObservableList<XYChart.Data<Double, Double>> pointsToDraw = path.getData();
        for (int i = 0; i < pointsToDraw.size(); i++) {
            XYChart.Data<Double, Double> p = pointsToDraw.get(i);
            double x = p.getXValue()*scaleFactor;
            double y = p.getYValue()*scaleFactor;
            getPoints().addAll(x, y);
//            System.out.println("(" + x + ", " + y + ")");
        }
    }

    /**
     * Attaches listener to listen for changes on the CartesianPlane path.
     * When changes are detected, the PathPolyLine updates accordingly.
     */
    private void attachDataChangeListener() {
        // Listen for changes on coordinate data to be drawn.
        ObservableList<XYChart.Data<Double, Double>> pointsToDraw = path.getData();
        pointsToDraw.addListener((ListChangeListener)(c -> {
            while (c.next()) {
                // Add the latest location to the polyline.
                if (c.wasAdded() && c.getAddedSize() == 1) {
                    XYChart.Data<Double, Double> latest = (XYChart.Data<Double, Double>)c.getAddedSubList().get(0);
                    double x = latest.getXValue()*scaleFactor;
                    double y = latest.getYValue()*scaleFactor;
                    getPoints().addAll(x, y);
                    System.out.println("(" + x + ", " + y + ")");
                }
                else {
                    refreshDrawing();
                }
            }
        }));
    }

    /**
     * Sets the scale factor and applies the scaling by calling refreshDrawing().
     * @param scaleFactor
     */
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor > 0.05 ? scaleFactor : 0.05;
        refreshDrawing();
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
