package mainApp;

import data.CartesianPlane;
import data.GlobalConstants;
import data.XY;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

/**
 * View class deals with GUI layout, styling, and event handlers.
 */
public class View {

    public static final String STYLESHEET = "mainApp/styles.css";
    public static final double TRANSLATE_BY = 10;

    private MainApp app;

    private BorderPane root;
    private SplitPane splitPane;
    private Pane plotPane;
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
        plotPane = new Pane();

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
        // Add STYLESHEET to root.
        root.getScene().getStylesheets().add(STYLESHEET);
        root.setId("root");

        // Apply styles to various UI components
        clearBtn.getStyleClass().add("button");
        serialBtn.getStyleClass().add("button");
        comPortTextFieldLabel.setPadding(new Insets(3, 0, 0 , 0));

        topLeftContainer.getStyleClass().add("buttonPannelContainer");
        topRightContainer.getStyleClass().add("buttonPannelContainer");

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
     * @param text The text to be displayed
     */
    protected void showInfoDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Add icon to the info pop up.
        try {
            String iconLocation = new File(GlobalConstants.APP_ICON_LOCATION).toURI().toURL().toString();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(iconLocation));
        }
        catch (Exception e) {
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
        actualPath.getPoints().clear();
        targetPath.getPoints().clear();
    }
}
