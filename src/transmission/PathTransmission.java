package transmission;

import data.DeadReckoner;
import javafx.scene.chart.XYChart;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PathTransmission implements Transmission {

    public static char START_TRANSMISSION = 'p';
    public static int BYTES_TO_READ = 8;

    private static final double THRESHOLD = 0.01;

    private XYChart.Series<Double, Double> series;

    private XYChart.Data<Double, Double> prevLocation;

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
        byte[] xAry = Transmission.objectToByteArray(signal.subList(0, 4).toArray());
        byte[] yAry = Transmission.objectToByteArray(signal.subList(4, 8).toArray());
        double x = ByteBuffer.wrap(xAry).getFloat();
        double y = ByteBuffer.wrap(yAry).getFloat();
        XYChart.Data<Double, Double> loc = new XYChart.Data<>(x, y);

//        System.out.println("x: " + x + ", y: " + y);

        if (!locationEquals(prevLocation, loc))
            series.getData().add(loc);

        prevLocation = loc;


        /*for (int i = 3; i < signal.length()-1; i++) { TODO figure out and re-implement.
            // Subsequent data for are delta x and delta y values
            double x = currentPoint.getX() + (int)signal.charAt(i);
            double y = currentPoint.getY() + (int)signal.charAt(i+1);
            XY coord = new XY(x, y);
            coordinates.getPoints().add(coord);
            currentPoint = coord;
        }*/
    }

    public static <T> boolean locationEquals(XYChart.Data<T, T> p1, XYChart.Data<T, T> p2) {
        if (p1 == null || p2 == null)
            return false;

        double x1 = ((Number)p1.getXValue()).doubleValue();
        double x2 = ((Number)p2.getXValue()).doubleValue();
        double y1 = ((Number)p1.getYValue()).doubleValue();
        double y2 = ((Number)p2.getYValue()).doubleValue();

        if (Math.abs(x2 - x1) < THRESHOLD && Math.abs(y2 - y1) < THRESHOLD)
            return true;

        return false;
    }
}
