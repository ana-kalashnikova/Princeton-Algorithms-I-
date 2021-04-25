import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials;
    private final double[] thresholdArray;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        Percolation percolation = new Percolation(n);
        thresholdArray = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
//          generate random row
                int randomRow = StdRandom.uniform(1, n + 1);
//          generate random column
                int randomCol = StdRandom.uniform(1, n + 1);
                percolation.open(randomRow, randomCol);
            }
            thresholdArray[i] = percolation.numberOfOpenSites() / Math.pow(n, 2);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(thresholdArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(thresholdArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96*stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96*stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = 5;
        int T = 10;

        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
        }

        PercolationStats percolationStats = new PercolationStats(n, T);

        System.out.printf("mean    = %f%n", percolationStats.mean());
        System.out.printf("stddev  = %f%n", percolationStats.stddev());
        System.out.printf("95%% confidence interval  = [%f, %f]",
                percolationStats.confidenceLo(), percolationStats.confidenceHi());


    }
}



