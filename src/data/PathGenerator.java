package data;

import javafx.scene.chart.XYChart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Generates infinity symbol in cartesian plane.
 */
public class PathGenerator {

    public static void main(String[] args) {
        DecimalFormat df1 = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0");
        ArrayList<XYChart.Data<Number, Number>> infPoints = getInfinityPoints(5, 600);
        ArrayList<XYChart.Data<Number, Number>> circlePoints = getCirclePoints(10*Math.PI/180, 600);
//        System.out.println(commaFormat(infPoints, df2));
//        System.out.println(tableFormat(infPoints, df1));
        System.out.println(commaFormat(circlePoints, df2));
//        System.out.println(tableFormat(circlePoints, df2));
    }

    public static String commaFormat(ArrayList<XYChart.Data<Number, Number>> points, NumberFormat ft) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            XYChart.Data pt = points.get(i);
            sb.append(ft.format(pt.getXValue()) + ", " + ft.format(pt.getYValue()));
            if (!(i + 1 == points.size())) sb.append(", ");
            if ((i + 1) % 5 == 0) sb.append("\n");
        }
        return sb.toString();
    }

    public static String tableFormat(ArrayList<XYChart.Data<Number, Number>> points, NumberFormat ft) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            XYChart.Data pt = points.get(i);
            sb.append(ft.format(pt.getXValue()) + "\t" + ft.format(pt.getYValue()));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static ArrayList<XYChart.Data<Number, Number>> getInfinityPoints(double step, double a) {
        ArrayList<XYChart.Data<Number, Number>> points = new ArrayList<>();
        double degToRad = Math.PI / 180;
        for (int t = 0; t < 360; t += step) {
            double x = a * Math.sqrt(2) * Math.cos(t * degToRad) / (Math.sin(t * degToRad) * Math.sin(t * degToRad) + 1);
            double y = a * Math.sqrt(2) * Math.cos(t * degToRad) * Math.sin(t * degToRad)
                    / (Math.sin(t * degToRad) * Math.sin(t * degToRad) + 1);
            XYChart.Data pt = new XYChart.Data(x, y);
            points.add(pt);
        }
        return points;
    }

    public static ArrayList<XYChart.Data<Number, Number>> getCirclePoints(double step, double r) {
        ArrayList<XYChart.Data<Number, Number>> points = new ArrayList<>();
        for (double theta = 0; theta < 2*Math.PI; theta += step) {
            double x = r*Math.cos(theta);
            double y = r*Math.sin(theta);
            points.add(new XYChart.Data(x, y));
        }
        return points;
    }
}
