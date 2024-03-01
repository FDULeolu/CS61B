package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    private int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    private double normaize() {
        return (state % period) / period * 2 - 1;
    }

    @Override
    public double next() {
        state++;
        return normaize();
    }
}
