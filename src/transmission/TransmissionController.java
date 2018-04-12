package transmission;

import java.util.ArrayList;

/**
 * Singleton class for controlling the serial communication in the app.
 */
public class TransmissionController {

    private static TransmissionController instance = null;

    private static final char DELIMETER = ';';

    SerialController serialController;

    private static EncoderTransmission driveTransmission;
    private static PathTransmission pathTransmission;

    private TransmissionController() {

    }

    public static TransmissionController getInstance() {
        if(instance == null) {
            instance = new TransmissionController();
        }
        return instance;
    }

    public static void flushTransmission(String input) throws InvalidTransmissionException {
        System.out.print(input);
        if (input.charAt(0) == Transmission.START_TICK_TRANSMISSION) {
            driveTransmission.processTransmission(parseTransmissionString(input, DELIMETER));
        }
        else if (input.charAt(0) == Transmission.START_ORIGINAL_PATH_TRANSMISSION) {
            pathTransmission.processTransmission(parseTransmissionString(input, DELIMETER));
        }
        else {
            throw new InvalidTransmissionException("Start of transmission not found.");
        }
    }

    public boolean startTransmission(String comPort) {
        driveTransmission = new EncoderTransmission();
        pathTransmission = new PathTransmission();

        serialController = new SerialController();
        return serialController.initialize(comPort);
    }

    public void stopTransmission() {
        serialController.closeSerial();
    }

    public EncoderTransmission getEncoderTransmission() {
        return driveTransmission;
    }

    public PathTransmission getPathTransmission() {
        return pathTransmission;
    }

    public static ArrayList parseTransmissionString(String text, char delimeter) {
        ArrayList<Number> signal = new ArrayList<>();
        // Iterate through every character with delimeter and parse the character inputs.
        // First character is ommitted since that is start of transmission character value.
        int prev = 1;
        text += ';';
        for (int i = 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == delimeter) {
                signal.add(Double.parseDouble(text.substring(prev, i)));
                prev = ++i;
            }
        }
        return signal;
    }

    public static void clear() {
        if (instance != null) {
            instance.getEncoderTransmission().clear();
        }
    }
}
