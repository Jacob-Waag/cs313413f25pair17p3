package edu.luc.etl.cs313.android.shapes.model;

/**
 * A point, implemented as a location without a shape.
 */
public class Point extends Location {

    public Point(final int x, final int y) {
        // Use a Circle of radius 0 as the shape per spec
        super(x, y, new Circle(0));
    }
}
