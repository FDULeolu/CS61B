package hw2;

import edu.princeton.cs.introcs.StdRandom;


public class PercolationStats {

    double mean;
    double result[];
    int rounds;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        result = new double[T];
        rounds = T;
        mean = 0.0;
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int x = StdRandom.uniform(N);
                int y = StdRandom.uniform(N);
                p.open(x, y);
            }
            result[i] = (double) p.numberOfOpenSites() / (N * N);
            mean += result[i];
        }
        mean /= T;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        double squareSum = 0.0;
        for (int i = 0; i < rounds; i++) {
            squareSum += Math.pow(result[i] - mean, 2);
        }
        return squareSum / (rounds - 1);
    }

    public double confidenceLow() {
        return mean - 1.96 * stddev() / Math.sqrt(rounds);
    }

    public double confidenceHigh() {
        return mean + 1.96 * stddev() / Math.sqrt(rounds);
    }

}
