package mainApp;

import data.CartesianPlane;
import data.XY;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Vector;

/**
 * This line chart updates real time as the data changes.
 */
public class RealtimeChart extends Region {

    private Vector<ObservableList<XYChart.Series<Number, Number>>> seriesPlotted;
    private ObservableList<CartesianPlane> seriesList;
    private LineChart<Number, Number> chart;
    private XYChart.Series series;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Pane plotPane;

    public void RealtimeChart(Pane plotPane, ObservableList<CartesianPlane> seriesList) {
        this.plotPane = plotPane;
        this.seriesList = seriesList;
        attachSeriesListListener();
        attachSeriesListeners(seriesList);
    }

    /**
     * Adds listener to listen for changes on the list of series.
     */
    private void attachSeriesListListener() {
        // Add change listener to listen for any changes in the list of series.
        seriesList.addListener((ListChangeListener) c -> {
            // A data series has been added. Add this series to listen for change events.
            if (c.wasAdded()) {
                List<CartesianPlane> subList = c.getAddedSubList();
                attachSeriesListeners(subList);
                refreshChart();
            } else if (c.wasRemoved()) {
                refreshChart();
            }
        });
    }

    /**
     * Adds listeners on individual series.
     *
     * @param lists
     */
    private void attachSeriesListeners(List<CartesianPlane> lists) {
        for (int i = 0; i < lists.size(); i++) {
            ObservableList<XY> currentSeries = lists.get(i).getPoints();
            currentSeries.addListener((ListChangeListener) c -> updateSingleSeries(c));
        }
    }

    public void updateSingleSeries(ListChangeListener.Change c) {

    }

    private void refreshChart() {

    }

    private void addLineChart() {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stock Monitoring, 2010");

        // Defining a series.
        series = new XYChart.Series();
        series.setName("My Portfolio");

        // Populate series with data.
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        lineChart.getData().add(series);
        plotPane.getChildren().add(lineChart);


        lineChart.setAnimated(true);
        xAxis.setAutoRanging(false);
    }

    public void doDebug() {
        series.getData().add(new XYChart.Data(15, 34));
        series.getData().add(new XYChart.Data(16, 36));
        series.getData().add(new XYChart.Data(17, 22));
        series.getData().add(new XYChart.Data(18, 45));
        xAxis.setLowerBound(5);
    }
}
