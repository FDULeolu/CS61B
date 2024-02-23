package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int nGrid;
    private Site[][] sites;
    private WeightedQuickUnionUF percolateSite;
    private int numberOfOpenSites;

    private class Site {
        boolean isOpen;

        public Site() {
            isOpen = false;
        }
    }
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        numberOfOpenSites = 0;
        nGrid = N;
        sites = new Site[nGrid][nGrid];
        percolateSite = new WeightedQuickUnionUF(nGrid * nGrid);
        for (int i = 0; i < nGrid; i++) {
            for (int j = 0; j < nGrid; j++) {
                if (i == 0) {
                    percolateSite.union(0, calIndex(0, j));
                }
                sites[i][j] = new Site();
            }
        }
    }

    private void concat(int x1, int y1, int x2, int y2) {
        if (percolateSite.find(calIndex(x1, y1)) == 0) {
            percolateSite.union(calIndex(x1, y1), calIndex(x2, y2));
            // DEBUG
            System.out.println("(" + x2 + ", " + y2 + ") is added to (" + x1 + ", " + y1 + ")");
            // DEBUG
        } else {
            percolateSite.union(calIndex(x2, y2), calIndex(x1, y1));
            // DEBUG
            System.out.println("(" + x1 + ", " + y1 + ") is added to (" + x2 + ", " + y2 + ")");
            // DEBUG
        }
    }

    private boolean checkIndex(int row, int col) {
        if (row >= 0 && row < nGrid && col >= 0 && col < nGrid) {
            return true;
        }
        return false;
    }

    private void argumentChecker(int row, int col) {
        if (row < 0 || row >= nGrid || col < 0 || col >= nGrid) {
            throw new IllegalArgumentException();
        }
    }

    private int calIndex(int row, int col) {
        return row * nGrid + col;
    }

    public void open(int row, int col) {
        argumentChecker(row, col);
        if (!sites[row][col].isOpen) {
            numberOfOpenSites++;
            sites[row][col].isOpen = true;
            if (checkIndex(row - 1, col) && sites[row - 1][col].isOpen) {
                concat(row - 1, col, row, col);
            }
            if (checkIndex(row + 1, col) && sites[row + 1][col].isOpen) {
                concat(row + 1, col, row, col);
            }
            if (checkIndex(row, col - 1) && sites[row][col - 1].isOpen) {
                concat(row, col - 1, row, col);
            }
            if (checkIndex(row, col + 1) && sites[row][col + 1].isOpen) {
                concat(row , col - 1, row, col);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        argumentChecker(row, col);
        return sites[row][col].isOpen;
    }

    public boolean isFull(int row, int col) {
        argumentChecker(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return percolateSite.find(calIndex(row, col)) < nGrid;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        for (int i = 0; i < nGrid; i++) {
            if (percolateSite.find(calIndex(nGrid - 1, i)) < nGrid) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {}
}
