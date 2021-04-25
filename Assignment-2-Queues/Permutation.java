import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {

        int k = 0;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        if (args.length > 0) {
            k = Integer.parseInt(args[0]);
            while (!StdIn.isEmpty()) {
                randomizedQueue.enqueue(StdIn.readString());
            }
            for (int i = 0; i < k; i++) {
                System.out.println(randomizedQueue.dequeue());
            }
        }
    }
}
