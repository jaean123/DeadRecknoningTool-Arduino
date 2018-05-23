package transmission;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for controlling the serial communication in the app.
 */
public class TransmissionController {

    private static TransmissionController instance;

    SerialController serialController;

    private static ArrayList<Byte> signalBuffer;

    private static EncoderTransmission driveTransmission;

    private static PathTransmission pathTransmission;

    private ArrayList<Byte> prevByteArray;

    private TransmissionController() {
        signalBuffer = new ArrayList<>();
    }

    public static TransmissionController getInstance() {
        if(instance == null) {
            instance = new TransmissionController();
        }
        return instance;
    }

    public void processTransmission() {
        ArrayList<ArrayList<Byte>> transmissions = new ArrayList<>();
        // -1 is the delimeter for each transmission.
        // This method is buggy Fix is needed and rewrite. Code is a mess.
        byte delimeter = -1;

        // remove until first delimeter if not at 0th index.
        int dIndex = signalBuffer.indexOf(delimeter);
        if (dIndex > 0) {
            for (int i = 0; i < dIndex; i++) {
                signalBuffer.remove(0);
            }
        }

        int secondDIndex = signalBuffer.subList(1, signalBuffer.size()).indexOf(delimeter);
        if (secondDIndex == -1) {
            return;
        }

        if (prevByteArray != null) {
            transmissions.add(prevByteArray);
            prevByteArray = null;
        }

        // while there are still things to be parsed into sub-transmissions.
        while (true) {
            // remove the first delimeter at index 0.
            signalBuffer.remove(0);
            dIndex = signalBuffer.indexOf(delimeter);
            if (dIndex > 0) {
                ArrayList<Byte> trans = new ArrayList<>(signalBuffer.subList(0, dIndex));
                if (trans.size() == PathTransmission.BYTES_TO_READ) {
                    transmissions.add(trans);
                }
                for (int i = 0; i < dIndex; i++) {
                    // remove from the zeroth index to the dIndex inclusive.
                    signalBuffer.remove(0);
                }
            }
            else {
                break;
            }
        }

        int size = transmissions.size();

        // if the array is not divisible by 2, then remove the last element.
        if (size % 2 != 0) {
            prevByteArray = transmissions.remove(size-1);
        }

        // path transmission must be sent two at a time in pairs for x and y values.
        for (int i = 0; i+1 < transmissions.size(); i += 2) {
            pathTransmission.processTransmission(transmissions.get(i)); // send x value
            pathTransmission.processTransmission(transmissions.get(i+1)); // send y value
        }
    }

    public void flushTransmission(ArrayList<Byte> input) throws InvalidTransmissionException {
        signalBuffer.addAll(input);
        processTransmission();
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

    public static ArrayList<ArrayList<Number>> parseTransmissionString(String text, char delimeter) {
        ArrayList<ArrayList<Number>> commandList = new ArrayList<>();
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
        return commandList;
    }

    public static void clear() {
        if (instance != null) {
            instance.getEncoderTransmission().clear();
        }
    }
}
