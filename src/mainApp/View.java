package mainApp;

import data.CartesianPlane;
import data.GlobalConstants;
import data.XY;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * View class deals with GUI layout, styling, and event handlers.
 */
public class View {

    public static final String STYLESHEET = "mainApp/styles.css";
    public static final double TRANSLATE_BY = 10;

    private MainApp app;

    private BorderPane root;
    private Pane plotPane;
    private Pane pathPane;
    private HBox buttonPane;

    private HBox topRightContainer;
    private HBox topLeftContainer;
    private Button resetBtn;
    private Button serialBtn;
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private Button aboutBtn;
    private Label comPortTextFieldLabel;
    private TextField comPortTextField;

    private Draw actualPath; // Dead reckoned path.
    private Draw targetPath; // Path drawn.

    public View(BorderPane root, MainApp app) {
        this.root = root;
        this.app = app;
        setLayout();
        applyStyles();
        initEventHandlers();
        startDrawingPath();
    }

    /**
     * Sets the layout of the user interface
     */
    public void setLayout() {

        // This Pane contains all the top portion of the root BorderPane
        buttonPane = new HBox();

        // Top left portion of buttonPane UI components
        comPortTextFieldLabel = new Label("Enter Port: ");
        comPortTextField = new TextField();
        serialBtn = new Button("Open Serial");
        topLeftContainer = new HBox();
        topLeftContainer.getChildren().addAll(comPortTextFieldLabel, comPortTextField, serialBtn);

        // Top right portion of buttonPane UI components
        zoomInBtn = new Button("+");
        zoomInBtn.setTooltip(new Tooltip("Zoom In (+ key)"));
        zoomOutBtn = new Button("-");
        zoomOutBtn.setTooltip(new Tooltip("Zoom Out (- key)"));
        aboutBtn = new Button("About");
        resetBtn = new Button("Reset");
        topRightContainer = new HBox();
        topRightContainer.getChildren().addAll(zoomInBtn, zoomOutBtn, aboutBtn, resetBtn);


        // Left, center, right placement in top HBox.
        // Refer to: https://stackoverflow.com/questions/41654333/how-to-align-children-in-a-hbox-left-center-and-right
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        buttonPane.getChildren().addAll(topLeftContainer, region1, region2, topRightContainer);

        // Center portion where the plots will go.
        plotPane = new Pane();

        // Path will be drawn in this Pane.
        pathPane = new Pane();
        pathPane.setPrefHeight(350);

        root.setTop(buttonPane);
        root.setCenter(plotPane);
        root.setBottom(pathPane);
    }

    /**
     * Applies the CSS styles on the various GUI elements.
     */
    public void applyStyles() {
        // Add STYLESHEET to root.
        root.getScene().getStylesheets().add(STYLESHEET);
        root.setId("root");

        // Apply styles to various UI components
        resetBtn.getStyleClass().add("button");
        serialBtn.getStyleClass().add("button");
        comPortTextFieldLabel.setPadding(new Insets(3, 0, 0 , 0));

        topLeftContainer.getStyleClass().add("buttonPannelContainer");
        topRightContainer.getStyleClass().add("buttonPannelContainer");

        buttonPane.setId("buttonPane");
    }

    /**
     * Initiates the event handlers for the various GUI components.
     */
    public void initEventHandlers() {
        // Serial button handler.
        serialBtn.setOnAction(e -> app.getController().processSerialOpen(serialBtn, comPortTextField.getText()));

        // Info button handler.
        aboutBtn.setOnAction(e -> showInfoDialog(GlobalConstants.ABOUT_TEXT));

        zoomInBtn.setOnAction(e -> zoomIn());

        zoomOutBtn.setOnAction(e -> zoomOut());

        // Focus on root BorderPane so that the textfield will not be in focus.
        root.setOnMouseClicked(e -> root.requestFocus());

        // handle plus or minus zoom key presses.
        root.getScene().setOnKeyPressed(e -> app.getController().processKeyPress(e));
    }

    public void startDrawingPath() {
//        CartesianPlane path = TransmissionController.getInstance().getEncoderTransmission().getDeadReckoner().getPlane();
        CartesianPlane path = new CartesianPlane();
        ObservableList<XY> points = path.getPoints();
        points.add(new XY(0,0));
        points.add(new XY(1,1));
        points.add(new XY(2,2));
        points.add(new XY(3,3));
        points.add(new XY(4,4));
        points.add(new XY(5,5));
        points.add(new XY(100,100));
        points.add(new XY(0,100));
        pathPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//        Color lineColor = Helpers.hexToRGB(GlobalConstants.DEAD_RECKONED_PATH_LINE_COLOR);
        Color lineColor = Color.BLUE;
        actualPath = new Draw(pathPane, path, lineColor);
        targetPath = new Draw(pathPane, path, lineColor);
    }

    /**
     * Shows information dialog
     * @param text The text to be displayed
     */
    public void showInfoDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(GlobalConstants.APP_NAME);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Zoom path view in/out.
     */
    private void zoom(double factor) {
        actualPath.setScaleFactor(actualPath.getScaleFactor()*factor);
        targetPath.setScaleFactor(targetPath.getScaleFactor()*factor);
    }

    public void zoomIn() {
        zoom(1.1);
    }

    public void zoomOut() {
        zoom(0.90);
    }

    private void translate(double dX, double dY) {
        actualPath.translate(dX, dY);
        targetPath.translate(dX, dY);
    }

    public void translateUp() {
        translate(0, -TRANSLATE_BY);
    }

    public void translateDown() {
        translate(0, TRANSLATE_BY);
    }

    public void translateRight() {
        translate(TRANSLATE_BY, 0);
    }

    public void translateLeft() {
        translate(-TRANSLATE_BY, 0);
    }
}
