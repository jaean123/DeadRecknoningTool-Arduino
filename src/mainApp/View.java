package mainApp;

import data.GlobalConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

/**
 * View class deals with GUI layout, styling, and event handlers.
 */
public class View {

    public static final double TRANSLATE_BY = 10;

    private MainApp app;

    private BorderPane root;
    private SplitPane splitPane;
    private HBox plotPane;
    private Pane pathPane;
    private HBox buttonPane;

    private HBox topRightContainer;

    private HBox topLeftContainer;
    private Button clearBtn;
    private Button serialBtn;
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private Button aboutBtn;
    private Label comPortTextFieldLabel;
    private TextField comPortTextField;

    private PathPolyLine actualPath; // Dead reckoned path.
    private PathPolyLine targetPath; // Path drawn.

    public View(BorderPane root, MainApp app) {
        this.root = root;
        this.app = app;
        setLayout();
        applyStyles();
        initEventHandlers();
        startDrawingPath();
        testPlotPane();
    }

    private void testPlotPane() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data

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

        RealtimeChart chart = new RealtimeChart("Test Chart", "x", "y", series);
        plotPane.getChildren().add(chart);
        plotPane.setAlignment(Pos.CENTER);


        series.getData().add(new XYChart.Data(100, 25));
        series.getData().add(new XYChart.Data(120, 200));
    }

    /**
     * Sets the layout of the user interface
     */
    private void setLayout() {

        // This Pane contains all the top portion of the root BorderPane
        buttonPane = new HBox();

        // Top left portion of buttonPane UI components
        comPortTextFieldLabel = new Label(GlobalConstants.ENTER_PORT);
        comPortTextField = new TextField();
        serialBtn = new Button(GlobalConstants.OPEN_SERIAL);
        serialBtn.setTooltip(new Tooltip(GlobalConstants.OPEN_SERIAL_TOOLTIP));
        topLeftContainer = new HBox();
        topLeftContainer.getChildren().addAll(comPortTextFieldLabel, comPortTextField, serialBtn);

        // Top right portion of buttonPane UI components
        zoomInBtn = new Button(GlobalConstants.ZOOM_IN);
        zoomInBtn.setTooltip(new Tooltip(GlobalConstants.ZOOM_IN_TOOLTIP));
        zoomOutBtn = new Button(GlobalConstants.ZOOM_OUT);
        zoomOutBtn.setTooltip(new Tooltip(GlobalConstants.ZOOM_OUT_TOOLTIP));
        aboutBtn = new Button(GlobalConstants.ABOUT);
        aboutBtn.setTooltip(new Tooltip(GlobalConstants.ABOUT_TOOLTIP));
        clearBtn = new Button(GlobalConstants.CLEAR);
        clearBtn.setTooltip(new Tooltip(GlobalConstants.CLEAR_TOOLTIP));
        topRightContainer = new HBox();
        topRightContainer.getChildren().addAll(zoomInBtn, zoomOutBtn, aboutBtn, clearBtn);

        // Left, center, right placement in top HBox.
        // Refer to: https://stackoverflow.com/questions/41654333/how-to-align-children-in-a-hbox-left-center-and-right
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        buttonPane.getChildren().addAll(topLeftContainer, region1, region2, topRightContainer);

        // Bottom portion of app.
        splitPane = new SplitPane();
        plotPane = new HBox();

        // Path will be drawn in this Pane.
        pathPane = new Pane();
        pathPane.setPrefSize(350, 600);

        splitPane.getItems().addAll(plotPane, pathPane);

        root.setTop(buttonPane);
        root.setCenter(splitPane);
    }

    /**
     * Applies the CSS styles on the various GUI elements.
     */
    private void applyStyles() {
        // Add STYLESHEET_CUSTOM to root.
        root.getScene().getStylesheets().add(GlobalConstants.getFilePath(GlobalConstants.STYLESHEET_BOOTSTRAP));
        root.getScene().getStylesheets().add(GlobalConstants.getFilePath(GlobalConstants.STYLESHEET_CUSTOM));

        root.setId("root");

        // Apply styles to various UI components
/*        clearBtn.getStyleClass().add("success");
        serialBtn.getStyleClass().add("success");
        zoomOutBtn.getStyleClass().add("success");
        zoomInBtn.getStyleClass().add("success");
        aboutBtn.getStyleClass().add("success");
        clearBtn.getStyleClass().add("success");*/

        comPortTextFieldLabel.setPadding(new Insets(3, 0, 0, 0));

        topLeftContainer.getStyleClass().add("buttonPannelContainer");
        topRightContainer.getStyleClass().add("buttonPannelContainer");

        plotPane.setId("plotPane");
        pathPane.setId("pathPane");

        buttonPane.setId("buttonPane");
    }

    /**
     * Initiates the event handlers for the various GUI components.
     */
    private void initEventHandlers() {
        // Serial button handler.
        serialBtn.setOnAction(e -> app.getController().processSerialOpen(serialBtn, comPortTextField.getText()));

        // Info button handler.
        aboutBtn.setOnAction(e -> showInfoDialog(GlobalConstants.ABOUT_TEXT));

        clearBtn.setOnAction(e -> app.getController().processClear());

        zoomInBtn.setOnAction(e -> zoomIn());

        zoomOutBtn.setOnAction(e -> zoomOut());

        // Focus on root BorderPane so that the textfield will not be in focus.
        // TODO Fix this?
        root.setOnMouseClicked(e -> {
            if (!root.isFocused()) root.requestFocus();
        });
        splitPane.setOnMouseClicked(e -> {
            if (!root.isFocused()) root.requestFocus();
        });

        pathPane.setOnMousePressed(e -> app.getController().processPathPaneMouseDown(e));
        pathPane.setOnMouseDragged(e -> app.getController().processPathPaneMouseDrag(e));
        pathPane.setOnMouseReleased(e -> app.getController().processPathPaneMouseDragReleased());
        pathPane.setOnScroll(e -> app.getController().processPathPaneScroll(e));

        // handle plus or minus zoom key presses.
        root.getScene().setOnKeyPressed(e -> app.getController().processKeyPress(e));
    }

    protected void startDrawingPath() {
//        CartesianPlane path = TransmissionController.getInstance().getEncoderTransmission().getDeadReckoner().getPlane();
        XYChart.Series<Double, Double> path = new XYChart.Series<>();

        path.getData().add(new XYChart.Data<Double, Double>(0.0, 0.0));
        path.getData().add(new XYChart.Data<Double, Double>(1.0, 1.0));
        path.getData().add(new XYChart.Data<Double, Double>(2.0, 2.0));
        path.getData().add(new XYChart.Data<Double, Double>(3.0, 3.0));
        path.getData().add(new XYChart.Data<Double, Double>(4.0, 4.0));
        path.getData().add(new XYChart.Data<Double, Double>(5.0, 5.0));
        path.getData().add(new XYChart.Data<Double, Double>(100.0, 100.0));
        path.getData().add(new XYChart.Data<Double, Double>(0.0, 100.0));

        pathPane.setBackground(
                new Background(
                        new BackgroundFill(
                                GlobalConstants.PATH_PANE_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        actualPath = new PathPolyLine(path, GlobalConstants.ACTUAL_PATH_LINE_COLOR);
        targetPath = new PathPolyLine(path, GlobalConstants.TARGET_PATH_LINE_COLOR);
        actualPath.setScaleX(2.0);
        actualPath.setScaleY(2.0);
        pathPane.getChildren().addAll(actualPath, targetPath);
//        actualPath = new Draw(pathPane, path, GlobalConstants.ACTUAL_PATH_LINE_COLOR);
//        targetPath = new Draw(pathPane, path, GlobalConstants.TARGET_PATH_LINE_COLOR);
    }

    /**
     * Shows information dialog
     *
     * @param text The text to be displayed
     */
    protected void showInfoDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Add icon to the info pop up.
        try {
            String iconLocation = new File(GlobalConstants.APP_ICON_LOCATION).toURI().toURL().toString();
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(GlobalConstants.getFilePath(GlobalConstants.STYLESHEET_BOOTSTRAP));
            Stage stage = (Stage) scene.getWindow();
            stage.getIcons().add(new Image(iconLocation));
        } catch (Exception e) {
            e.printStackTrace();
        }

        alert.setTitle(GlobalConstants.APP_NAME);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Zoom path view in/out.
     */
    protected void zoom(double increment) {
        actualPath.setScaleFactor(actualPath.getScaleFactor() + increment);
        targetPath.setScaleFactor(targetPath.getScaleFactor() + increment);
    }

    protected void zoomIn() {
        zoom(0.10);
    }

    protected void zoomOut() {
        zoom(-0.10);
    }

    public void translate(double dX, double dY) {
        actualPath.setTranslateX(actualPath.getTranslateX() + dX);
        actualPath.setTranslateY(actualPath.getTranslateY() + dY);
        targetPath.setTranslateX(targetPath.getTranslateX() + dX);
        targetPath.setTranslateY(targetPath.getTranslateY() + dY);
    }

    protected void translateUp() {
        translate(0, -TRANSLATE_BY);
    }

    protected void translateDown() {
        translate(0, TRANSLATE_BY);
    }

    protected void translateRight() {
        translate(TRANSLATE_BY, 0);
    }

    protected void translateLeft() {
        translate(-TRANSLATE_BY, 0);
    }

    /**
     * Remove path drawing, reset the plot, and delete the data.
     * TODO: Reset plot.
     */
    protected void clear() {
        // TODO: implement this.
    }
}
