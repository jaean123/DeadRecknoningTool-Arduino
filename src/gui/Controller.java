package gui;

import javafx.scene.control.Button;
import transmission.TransmissionController;

public class Controller {

    private MainApp app;

    boolean isSerialOpen;

    public Controller(MainApp app) {
        this.app = app;
    }

    public void processSerialOpen(Button serialBtn, String comPort) {
        if (isSerialOpen) {
            TransmissionController.getInstance().stopTransmission();
            serialBtn.setText("Open Serial");
        }
        else {
            boolean startSuccess = TransmissionController.getInstance().startTransmission(comPort);
            if (startSuccess) serialBtn.setText("Close Serial");
            else {
                app.getView().showInfoDialog("Connection failed. Could not find serial port.");
            }
        }
        isSerialOpen = !isSerialOpen;
    }
}
