import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FastCollinearPoints {

    private Point[] points;
    private ArrayList<LineSegment> segments;

    // finds all line segments containing 4 or more points
//    Throw an IllegalArgumentException if the argument to the constructor is null,
//    if any point in the array is null, or if the argument to the constructor contains a repeated point.
    public FastCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException();
        }
        this.points = points;
        Arrays.sort(points);
//      check for duplicates and nulls
        validate();

        segments = new ArrayList<>();

        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        Comparator<Point> comparator;
        ArrayList<Point> segmentPoints;

//        sort by points, sort by slopes
//        where the slopes are equal the points will be sorted in descending order

        for (int i = 0; i < points.length; i++) {
            comparator = points[i].slopeOrder();
//            ???? what for
//            Arrays.sort(pointsCopy);
            Arrays.sort(pointsCopy, comparator);
            int j;
            for (j = 0; j < pointsCopy.length; j++) {
                if (points[i].equals(pointsCopy[j])) continue;
                double slope = points[i].slopeTo(pointsCopy[j]);


                segmentPoints = new ArrayList<>();
                segmentPoints.add(points[i]);
                segmentPoints.add(pointsCopy[j]);
                int k = j + 1;

                while (k < pointsCopy.length && points[i].slopeTo(pointsCopy[k]) == slope) {
                    if (points[i].equals(pointsCopy[k])) {
                        k++;
                        continue;
                    }
                    segmentPoints.add(pointsCopy[k]);
                    k++;
                }

                if (segmentPoints.size() >= 4) {
                    Collections.sort(segmentPoints);
                    if (segmentPoints.get(0).compareTo(points[i]) == 0){
                        segments.add(new LineSegment(segmentPoints.get(0), segmentPoints.get(segmentPoints.size() - 1)));
                    }
                }
                j = k - 1;
            }

        }

    }

    private void validate(){
        if (points[0] == null) throw new IllegalArgumentException();
        for (int i = 1; i < points.length; i++){
            if (points[i] == null) throw new IllegalArgumentException();
            if (points[i].compareTo(points[i - 1]) == 0) throw new IllegalArgumentException();
        }
    }

    // the number of line segments
    public int numberOfSegments(){
        return segments.size();
    }

    public LineSegment[] segments(){
        return segments.toArray(new LineSegment[0]);
    }
}
