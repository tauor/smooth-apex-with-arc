import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


public class Draw extends JPanel {

    public static class IntersectionResult {
        public Point2D intersectionPoint;
        public double averageDistance;

        public IntersectionResult(Point2D intersectionPoint, double averageDistance) {
            this.intersectionPoint = intersectionPoint;
            this.averageDistance = averageDistance;
        }
    }

    public static void draw(Graphics g, Point2D[] points){
        Graphics2D g2d = (Graphics2D) g;

        if (points.length != 3) {
            throw new IllegalArgumentException("Exactly 3 points are required.");
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine((int) points[0].getX(), (int) points[0].getY(), (int) points[1].getX(), (int) points[1].getY());
        g2d.drawLine((int) points[1].getX(), (int) points[1].getY(), (int) points[2].getX(), (int) points[2].getY());

        g2d.setColor(Color.RED);
        Point2D bisectorIntersection = calculateIntersection(points).intersectionPoint;
        double radius = calculateIntersection(points).averageDistance;
        g2d.draw(drawCircle(bisectorIntersection, radius));

    }

    public static IntersectionResult calculateIntersection(Point2D[] points) {
        Point2D p0 = points[0];
        Point2D p1 = points[1];
        Point2D p2 = points[2];

        // Calculate the midpoints of the line segments
        Point2D midpoint1 = new Point2D.Double((p0.getX() + p1.getX()) / 2, (p0.getY() + p1.getY()) / 2);
        Point2D midpoint2 = new Point2D.Double((p2.getX() + p1.getX()) / 2, (p2.getY() + p1.getY()) / 2);

        // Calculate the slopes of the line segments
        double slope1 = (p1.getX() - p0.getX() == 0) ? Double.POSITIVE_INFINITY : (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        double slope2 = (p1.getX() - p2.getX() == 0) ? Double.POSITIVE_INFINITY : (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());

        // Calculate the negative reciprocals of the slopes to get the slopes of perpendicular bisectors
        double perpendicularSlope1 = (slope1 == 0) ? Double.POSITIVE_INFINITY : -1 / slope1;
        double perpendicularSlope2 = (slope2 == 0) ? Double.POSITIVE_INFINITY : -1 / slope2;

        // Calculate the equations of the perpendicular bisectors in point-slope form
        double intercept1 = midpoint1.getY() - perpendicularSlope1 * midpoint1.getX();
        double intercept2 = midpoint2.getY() - perpendicularSlope2 * midpoint2.getX();

        // Check if line 1 is vertical
        if (slope1 == Double.POSITIVE_INFINITY) {
            double intersectionX = midpoint1.getX();
            double intersectionY = perpendicularSlope2 * intersectionX + intercept2;
            double distance1 = midpoint1.distance(intersectionX, intersectionY);
            double distance2 = midpoint2.distance(intersectionX, intersectionY);
            double averageDistance = (distance1 + distance2) / 2;
            return new IntersectionResult(new Point2D.Double(intersectionX, intersectionY), averageDistance);
        }

        // Check if line 2 is vertical
        if (slope2 == Double.POSITIVE_INFINITY) {
            double intersectionX = midpoint2.getX();
            double intersectionY = perpendicularSlope1 * intersectionX + intercept1;
            double distance1 = midpoint1.distance(intersectionX, intersectionY);
            double distance2 = midpoint2.distance(intersectionX, intersectionY);
            double averageDistance = (distance1 + distance2) / 2;
            return new IntersectionResult(new Point2D.Double(intersectionX, intersectionY), averageDistance);
        }

        // Check if line 1 is horizontal
        if (slope1 == 0) {
            double intersectionY = midpoint1.getY();
            intercept2 = midpoint2.getY() - perpendicularSlope2 * midpoint2.getX();
            double intersectionX = (intersectionY - intercept2) / perpendicularSlope2;
            double distance1 = midpoint1.distance(intersectionX, intersectionY);
            double distance2 = midpoint2.distance(intersectionX, intersectionY);
            double averageDistance = (distance1 + distance2) / 2;
            return new IntersectionResult(new Point2D.Double(intersectionX, intersectionY), averageDistance);
        }

        // Check if line 2 is horizontal
        if (slope2 == 0) {
            double intersectionY = midpoint2.getY();
            intercept1 = midpoint1.getY() - perpendicularSlope1 * midpoint1.getX();
            double intersectionX = (intersectionY - intercept1) / perpendicularSlope1;
            double distance1 = midpoint1.distance(intersectionX, intersectionY);
            double distance2 = midpoint2.distance(intersectionX, intersectionY);
            double averageDistance = (distance1 + distance2) / 2;
            return new IntersectionResult(new Point2D.Double(intersectionX, intersectionY), averageDistance);
        }

        // Calculate the x-coordinate of the intersection point
        double intersectionX = (intercept2 - intercept1) / (perpendicularSlope1 - perpendicularSlope2);

        // Calculate the y-coordinate of the intersection point
        double intersectionY = perpendicularSlope1 * intersectionX + intercept1;

        double distance1 = midpoint1.distance(intersectionX, intersectionY);
        double distance2 = midpoint2.distance(intersectionX, intersectionY);
        double averageDistance = (distance1 + distance2) / 2;

        return new IntersectionResult(new Point2D.Double(intersectionX, intersectionY), averageDistance);
    }



    private static Ellipse2D drawCircle (Point2D center, double radius){
        int x = (int) (center.getX() - radius);
        int y = (int) (center.getY() - radius);

        return new Ellipse2D.Double(x, y, 2 * radius, 2 * radius);
    }

}
