package transmission;

public class TransmissionController {

    private static TransmissionController instance = null;

    SerialController serialController;

    private static DriveTransmission driveTransmission;
    private static PathTransmission originalPathTransmission;
    private static StringBuilder transmission;

    private TransmissionController() {

    }

    public static TransmissionController getInstance() {
        if(instance == null) {
            instance = new TransmissionController();
        }
        return instance;
    }

    public static void flushTransmission(String input) throws InvalidTransmissionEx {
        if (input.charAt(0) == Transmission.START_TICK_TRANSMISSION) {
            driveTransmission.processTransmission(input);
        }
        else if (input.charAt(0) == Transmission.START_ORIGINAL_PATH_TRANSMISSION) {
            originalPathTransmission.processTransmission(input);
        }
        else {
            throw new InvalidTransmissionEx("Start of transmission not found.");
        }
    }

    public boolean startTransmission(String comPort) {
        driveTransmission = new DriveTransmission();
        originalPathTransmission = new PathTransmission();
        transmission = new StringBuilder();

        serialController = new SerialController();
        return serialController.initialize(comPort);
    }

    public void stopTransmission() {
        serialController.closeSerial();
    }

    public static DriveTransmission getDriveTransmission() {
        return driveTransmission;
    }

    public static PathTransmission getOriginalPathTransmission() {
        return originalPathTransmission;
    }
}
