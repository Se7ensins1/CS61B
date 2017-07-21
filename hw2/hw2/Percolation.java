package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private int numOpen;
    private boolean[][] grid;
    private WeightedQuickUnionUF topBottom;
    private WeightedQuickUnionUF top;

    /**
     * create N-by-N grid, with all sites initially blocked
     * @param N size of the newly created ground
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("can not create a grid of size N less than 0");
        }
        this.N = N;
        this.numOpen = 0;
        this.grid = new boolean[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                grid[row][col] = false;
            }
        }
        // extra 2 are the virtual top (N+1) and bottom (N+2) sites
        this.topBottom = new WeightedQuickUnionUF(N * N + 2);

        this.top = new WeightedQuickUnionUF(N * N + 1);
    }

    /**
     * open the site (row, col) if it is not open already
     * @param row site's row to open up
     * @param col site's col to open up
     */
    public void open(int row, int col) {
        if (row < 0 || row >= this.N || col < 0 || col >= this.N) {
            throw new IndexOutOfBoundsException("can not find index");
        }
        if (grid[row][col]) {
            return;
        }
        this.numOpen += 1;
        this.grid[row][col] = true;
        int toNum = this.xyTo1D(row, col);

        // if it is on the first row, the site is union with virtual top site
        if (row == 0) {
            this.topBottom.union(toNum, N * N);
            this.top.union(toNum, N * N);
        }

        // if it is on the last row, the site is union with virtual bottom site
        if (row == N - 1) {
            this.topBottom.union(toNum, N * N + 1);
        }

        // checks if it is connected to neighboring sites
        if (row > 0 && this.grid[row - 1][col]) {
            this.topBottom.union(toNum, this.xyTo1D(row - 1, col));
            this.top.union(toNum, this.xyTo1D(row - 1, col));
        }
        if (row < N - 1 && this.grid[row + 1][col]) {
            this.topBottom.union(toNum, this.xyTo1D(row + 1, col));
            this.top.union(toNum, this.xyTo1D(row + 1, col));
        }
        if (col > 0 && this.grid[row][col - 1]) {
            this.topBottom.union(toNum, this.xyTo1D(row, col - 1));
            this.top.union(toNum, this.xyTo1D(row, col - 1));
        }
        if (col < N - 1 && this.grid[row][col + 1]) {
            this.topBottom.union(toNum, this.xyTo1D(row, col + 1));
            this.top.union(toNum, this.xyTo1D(row, col + 1));
        }
    }

    /**
     * is the site (row, col) open?
     * @param row site's row to check if open
     * @param col site's row to check if open
     * @return true if the site is open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= this.N || col < 0 || col >= this.N) {
            throw new IndexOutOfBoundsException("can not find index");
        }
        return grid[row][col];
    }

    /**
     * is the site (row, col) full?
     * @param row site's row to check if full
     * @param col site's row to check if full
     * @return true if site is full, false otherwise
     */
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= this.N || col < 0 || col >= this.N) {
            throw new IndexOutOfBoundsException("can not find index");
        }
        // if it connects with the virtual top row, it is full
        return this.top.connected(this.xyTo1D(row, col), N * N);
    }

    /**
     * number of open sites
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return this.numOpen;
    }

    /**
     * does the system percolate?
     * @return true is the system percolates, false otherwise
     */
    public boolean percolates() {
        return this.topBottom.connected(N * N, N * N + 1);
    }

    private int xyTo1D(int r, int c) {
        return (this.N * r) + c;
    }
}
