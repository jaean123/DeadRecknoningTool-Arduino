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

    @Override
    public void processTransmission(ArrayList<Integer> signal) {
        // Get initial point x and y
        double x = signal.get(0);
        double y = signal.get(1);
        series.getData().add(new XYChart.Data<>(x, y));


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
