package edu.luc.etl.cs313.android.shapes.model;\

// this Visitor counts how many Shape objects exist in a structure.
// each shape type contributes a count value that gets combined
// across nested decorators, locations, and groups.
public class Count implements Visitor<Integer> {

    @Override
    public Integer onCircle(final Circle c) {
        return 1;
    }

    @Override
    public Integer onRectangle(final Rectangle r) {
        return 1;
    }

    @Override
    public Integer onPolygon(final Polygon p) {
        if (p.getPoints() == null || p.getPoints().isEmpty()) return 0;
        return 1; // count entire polygon as one shape (not points)
    }

    @Override
    public Integer onGroup(final Group g) {
        if (g.getShapes() == null || g.getShapes().isEmpty()) return 0;
        int sum = 0;
        for (Shape s : g.getShapes()) {
            if (s != null) {
                Integer count = s.accept(this);
                sum += (count != null ? count : 0);
            }
        }
        return sum;
    }

    @Override
    public Integer onLocation(final Location l) {
        if (l == null || l.getShape() == null) return 0;
        Integer count = l.getShape().accept(this);
        return (count != null) ? count : 0;
    }

    @Override
    public Integer onFill(final Fill f) {
        if (f == null || f.getShape() == null) return 0;
        Integer count = f.getShape().accept(this);
        return (count != null) ? count : 0;
    }

    @Override
    public Integer onStrokeColor(final StrokeColor s) {
        if (s == null || s.getShape() == null) return 0;
        Integer count = s.getShape().accept(this);
        return (count != null) ? count : 0;
    }

    @Override
    public Integer onOutline(final Outline o) {
        if (o == null || o.getShape() == null) return 0;
        Integer count = o.getShape().accept(this);
        return (count != null) ? count : 0;
    }
}