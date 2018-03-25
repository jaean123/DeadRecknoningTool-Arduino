package mainApp;

import data.GlobalConstants;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import transmission.EncoderTransmission;
import transmission.TransmissionController;

/**
 * View class deals with GUI layout, styling, and event handlers.
 */
public class View {

    public static String stylesheet = "mainApp/styles.css";

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

    private Polyline points;

    public View(BorderPane root, MainApp app) {
        this.root = root;
        this.app = app;
        setLayout();
        applyStyles();
        initEventHandlers();
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

        root.setTop(buttonPane);
        root.setCenter(plotPane);
        root.setBottom(pathPane);
    }

    /**
     * Applies the CSS styles on the various GUI elements.
     */
    public void applyStyles() {
        // Add stylesheet to root.
        root.getScene().getStylesheets().add(stylesheet);
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
        serialBtn.setOnAction(e -> {
            app.getController().processSerialOpen(serialBtn, comPortTextField.getText());
        });

        // Info button handler.
        aboutBtn.setOnAction(e -> {
            showInfoDialog(GlobalConstants.ABOUT_TEXT);
        });

        // handle plus or minus zoom key presses.
        root.getScene().setOnKeyPressed(e -> {
            app.getController().processZoom(e.getCode());
        });
    }

    public void startDrawingPath() {
        EncoderTransmission encoderTransmission = TransmissionController.getInstance().getEncoderTransmission();
        Color lineColor = Helpers.hexToRGB(GlobalConstants.DEAD_RECKONED_PATH_LINE_COLOR);
        Draw draw = new Draw(pathPane, encoderTransmission, lineColor);
        draw.startDrawing();
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
}
