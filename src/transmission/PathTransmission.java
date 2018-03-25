package transmission;

import data.CartesianPlane;
import data.XY;
import data.DeadReckoner;

import java.util.ArrayList;

public class PathTransmission implements Transmission {

    CartesianPlane coordinates;

    DeadReckoner deadReckoner;

    public PathTransmission() {
        //deadReckoner = new DeadReckoner(coordinates);
    }

    @Override
    public void processTransmission(ArrayList<Integer> signal) {
        // Get initial point x and y
        XY currentPoint = new XY(signal.get(0), signal.get(1));
        coordinates.getPoints().add(currentPoint);


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
