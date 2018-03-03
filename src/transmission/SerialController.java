package transmission;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;


public class SerialController implements SerialPortEventListener {

    SerialPort serialPort;
    boolean closeSerial;

    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM11", // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 500;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    private StringBuilder transmission;

    public SerialController() { }

    public boolean initialize(String comPort) {
        // the next line is for Raspberry Pi and
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

      //  System.setProperty("java.library.path", "/path/to/library");

        transmission = new StringBuilder();
        closeSerial = false;

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
/*            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }*/
            // original code commented out.
            // Todo: cross platform fix
            if (currPortId.getName().equals(comPort)) {
                portId = currPortId;
                break;
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return false;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return true;
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    private synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                // if it has end of transmission, flush it to the transmission
                int intChar;
                while (true) {
                    intChar = input.read();
                    if (intChar == -1) {
                        break;
                    }
                    else if ((char)intChar == Transmission.END_TRANSMISSION) {
                        // flush the transmission to transmission processer
                        // flush not including the end transmission signal.
                        try {
                            TransmissionController.flushTransmission(transmission.toString().substring(0, transmission.length()));
                        } catch (InvalidTransmissionEx e) {
                            e.printStackTrace();
                        }
                        transmission.setLength(0);
                    } else if (intChar != 0) {
                        transmission.append((char)intChar);
                    }
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        if (closeSerial) {
            close();
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public void closeSerial() {
        closeSerial = true;
    }
}
