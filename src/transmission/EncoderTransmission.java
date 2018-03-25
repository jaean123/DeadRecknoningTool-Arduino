package transmission;

import data.DeadReckoner;
import data.EncoderData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EncoderTransmission implements Transmission {

    private ObservableList<EncoderData> encoderHistory;
    private DeadReckoner deadReckoner;

    public EncoderTransmission() {
        encoderHistory = FXCollections.observableList(new ArrayList<EncoderData>());
        deadReckoner = new DeadReckoner(encoderHistory);
    }

    @Override
    public void processTransmission(ArrayList<Integer> signal) {
        try {
            EncoderData data = new EncoderData(signal.get(0), signal.get(1), signal.get(2));
            encoderHistory.add(data);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DeadReckoner getDeadReckoner() {
        return deadReckoner;
    }
}
