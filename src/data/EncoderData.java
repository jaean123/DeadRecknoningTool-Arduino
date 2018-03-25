package data;

public class EncoderData {

    private int leftTicks;
    private int rightTicks;
    private int elapsedTime;

    public EncoderData(int leftTicks, int rightTicks, int elapsedTime) {
        this.leftTicks = leftTicks;
        this.rightTicks = rightTicks;
        this.elapsedTime = elapsedTime;
    }

    public int getLeftTicks() {
        return leftTicks;
    }

    public int getRightTicks() {
        return rightTicks;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}
