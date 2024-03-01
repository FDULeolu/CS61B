package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    private int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state++;
        return normalize(state);
    }

    private double normalize(int x) {
        return (double) x * 2 / period - 1;
    }
}
