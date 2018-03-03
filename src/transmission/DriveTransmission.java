package transmission;

import data.EncoderCount;

import java.util.Vector;

public class DriveTransmission implements Transmission {

    private static final char DELIMETER = ';';

    private Vector<EncoderCount> encoderCounts;

    public DriveTransmission() {
        encoderCounts = new Vector<>();
    }

    @Override
    public void processTransmission(String input) {
        try {
            int delIndex = input.indexOf(DELIMETER);
            int num1 = Integer.parseInt(input.substring(1, delIndex));
            int num2 = Integer.parseInt(input.substring(delIndex+1, input.length()));
            EncoderCount data = new EncoderCount(num1, num2);
            encoderCounts.add(data);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Vector<EncoderCount> getDriveData() {
        return encoderCounts;
    }
}
