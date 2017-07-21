package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trials;
    private int size;
    private double[] fracOpen;

    /**
     * perform T independent experiments on an N-by-N grid
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("can not be less than 0");
        }
        this.trials = T;
        this.size = N;
        this.fracOpen = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                int ranX = StdRandom.uniform(0, N);
                int ranY = StdRandom.uniform(0, N);
                perc.open(ranX, ranY);
            }
            this.fracOpen[i] = (double) perc.numberOfOpenSites() / (N * N);
        }
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(this.fracOpen);
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return StdStats.stddev(this.fracOpen);
    }

    /**
     * low  endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLow() {
        return this.mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHigh() {
        return this.mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }
}
