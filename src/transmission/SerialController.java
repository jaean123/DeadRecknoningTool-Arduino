package transmission;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public class SerialController {

    boolean disconnectComPort;

    public SerialController() {
        disconnectComPort = false;
    }

    public boolean initialize(String portName) {
        SerialPort comPort = SerialPort.getCommPort(portName);
        comPort.setBaudRate(115200);
        boolean portOpened = comPort.openPort();

        Thread serialThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        while (comPort.bytesAvailable() == 0)
                            Thread.sleep(20);

                        int numBytesAvailable = comPort.bytesAvailable();
                        if (numBytesAvailable > 0) {
                            byte[] readBuffer = new byte[comPort.bytesAvailable()];
                            int numRead = comPort.readBytes(readBuffer, readBuffer.length);

                            ArrayList<Byte> signalReceived = new ArrayList<>();
                            if (numRead > 0) {
                                for (int i = 0; i < numRead; i++) {
                                    signalReceived.add(readBuffer[i]);
                                }
                            }
                            TransmissionController.getInstance().flushTransmission(signalReceived);
                        }
                        if (disconnectComPort) {
                            comPort.closePort();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                comPort.closePort();
            }
        };
        serialThread.start();

        return portOpened;
    }

    public void closeSerial() {
        disconnectComPort = true;
    }
}
