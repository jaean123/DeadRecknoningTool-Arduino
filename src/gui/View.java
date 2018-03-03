package gui;

import data.GlobalConstants;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polyline;

public class View {

    public static String stylesheet = "gui/styles.css";

    private MainApp app;

    private BorderPane root;
    private HBox buttonPane;

    private HBox resetBtnContainer;
    private HBox comPortInputContainer;
    private Button resetBtn;
    private Button serialBtn;
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

    public void setLayout() {
        buttonPane = new HBox();
        resetBtn = new Button("Reset");
        serialBtn = new Button("Open Serial");
        resetBtnContainer = new HBox();
        comPortInputContainer = new HBox();
        comPortTextFieldLabel = new Label("Enter Port: ");
        comPortTextField = new TextField();

        points = new Polyline();
        points.getPoints().addAll(new Double[]{
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0});

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        resetBtnContainer.getChildren().add(resetBtn);
        comPortInputContainer.getChildren().addAll(comPortTextFieldLabel, comPortTextField, serialBtn);
        buttonPane.getChildren().addAll(comPortInputContainer, region1, region2, resetBtnContainer);
        root.setTop(buttonPane);
        root.setBottom(points);
    }

    public void applyStyles() {
        root.getScene().getStylesheets().add(stylesheet);
        root.setId("root");


        resetBtn.getStyleClass().add("button");
        serialBtn.getStyleClass().add("button");
        comPortTextFieldLabel.setPadding(new Insets(3, 0, 0 , 0));

        comPortInputContainer.getStyleClass().add("buttonPannelContainer");
        resetBtnContainer.getStyleClass().add("buttonPannelContainer");

        buttonPane.setId("buttonPane");
    }

    public Button getSerialBtn() {
        return serialBtn;
    }

    public void initEventHandlers() {
        serialBtn.setOnAction(e -> {
            app.getController().processSerialOpen(serialBtn, comPortTextField.getText());
        });
    }

    public void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(GlobalConstants.appName);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
