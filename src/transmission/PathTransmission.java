package transmission;

import data.PathData;
import data.Coordinate;

public class PathTransmission implements Transmission {

    Coordinate currentPoint;
    PathData coordinates;

    @Override
    public void processTransmission(String input) {
        // Get initial point x and y
        currentPoint = new Coordinate(input.charAt(1), input.charAt(2));
        coordinates.getPoints().add(currentPoint);

        for (int i = 3; i < input.length()-1; i++) {
            // Subsequent data for are delta x and delta y values
            int x = currentPoint.getX() + (int)input.charAt(i);
            int y = currentPoint.getY() + (int)input.charAt(i+1);
            Coordinate coord = new Coordinate(x, y);
            coordinates.getPoints().add(coord);
            currentPoint = coord;
        }

        // Notify program to draw

    }

    public PathData getCoordinates() {
        return coordinates;
    }
}
