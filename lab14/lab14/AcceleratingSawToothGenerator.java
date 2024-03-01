package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int period;
    private double periodChangeFactor;

    private int state;
    private int round;

    public AcceleratingSawToothGenerator(int period, double periodChangeFactor) {
        this.period = period;
        this.periodChangeFactor = periodChangeFactor;
        state = 0;
        round = 1;
    }

    private double calGeometricSeries(int round) {
        return period * (1 - Math.pow(periodChangeFactor, round)) / (1 - periodChangeFactor);
    }

    private double normalize() {
        if (state > calGeometricSeries(round)) {
            round++;
        }
        double curPeriod = Math.pow(periodChangeFactor, round - 1) * period;
        double curState = state - calGeometricSeries(round - 1);
        return curState / curPeriod * 2 - 1;
    }

    @Override
    public double next() {
        state++;
        return normalize();
    }
}
