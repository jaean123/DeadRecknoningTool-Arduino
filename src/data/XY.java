package data;

public class XY {
    private double x;
    private double y;

    public XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public XY add(XY xy) {
        return new XY(x += xy.getX(), y += xy.getY());
    }
}
