package edu.luc.etl.cs313.android.shapes.model;

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
        if (p.getPoints() == null) return 0;
        return p.getPoints().size();
    }

    @Override
    public Integer onGroup(final Group g) {
        if (g.getShapes() == null) return 0;
        int sum = 0;
        for (Shape s : g.getShapes()) {
            Integer count = s.accept(this);
            if (count != null) {
                sum += count;
            }
        }
        return sum;
    }

    @Override
    public Integer onLocation(final Location l) {
        if (l.getShape() == null) return 0;
        Integer count = l.getShape().accept(this);
        return count != null ? count : 0;
    }

    @Override
    public Integer onFill(final Fill f) {
        if (f.getShape() == null) return 0;
        Integer count = f.getShape().accept(this);
        return count != null ? count : 0;
    }

    @Override
    public Integer onStrokeColor(final StrokeColor s) {
        if (s.getShape() == null) return 0;
        Integer count = s.getShape().accept(this);
        return count != null ? count : 0;
    }

    @Override
    public Integer onOutline(final Outline o) {
        if (o.getShape() == null) return 0;
        Integer count = o.getShape().accept(this);
        return count != null ? count : 0;
    }
}