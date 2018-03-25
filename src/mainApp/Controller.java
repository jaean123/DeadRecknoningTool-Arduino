package mainApp;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import transmission.Transmission;
import transmission.TransmissionController;

/**
 * Controller class mainly contains methods that gets called in response to UI events.
 */
public class Controller {

    private MainApp app;

    boolean isSerialOpen;

    public Controller(MainApp app) {
        this.app = app;
    }

    /**
     * Called when serial button is pressed
     * @param serialBtn The serial button.
     * @param comPort COM port to connect to (only used when connecting).
     */
    public void processSerialOpen(Button serialBtn, String comPort) {
        if (isSerialOpen) {
            // disconnect and change button text to "Open Serial".
            TransmissionController.getInstance().stopTransmission();
            serialBtn.setText("Open Serial");
        }
        else {
            // Connect and change button text to "Close Serial".
            boolean startSuccess = TransmissionController.getInstance().startTransmission(comPort);
            if (startSuccess) {
                app.getView().startDrawingPath();
                serialBtn.setText("Close Serial");
            }
            else {
                app.getView().showInfoDialog("Connection failed. Could not find serial port.");
            }
        }
        isSerialOpen = !isSerialOpen;
    }

    public void processZoom(KeyCode code) {
        if (code == KeyCode.PLUS) {

        }
        else if (code == KeyCode.MINUS) {

        }
    }
}
