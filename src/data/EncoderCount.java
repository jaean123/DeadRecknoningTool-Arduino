package data;

public class EncoderCount {

    private int leftTicks;
    private int rightTicks;
    private int elapsedTime;

    public EncoderCount(int leftTicks, int rightTicks) {
        this.leftTicks = leftTicks;
        this.rightTicks = rightTicks;
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
