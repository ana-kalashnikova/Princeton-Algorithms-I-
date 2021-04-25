import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF unionFind;
    private final int n;
    private final int[] array;
    private int openSites;
    private final int virtualTopSite;
    private final int virtualBottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        openSites = 0;
        array = new int[(n * n) + 1];
        unionFind = new WeightedQuickUnionUF((n * n) + 2);

//       set all values negative as closed
        for (int i = 1; i < array.length; i++) {
            array[i] = -i;
        }

        virtualTopSite = 0;
        virtualBottomSite = (n * n) + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if(invalidSite(row, col)) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
//                get the array value based on row and col
//                to open a site make a value positive
            int converted = Math.abs(convert(row, col));
            array[converted] = Math.abs(array[converted]);
            openSites++;
        }
//          if it's a top row, connect to the virtualTopSite
        if (row == 1) {
            unionFind.union(convert(row, col), virtualTopSite);
        }
//          if it's a bottom row, connect to the virtualBottomSite
        if (row == n) {
            unionFind.union(convert(row, col), virtualBottomSite);
        }
//            connect site to neighbors:
//            check if it's not a top row,
//            and then check if the site in the top row is open
        if (row != 1 && isOpen(row - 1, col)) {
            unionFind.union(convert(row - 1, col), convert(row, col));
        }
//            check if its not the last row,
//            and then check if the site on the previous row is open
        if (row != n && isOpen(row + 1, col)) {
            unionFind.union(convert(row + 1, col), convert(row, col));
        }
//            check if its not the last col,
//            and then check if the site in the next col is open
        if (col != n && isOpen(row, col + 1)) {
            unionFind.union(convert(row, col + 1), convert(row, col));
        }
//            check if its not the first col,
//            and then check if the site in the previous col is open
        if (col != 1 && isOpen(row, col - 1)) {
            unionFind.union(convert(row, col - 1), convert(row, col));
        }
    }


    // is the site (row, col) open?
    // the site is opened if it's positive or 0
    public boolean isOpen(int row, int col) {
        if(invalidSite(row, col)) {
            throw new IllegalArgumentException();
        }
        return convert(row, col) >= 0;
    }

    //is the site (row, col) full?
    // the site is full if it is connected to the virtualTopSite
    public boolean isFull(int row, int col) {
        if(invalidSite(row, col)) {
            throw new IllegalArgumentException();
        }
        return unionFind.find(Math.abs(convert(row, col))) == unionFind.find(virtualTopSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }

    // does the system percolate?
    //the sytem percolates if the virtualTopSite is connected to the virtualBottomSite
    public boolean percolates() {
        return unionFind.find(virtualTopSite) == unionFind.find(virtualBottomSite);
    }

    private boolean invalidSite(int row, int col) {
        return (row < 1 || row > n) || (col < 1 || col > n);
    }

    //return a value from array that corresponds to the value in hypothetical 2D array
    // based on given row and col
    private int convert(int row, int col) {
        return array[((row - 1) * n) + col];
    }

//     test client (optional)
    public static void main(String[] args) {
        int n = 3;
        Percolation percolation = new Percolation(n);
        while(!percolation.percolates()){
//          generate random row
            int randomRow = StdRandom.uniform(1, n + 1);
//          generate random column
            int randomCol = StdRandom.uniform(1, n + 1);
            percolation.open(randomRow, randomCol);
        }

    }
}
