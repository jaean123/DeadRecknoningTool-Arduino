package mainApp;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import transmission.TransmissionController;

/**
 * Controller class mainly contains methods that gets called in response to UI events.
 */
public class Controller {

    private MainApp app;
    boolean isSerialOpen;
    boolean onDragMode;
    double prevMouseX, prevMouseY;

    public Controller(MainApp app) {
        this.app = app;
    }

    /**
     * Called when serial button is pressed
     *
     * @param serialBtn The serial button.
     * @param comPort   COM port to connect to (only used when connecting).
     */
    public void processSerialOpen(Button serialBtn, String comPort) {
        if (isSerialOpen) {
            // disconnect and change button text to "Open Serial".
            TransmissionController.getInstance().stopTransmission();
            serialBtn.setText("Open Serial");
        } else {
            // Connect and change button text to "Close Serial".
            boolean startSuccess = TransmissionController.getInstance().startTransmission(comPort);
            if (startSuccess) {
                app.getView().startDrawingPath();
                serialBtn.setText("Close Serial");
            } else {
                app.getView().showInfoDialog("Connection failed. Could not find serial port.");
            }
        }
        isSerialOpen = !isSerialOpen;
    }

    public void processKeyPress(KeyEvent e) {
        KeyCode code = e.getCode();
        View view = app.getView();
        // Zoom in
        if (code == KeyCode.ADD) {
            view.zoomIn();
            e.consume();
        }
        // Zoom out
        else if (code == KeyCode.SUBTRACT) {
            view.zoomOut();
            e.consume();
        }
        // Translate up
        else if (code == KeyCode.UP) {
            view.translateUp();
            e.consume();
        }
        // Translate right
        else if (code == KeyCode.RIGHT) {
            view.translateRight();
            e.consume();
        }
        // Translate down
        else if (code == KeyCode.DOWN) {
            view.translateDown();
            e.consume();
        }
        // Translate left
        else if (code == KeyCode.LEFT) {
            view.translateLeft();
            e.consume();
        }
        if (code == KeyCode.D) {
//            app.getView().doDebug();
        }
    }

    public void processClear() {
        app.getView().clear();
        TransmissionController.clear();
    }

    public void processPathPaneMouseDown(MouseEvent e) {
        onDragMode = true;
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    public void processPathPaneMouseDrag(MouseEvent e) {
        if (onDragMode) {
            double x = e.getX();
            double y = e.getY();
            double dx = x - prevMouseX;
            double dy = y - prevMouseY;
            prevMouseX = x;
            prevMouseY = y;
            app.getView().translate(dx, dy);
        }
    }

    public void processPathPaneMouseDragReleased() {
        if (onDragMode) onDragMode = false;
    }

    public void processPathPaneScroll(ScrollEvent e) {
        double dy = e.getDeltaY();
        if (dy > 0) {
            app.getView().zoomIn();
        }
        else {
            app.getView().zoomOut();
        }
    }
}
