package mainApp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * This line chart updates real time as the data changes.
 */
public class RealtimeChart extends Region {

    private ObservableList<XYChart.Series<Number, Number>> seriesList;

    private LineChart<Number, Number> chart;

    private NumberAxis xAxis;
    private NumberAxis yAxis;

    private double xWidth;
    private double xMax;

    public RealtimeChart(String title, String xLabel, String yLabel) {
        LineChart<Number, Number> temp = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        constructObject(title, xLabel, yLabel, temp.getData());
    }

    public RealtimeChart(String title, String xLabel, String yLabel, XYChart.Series<Number, Number> series) {
        ObservableList<XYChart.Series<Number, Number>> list = FXCollections.observableList(new ArrayList<>());
        list.add(series);
        constructObject(title, xLabel, yLabel, list);
    }

    public RealtimeChart(String title, String xLabel, String yLabel, ObservableList<XYChart.Series<Number, Number>> seriesList) {
        constructObject(title, xLabel, yLabel, seriesList);
    }

    private void constructObject(String title, String xLabel, String yLabel, ObservableList<XYChart.Series<Number, Number>> seriesList) {
        this.seriesList = seriesList;
        xWidth = 5;

        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLabel(xLabel);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        yAxis.setLabel(yLabel);

        chart = new LineChart<Number, Number>(xAxis, yAxis, seriesList);
        chart.setCreateSymbols(false);
        attachSeriesListListener();
        attachSingleSeriesListener(seriesList);
        setLayout();
    }

    private void setLayout() {
        getStyleClass().add("realtimeChart");
        getChildren().add(chart);

        chart.prefWidthProperty().bind(widthProperty());
        chart.getStyleClass().add("lineChart");
        updateLegendVisible();
    }

    private void updateLegendVisible() {
        if (seriesList.size() == 1) {
            chart.setLegendVisible(false);
        } else {
            chart.setLegendVisible(true);
        }
    }

    /**
     * Attaches listeners for every XYData.Data in XYCHart.Series.
     *
     * @param seriesList
     */
    private void attachSingleSeriesListener(List<XYChart.Series<Number, Number>> seriesList) {
        for (int i = 0; i < seriesList.size(); i++) {
            ObservableList<XYChart.Data<Number, Number>> data = seriesList.get(i).getData();
            data.addListener((ListChangeListener) c -> {
                processSingleSeriesChange(c);
            });
        }
    }

    /**
     * Ataches listener for every element in seriesList.
     */
    private void attachSeriesListListener() {
        seriesList.addListener((ListChangeListener) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    attachSingleSeriesListener(c.getAddedSubList());
                    updateLegendVisible();
                }
            }
        });
    }

    /**
     * Called when a single series is changed.
     *
     * @param c
     */
    private void processSingleSeriesChange(ListChangeListener.Change c) {
        while (c.next()) {
            if (c.wasAdded()) {
                double maxX = xMax;
                List<XYChart.Data<Number, Number>> sublist = c.getAddedSubList();
                for (int i = 0; i < sublist.size(); i++) {
                    XYChart.Data<Number, Number> p = sublist.get(i);
                    double x = p.getXValue().doubleValue();
                    double y = p.getYValue().doubleValue();

                    if (x > maxX) maxX = x;
                }
                setXMax(maxX);
            }
        }
    }

    /**
     * Sets the xMax and changes the bounds of the x-axis.
     * This is what gives it realtime.
     *
     * @param xMax
     */
    private void setXMax(double xMax) {
        this.xMax = xMax;

        // Update the xAxis range
        double xMin = xMax - xWidth;
        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
    }

    public void setSeries(XYChart.Series<Number, Number> series) {
        ObservableList<XYChart.Series<Number, Number>> list = FXCollections.observableList(new ArrayList<>());
        list.add(series);
    }

    public double getXWidth() {
        return xWidth;
    }

    public void setXWidth(double xWidth) {
        this.xWidth = xWidth;
    }

    public void setSize(double w, double h) {
        chart.setPrefSize(w, h);
        chart.setMinSize(w, h);
        chart.setMaxSize(w, h);
    }

    public void clear() {

    }

    public LineChart<Number, Number> getChart() {
        return chart;
    }
}
