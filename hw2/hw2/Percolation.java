package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int nGrid;
    private Site[][] sites;
    private WeightedQuickUnionUF percolateSite;
    private WeightedQuickUnionUF percolateSiteWithoutBotton;
    private int numberOfOpenSites;
    private int topSite;
    private int bottomSite;

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
        topSite = N * N;
        bottomSite = N * N + 1;
        percolateSite = new WeightedQuickUnionUF(nGrid * nGrid + 2);
        percolateSiteWithoutBotton = new WeightedQuickUnionUF(nGrid * nGrid + 1);
        for (int i = 0; i < nGrid; i++) {
            for (int j = 0; j < nGrid; j++) {
                if (i == 0) {
                    percolateSite.union(topSite, calIndex(i, j));
                    percolateSiteWithoutBotton.union(topSite, calIndex(i, j));
                }
                if (i == N - 1) {
                    percolateSite.union(bottomSite, calIndex(i, j));
                }
                sites[i][j] = new Site();
            }
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
                percolateSite.union(calIndex(row - 1, col), calIndex(row, col));
                percolateSiteWithoutBotton.union(calIndex(row - 1, col), calIndex(row, col));
            }
            if (checkIndex(row + 1, col) && sites[row + 1][col].isOpen) {
                percolateSite.union(calIndex(row + 1, col), calIndex(row, col));
                percolateSiteWithoutBotton.union(calIndex(row + 1, col), calIndex(row, col));
            }
            if (checkIndex(row, col - 1) && sites[row][col - 1].isOpen) {
                percolateSite.union(calIndex(row, col - 1), calIndex(row, col));
                percolateSiteWithoutBotton.union(calIndex(row, col - 1), calIndex(row, col));
            }
            if (checkIndex(row, col + 1) && sites[row][col + 1].isOpen) {
                percolateSite.union(calIndex(row, col + 1), calIndex(row, col));
                percolateSiteWithoutBotton.union(calIndex(row, col + 1), calIndex(row, col));
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
        return percolateSiteWithoutBotton.connected(topSite, calIndex(row, col));
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        if (numberOfOpenSites == 0) {
            return false;
        }
        return percolateSite.connected(topSite, bottomSite);
    }

    public static void main(String[] args) {}
}
