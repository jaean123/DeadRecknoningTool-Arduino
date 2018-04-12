package transmission;

import data.DeadReckoner;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class PathTransmission implements Transmission {

    private XYChart.Series<Double, Double> series;

    private DeadReckoner deadReckoner;

    public PathTransmission() {
        //deadReckoner = new DeadReckoner(coordinates);
        series = new XYChart.Series<>();
    }

    public XYChart.Series<Double, Double> getSeries() {
        return series;
    }

    @Override
    public void processTransmission(ArrayList signal) {
        // Get initial point x and y
        double x = (Double) signal.get(0);
        double y = (Double) signal.get(1);
        series.getData().add(new XYChart.Data<>(x, y));
//        System.out.println("x: " + x + ", y: " + y);

        /*for (int i = 3; i < signal.length()-1; i++) { TODO figure out and re-implement.
            // Subsequent data for are delta x and delta y values
            double x = currentPoint.getX() + (int)signal.charAt(i);
            double y = currentPoint.getY() + (int)signal.charAt(i+1);
            XY coord = new XY(x, y);
            coordinates.getPoints().add(coord);
            currentPoint = coord;
        }*/
    }
}
