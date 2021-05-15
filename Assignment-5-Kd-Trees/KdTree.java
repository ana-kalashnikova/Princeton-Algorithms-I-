import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final double HEIGHT = 1;
    private static final double WIDTH = 1;
    private Node root;
    private int n;

    // construct an empty set of points
    public KdTree() {
        root = null;
        n = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // number of points in the set
    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, true, null);
    }

    private Node insert(Node x, Point2D p, boolean vertical, Node parent) {
        if (x == null) {
            n ++;
            return computeRect(p, parent, vertical);
        }
//        if x-aligned (vertical), than compare x
        int cmp;
        if (vertical) {
//            vertical, so compare X
            cmp = compareXY(p.x(), x.p.x());
//            if cmp < 0 , insert on the left side
            if (cmp < 0) {
                x.lb  = insert(x.lb, p, false, x);
//            if cmp >= 0 insert on the right side
            } else if (cmp >= 0) {
                x.rt = insert(x.rt, p, false, x);
            }
//        if y-aligned (horizontal), compare y
        } else {
//            horizontal, so compare Y
            cmp = compareXY(p.y(), x.p.y());
            if (cmp < 0) {
//               if cmp < 0, insert to the left (bottom of the x.p())
                x.lb  = insert(x.lb, p, true, x);
//               if cmp >= 0, insert to the right (top of the x.p())
            } else if (cmp >= 0) {
                x.rt = insert(x.rt, p, true, x);
            }
        }
        return x;
    }

    private Node computeRect(Point2D p, Node parent, boolean vertical) {
//            if its the first node in the tree
        if (parent == null) {
            return new Node(p,
                    new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                    null, null);
        }
//            if the child Node is vertical determine if its the left or the right child
        if (vertical) {
//            if the Node is the right child of the parent
            if (p.y() >= parent.p.y()) {
                double xmin = parent.rect.xmin();
                double ymin = parent.p.y();
                double xmax = parent.rect.xmax();
                double ymax = parent.rect.ymax();
                return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
//            if the child Node is the left child of the parent
            } else {
                double xmin = parent.rect.xmin();
                double ymin = parent.rect.ymin();
                double xmax = parent.rect.xmax();
                double ymax = parent.p.y();
                return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
            }
//            if the child Node is Horizontal, determine if its the left(top) or the right(bottom) child
        } else {
//            if the Node is the right(top) child of the parent
            if (p.x() >= parent.p.x()) {
                double xmin = parent.p.x();
                double ymin = parent.rect.ymin();
                double xmax = parent.rect.xmax();
                double ymax = parent.rect.ymax();
                return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
//            if the Node is the left(bottom) child of the parent
            } else {
                double xmin = parent.rect.xmin();
                double ymin = parent.rect.ymin();
                double xmax = parent.p.x();
                double ymax = parent.rect.ymax();
                return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
            }
        }
    }

    private int compareXY(double point, double parent) {
        if (point < parent) {
            return -1;
        } else if(point > parent) {
            return  1;
        } else {
            return 0;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(p, root, true);
    }

    private boolean contains(Point2D p, Node x, boolean vertical) {
        if (x == null) return false;
        if (p.equals(x.p)) return true;
//        if X is vertical, compare X coordinate
        if (vertical) {
            int cmp = compareXY(p.x(), x.p.x());
            if (cmp < 0) {
                return contains(p, x.lb, false);
            } else {
                return contains(p, x.rt, false);
            }
//        if X is horizontal, compare Y coordinate
        } else {
            int cmp = compareXY(p.y(), x.p.y());
            if (cmp < 0) {
                return contains(p, x.lb, true);
            } else {
                return contains(p, x.rt, true);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        draw(root, true);
    }

    private void draw(Node x, boolean vertical) {
        if (x == null) return;
        if (vertical) {
            draw(x.lb, false);
            drawVerticalLine(x);
            draw(x.rt, false);
        } else {
            draw(x.lb, true);
            drawHorizontalLine(x);
            draw(x.rt, true);
        }
    }

    private void drawVerticalLine(Node x) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x.p.x(), x.p.y());
        double xmin = x.p.x();
        double ymin = x.rect.ymin();
        double xmax = x.p.x();
        double ymax = x.rect.ymax();
        if (ymin == Double.NEGATIVE_INFINITY) ymin = 0;
        if (ymax == Double.POSITIVE_INFINITY) ymax = HEIGHT;
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(xmin, ymin, xmax, ymax);
    }

    private void drawHorizontalLine(Node x) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x.p.x(), x.p.y());
        double xmin = x.rect.xmin();
        double ymin = x.p.y();
        double xmax = x.rect.xmax();
        double ymax = x.p.y();
        if (xmin == Double.NEGATIVE_INFINITY) xmin = 0;
        if (xmax == Double.POSITIVE_INFINITY) xmax = WIDTH;
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(xmin, ymin, xmax, ymax);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> pointsInRange = new Queue<>();
        range (pointsInRange, rect, root);
        return pointsInRange;
    }

    private void range(Queue<Point2D> pointsInRange, RectHV rect, Node x) {
        if (x == null) return;
        if (x.rect.intersects(rect)) {
            if (rect.contains(x.p)) pointsInRange.enqueue(x.p);
            range(pointsInRange, rect, x.lb);
            range(pointsInRange, rect, x.rt);
        } else {
            return;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        return nearest(p, root, root.p, true);
    }

    private Point2D nearest(Point2D p, Node x, Point2D nearest, boolean vertical) {
//        if the closest point discovered so far is closer than the distance between the query point
//        and the rectangle corresponding to a node there is no need to explore that node (or its subtrees).
        if (x == null || nearest.distanceSquaredTo(p) < x.rect.distanceSquaredTo(p)) {
            return nearest;
        }
        if (x.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = x.p;
        }
        if (vertical) {
//                go left
            if (p.x() < x.p.x()) {
                nearest = nearest(p, x.lb, nearest, false);
                nearest = nearest(p, x.rt, nearest, false);
//                go right
            } else {
                nearest = nearest(p, x.rt, nearest, false);
                nearest = nearest(p, x.lb, nearest, false);
            }
        } else {
//                go bottom
            if (p.y() < x.p.y()) {
                nearest =nearest(p, x.lb, nearest, true);
                nearest =nearest(p, x.rt, nearest, true);
            } else {
//                go top
                nearest =nearest(p, x.rt, nearest, true);
                nearest =nearest(p, x.lb, nearest, true);
            }
        }
        return nearest;
    }

    private class Node {
        // the point
        private final Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private final RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;


        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    public static void main(String [] args) {
    }

}
