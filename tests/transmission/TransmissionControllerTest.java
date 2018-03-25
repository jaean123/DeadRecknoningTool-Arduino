package transmission;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TransmissionControllerTest {

    @Test
    void parseTransmissionString() {
        ArrayList<Integer> signal1 = TransmissionController.parseTransmissionString("!10;12", ';');
        ArrayList<Integer> signal2 = TransmissionController.parseTransmissionString("!1;1", ';');
        ArrayList<Integer> signal3 = TransmissionController.parseTransmissionString("!0;0", ';');
        ArrayList<Integer> signal4 = TransmissionController.parseTransmissionString("!1;2;3;4", ';');
        ArrayList<Integer> signal5 = TransmissionController.parseTransmissionString("!0", ';');

        assertEquals(signal1.get(0), new Integer(10));
        assertEquals(signal1.get(1), new Integer(12));

        assertEquals(signal2.get(0), new Integer(1));
        assertEquals(signal2.get(1), new Integer(1));

        assertEquals(signal3.get(0), new Integer(0));
        assertEquals(signal3.get(1), new Integer(0));

        assertEquals(signal4.get(0), new Integer(1));
        assertEquals(signal4.get(1), new Integer(2));
        assertEquals(signal4.get(2), new Integer(3));
        assertEquals(signal4.get(3), new Integer(4));

        assertEquals(signal5.get(0), new Integer(0));


    }
}