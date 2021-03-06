package data;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 * Accepts input of the encoderHistory count for left and right wheels and converts it to path in cartesian coordinates.
 */
public class DeadReckoner {

    // Encoder series for each iteration
    private final ObservableList<EncoderData> encoderHistory;

    // 2D cartesian plane will store the location that the robot will follow.
    private XYChart.Series<Double, Double> series;

    double radius; // wheel radius
    double length; // length between left to right wheel
    double thetaCurr; // current theta in radians
    double thetaPrev; // previous theta in radians
    double xCurr, yCurr;

    static final double TICKS_PER_REV = 360;

    public DeadReckoner(ObservableList<EncoderData> encoderHistory) {
        this.encoderHistory = encoderHistory;
        this.radius = GlobalConstants.WHEEL_RADIUS;
        thetaCurr = 0;
        xCurr = 0;
        yCurr = 0;
        series = new XYChart.Series<Double, Double>();
        addChangeListener();
    }

    public DeadReckoner(ObservableList<EncoderData> encoderHistory, double wheelRadius, double length) {
        this(encoderHistory);
        this.radius = wheelRadius;
        this.length = length;
    }

    private void addChangeListener() {
        encoderHistory.addListener((ListChangeListener)(c -> {
            EncoderData encoderData = encoderHistory.get(encoderHistory.size());
            ObservableList<XYChart.Data<Double, Double>> list = series.getData();

            double[] change = computeLocationChange(encoderData);
            double prevX = list.get(list.size()-1).getXValue();
            double prevY = list.get(list.size()-1).getYValue();
            double currX = prevX + change[0];
            double currY = prevY + change[1];

            list.add(new XYChart.Data<>(currX, currY));
        }));
    }

    private double[] computeLocationChange(EncoderData encoderData) {
        int ticksLeft = encoderData.getLeftTicks();
        int ticksRight = encoderData.getRightTicks();
        int dt = encoderData.getElapsedTime();

        double omega_left, omega_right, velocityLeft, velocityRight, velocity, omega;
        double k00, k01, k02, k10, k11, k12, k20, k21, k22, k30, k31, k32;
        double xNext, yNext, thetaNext, dx, dy, dtheta;

        // Angular velcoities of the wheels
        omega_left = ticksLeft / dt / TICKS_PER_REV * 2 * Math.PI;
        omega_right = ticksRight / dt / TICKS_PER_REV * 2 * Math.PI;

        // Calculate wheel linear velocity.
        velocityLeft = omega_left*radius;
        velocityRight = omega_right*radius;

        // Linear velocity and angular velcotiy of robot.
        velocity = (velocityLeft + velocityRight) / 2;
        omega = (velocityRight - velocityLeft) / length;

        // Use Runge-Kutta integration
        k00 = velocity * Math.cos(thetaPrev);
        k01 = velocity * Math.sin(thetaPrev);
        k02 = omega;

        k10 = velocity * Math.cos(thetaPrev + dt/2*k02);
        k11 = velocity * Math.sin(thetaPrev + dt/2*k02);
        k12 = omega;

        k20 = velocity * Math.cos(thetaPrev + dt/2*k12);
        k21 = velocity * Math.sin(thetaPrev + dt/2*k12);
        k22 = omega;

        k30 = velocity * Math.cos(thetaPrev + dt*k22);
        k31 = velocity * Math.sin(thetaPrev + dt*k22);
        k32 = omega;

        xNext = xCurr + dt/6*(k00 + 2*(k10 + k20) + k30);
        yNext = yCurr + dt/6*(k01 + 2*(k11 + k21) + k31);
        thetaNext = thetaPrev + dt/6*(k02 + 2*(k12 + k22) + k32);

        dx = xNext - xCurr;
        dy = yNext - yCurr;
        thetaNext = thetaNext - thetaPrev;

        xCurr = xNext;
        yCurr = yNext;
        thetaCurr = thetaNext;

        return new double[]{dx, dy};
    }

    public XYChart.Series<Double, Double> getSeries() {
        return series;
    }
}

