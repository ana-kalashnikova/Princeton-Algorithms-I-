import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item []) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return size;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                count++;
            } else {
                temp[i - count] = a[i];
            }
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (size == a.length) resize(2 * a.length);
        a[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int randomIndex = StdRandom.uniform(a.length);

        while (a[randomIndex] == null) {
            randomIndex = StdRandom.uniform(a.length);
        }

        Item item = a[randomIndex];
        a[randomIndex] = null;
        size--;
        if (size > 0 && size == a.length / 4) resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        int randomIndex = StdRandom.uniform(a.length);

        while (a[randomIndex] == null) {
            randomIndex = StdRandom.uniform(a.length);
        }

        return a[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        int iteratorSize = size;
        Item[] b = createIterator();

        private Item[] createIterator() {
            Item[] b = (Item[]) new Object[size];

            int count = 0;
            for (int i = 0; i < a.length; i++){
                if (a[i] == null) {
                    count++;
                } else {
                    b[i - count] = a[i];
                }
            }
            return b;
        }

        @Override
        public boolean hasNext() {
            return iteratorSize > 0;
        }

        @Override
        public Item next() {
            if (iteratorSize == 0){
                throw new NoSuchElementException();
            }

            int randomIndex = StdRandom.uniform(iteratorSize);
            Item item = b[randomIndex];
            b[randomIndex] = b[iteratorSize - 1];
            iteratorSize--;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        String name1 = "Harry";
        String name2 = "Ron";
        String name3 = "Hermione";
        String name4 = "Sirius";

        randomizedQueue.enqueue(name1);
        randomizedQueue.enqueue(name2);
        randomizedQueue.enqueue(name3);
        randomizedQueue.enqueue(name4);

        for (String s : randomizedQueue) {
            System.out.println(s);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(randomizedQueue.dequeue());
        }
    }
}
