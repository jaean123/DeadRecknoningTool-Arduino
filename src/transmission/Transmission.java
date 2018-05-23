package transmission;

import java.util.ArrayList;

public interface Transmission {

    public void processTransmission(ArrayList signal);

    public static byte[] objectToByteArray(Object[] objAry) {
        byte[] byteAry = new byte[objAry.length];
        for (int i = 0; i < objAry.length; i++) {
            byteAry[i] = (Byte) objAry[i];
        }
        return byteAry;
    }
}
