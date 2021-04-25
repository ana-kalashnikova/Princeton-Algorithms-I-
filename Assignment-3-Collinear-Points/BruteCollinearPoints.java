import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

// Write a program BruteCollinearPoints.java that examines 4 points at a time
// and checks whether they all lie on the same line segment, returning all such line segments.
// To check whether the 4 points p, q, r, and s are collinear,
// check whether the three slopes between p and q, between p and r, and between p and s are all equal.

//The method segments() should include each line segment containing 4 points exactly once.
// If 4 points appear on a line segment in the order p→q→r→s,
// then you should include either the line segment p→s or s→p (but not both)
// and you should not include subsegments such as p→r or q→r.
// For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
//
//Corner cases. Throw an IllegalArgumentException if the argument to the constructor is null,
// if any point in the array is null, or if the argument to the constructor contains a repeated point.


public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;
    private Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException();
        }
        this.points = points;
        validate();
        createSegments();
    }

    // To check whether the 4 points p, q, r, and s are collinear,
    // check whether the three slopes between p and q, between p and r, and between p and s are all equal.
    private void createSegments(){
        segments = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++){
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[k]) == points[i].slopeTo(points[l])){
                            lineSegment(i, j, k, l);
                        }
                    }
                }
            }
        }
    }

    //  compare points to find 2 end points on the LineSegment
    private void lineSegment(int i, int j, int k, int l){
        Point[] array = {points[i], points[j], points[k], points[l]};
        Arrays.sort(array);
        segments.add(new LineSegment(array[0], array[3]));
    }

    // the number of line segments
    public int numberOfSegments(){
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

//  check duplicates and nulls
    private void validate(){
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
    }


    public static void main(String args[]){

        In in = new In("input6.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        Arrays.stream(bruteCollinearPoints.segments()).forEach(System.out::println);
    }

}

