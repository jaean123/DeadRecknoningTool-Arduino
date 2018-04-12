package transmission;

import java.util.ArrayList;

public interface Transmission {

    public static final char START_ORIGINAL_PATH_TRANSMISSION = 'p';
    public static final char START_TICK_TRANSMISSION = 't';
    public static final char END_TRANSMISSION = '\n';

    public void processTransmission(ArrayList signal);
}
