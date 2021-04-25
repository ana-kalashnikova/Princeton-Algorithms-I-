import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;


public class Solver {

    private Board twinBoard;
    private SearchNode searchNode;
    private SearchNode twinSearchNode;
    private SearchNode lastNode;
    private SearchNode lastTwinNode;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        this.twinBoard = initial.twin();
        searchNode = new SearchNode(initial, 0, null);
        twinSearchNode = new SearchNode(twinBoard, 0, null);

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        pq.insert(searchNode);
        pqTwin.insert(twinSearchNode);

//        remove the first node
        SearchNode removed = pq.delMin();
        SearchNode removedTwin = pqTwin.delMin();

        lastNode = removed;
        lastTwinNode = removedTwin;

        while (removed.board.isGoal() == false && removedTwin.board.isGoal() == false) {

            removed = nextBoard(pq, removed);
            removedTwin = nextBoard(pqTwin, removedTwin);

            lastNode = removed;
            lastTwinNode = removedTwin;
        }

        if (removed.board.isGoal() == true) {
            solvable = true;
        } else {
            solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if (!isSolvable()) return -1;
        return lastNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if (isSolvable()) {
           Stack<Board> sequence = new Stack<>();
           SearchNode current = lastNode;
           while (current != null) {
               sequence.push(current.board);
               current = current.previousSearchNode;
           }
           return sequence;
        } else {
            return null;
        }
    }

    private SearchNode nextBoard(MinPQ<SearchNode> pq, SearchNode removed){
//            add the neighbors of the removed board to pq
        Iterable<Board> neighbors = removed.board.neighbors();

        for (Board b : neighbors) {
            if (removed.previousSearchNode != null) {
                if (b.equals(removed.previousSearchNode.board)) continue;
            }
            pq.insert(new SearchNode(b, removed.getMoves() + 1, removed));
        }
        return pq.delMin();
    }


    private static class SearchNode implements Comparable<SearchNode>{

        private Board board;
        private int moves;
        private SearchNode previousSearchNode;
//        manhattan/hamming + moves
        private int priority;

        public SearchNode(Board board, int moves, SearchNode previousSearchNode){
            this.board = board;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
            this.previousSearchNode = previousSearchNode;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public void setMoves(int moves) {
            this.moves = moves;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

    //      compare states based on their Hamming or Manhattan priorities
        @Override
        public int compareTo(SearchNode node) {
            if (this.priority < node.priority) {
                return -1;
            } else if (this.priority > node.priority) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // test client (see below)
    public static void main(String[] args){

    }

}
