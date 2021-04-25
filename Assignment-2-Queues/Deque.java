import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
       first = null;
       last = null;
       size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

//        if the deque is empty, add first node, set next and previous to null,
//        the first node is also the last if the size is 0
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.previous = null;
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            oldFirst.previous = first;
            first.next = oldFirst;
            first.previous = null;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (isEmpty()) {
            addFirst(item);
        } else {
            Node prev = last;
            last = new Node();
            prev.next = last;
            last.item = item;
            last.next = null;
            last.previous = prev;
            size++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item firstItem = first.item;
        if (size() > 1){
            first = first.next;
            first.previous = null;
        }
        size--;
        return firstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item lastItem = last.item;
        if (size() > 1){
            last = last.previous;
            last.next = null;
        }
        size--;
        return lastItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        String name1 = "Harry";
        String name2 = "Ron";
        String name3 = "Hermione";
        String name4 = "Sirius";

        deque.addFirst(name1);
        deque.addLast(name2);
        System.out.println(deque.size());

        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.isEmpty());

        deque.addFirst(name1);
        deque.addLast(name2);
        deque.addLast(name3);
        deque.addLast(name4);

        for (String s : deque) {
            System.out.println(s);
        }

        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());

    }

}
