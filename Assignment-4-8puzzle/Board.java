import edu.princeton.cs.algs4.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {

    private int n;
    private int[] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw  new IllegalArgumentException();
        this.n = tiles.length;
        board = Stream.of(tiles)
                .flatMapToInt(IntStream::of)
                .toArray();

    }

    // string representation of this board
    public String toString() {
        String s = "";
        s += n;
        for (int i = 0; i < board.length; i += n){
            if (i < board.length) s += "\n";
            for (int j = 0; j < n; j++){
                s += board[i + j] + " ";
            }
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
//    The Hamming distance betweeen a board and the goal board is the number of tiles in the wrong position
    public int hamming() {
        int countHamming = 0;
        for (int i = 0; i < board.length; i++) {
           if (board[i] == 0) continue;
           if (board[i] != i + 1) countHamming++;
        }
        return countHamming;

    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int countManhattan = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) continue;
            int rowActual = i / n;
            int colActual = i % n;
            int rowExpected = (board[i] - 1) / n;
            int colExpected = (board[i] - 1) % n;
            countManhattan += Math.abs((rowActual - rowExpected)) + Math.abs((colActual - colExpected));
        }
        return countManhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < board.length; i++){
            if (board[i] != that.board[i]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int emptySpace = findEmptySpaceIndex();

        int emptySpaceRow = emptySpace / n;
        int emptySpaceCol = emptySpace % n;

        return getNeighbors(emptySpace, emptySpaceRow, emptySpaceCol);
    }

    private int findEmptySpaceIndex(){
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    private Stack<Board> getNeighbors(int emptySpace, int row, int col){
        Stack<Board> neighbors = new Stack<>();
        int numNeighbors = numNeighbors(row, col);
        if (numNeighbors == 2) {
//                first row, first col
            if (row == 0 && col == 0) {
                neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace + n));
//                first row, last col
            } else if (row == 0 && col == n - 1) {
                neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace + n));
//                last row, first col
            } else if (row == n - 1 && col == 0) {
                neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace - n));
//                last row, last col
            } else {
                neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace - n));
            }
        } else if (numNeighbors == 3) {
            if (row == 0) {
                neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace + n));
            } else if (row == n - 1) {
                neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
                neighbors.push(neighborBoard(emptySpace, emptySpace - n));
            } else if (col == 0) {
                neighbors.push(neighborBoard(emptySpace, emptySpace - n));
                neighbors.push(neighborBoard(emptySpace, emptySpace + n));
                neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
            } else {
                neighbors.push(neighborBoard(emptySpace, emptySpace - n));
                neighbors.push(neighborBoard(emptySpace, emptySpace + n));
                neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
            }
        } else {
            neighbors.push(neighborBoard(emptySpace, emptySpace + 1));
            neighbors.push(neighborBoard(emptySpace, emptySpace - 1));
            neighbors.push(neighborBoard(emptySpace, emptySpace + n));
            neighbors.push(neighborBoard(emptySpace, emptySpace - n));
        }
        return neighbors;
    }

    private int numNeighbors(int row, int col){
        int numNeighbors;
        if (row == 0) {
            if (col == 0 || col == n - 1) {
                numNeighbors = 2;
            } else {
                numNeighbors = 3;
            }
        } else if (row == n - 1){
            if (col == 0 || col == n - 1) {
                numNeighbors = 2;
            } else {
                numNeighbors = 3;
            }
        } else if (col == 0 || col == n - 1) {
            numNeighbors = 3;
        } else {
            numNeighbors = 4;
        }
        return numNeighbors;
    }
//  index1 is a position of a black tile, index2 is a position of tile that the blank tile will be exchanged with
    private Board neighborBoard(int index1, int index2){
//        row and col of the blank tile
        int row1 = index1 / n;
        int col1 = index1 % n;
//        row and col of the exchange tile
        int row2 = index2 / n;
        int col2 = index2 % n;

        int [][] neighborBoard = convertToBoard2D();

//      save blank tile in placeholder
        int placeholder = board[index1];
        neighborBoard[row1][col1] = neighborBoard[row2][col2];
        neighborBoard[row2][col2] = placeholder;
        return new Board(neighborBoard);
    }

    private int[][] convertToBoard2D () {
        int [][] neighborBoard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighborBoard[i][j] = board[i * n + j];
            }
        }
        return neighborBoard;
    }

    // a board that is obtained by exchanging any pair of tiles
//    twin has to be always the same and should not modify the board
    public Board twin() {
        int index = 0;
        for (int i = 0; i < board.length; i++){
            if (board[i] != 0 && board[i + 1] != 0) {
//                check if board[i + 1] is on the same row as board[i]
                if ((i + 1) % n != 0) {
                    index = i;
                    break;
                }
            }
        }
        int row = index / n;
        int col = index % n;

        int[][] twinTiles = convertToBoard2D();

        int placeholder = twinTiles[row][col];
        twinTiles[row][col] = twinTiles[row][col + 1];
        twinTiles[row][col + 1] = placeholder;
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
