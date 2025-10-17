package edu.luc.etl.cs313.android.shapes.model;

public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int r = c.getRadius();
        return new Location(-r, -r, new Rectangle(2 * r, 2 * r));
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onLocation(final Location l) {
        if (l.getShape() == null) {
            throw new IllegalStateException("Location shape cannot be null");
        }
        Location inner = l.getShape().accept(this);
        if (inner == null) {
            throw new IllegalStateException("Inner shape bounding box cannot be null");
        }
        Rectangle rect = (Rectangle) inner.getShape();
        return new Location(
                l.getX() + inner.getX(),
                l.getY() + inner.getY(),
                new Rectangle(rect.getWidth(), rect.getHeight())
        );
    }

    @Override
    public Location onGroup(Group g) {
        if (g.getShapes() == null || g.getShapes().isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE, bottom = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()) {
            Location loc = s.accept(this);
            if (loc == null) continue;

            Rectangle rect = (Rectangle) loc.getShape();

            int currLeft = loc.getX();
            int currTop = loc.getY();
            int currRight = currLeft + rect.getWidth();
            int currBottom = currTop + rect.getHeight();

            left = Math.min(left, currLeft);
            top = Math.min(top, currTop);
            right = Math.max(right, currRight);
            bottom = Math.max(bottom, currBottom);
        }

        if (left == Integer.MAX_VALUE || top == Integer.MAX_VALUE) {
            return new Location(0, 0, new Rectangle(0, 0));
        }
        return new Location(left, top, new Rectangle(right - left, bottom - top));
    }

    @Override
    public Location onFill(Fill f) {
        if (f.getShape() == null) {
            return new Location(0, 0, new Rectangle(0, 0));
        }
        Location loc = f.getShape().accept(this);
        return loc != null ? loc : new Location(0, 0, new Rectangle(0, 0));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        if (c.getShape() == null) {
            return new Location(0, 0, new Rectangle(0, 0));
        }
        Location loc = c.getShape().accept(this);
        return loc != null ? loc : new Location(0, 0, new Rectangle(0, 0));
    }

    @Override
    public Location onOutline(final Outline o) {
        if (o.getShape() == null) {
            return new Location(0, 0, new Rectangle(0, 0));
        }
        Location loc = o.getShape().accept(this);
        return loc != null ? loc : new Location(0, 0, new Rectangle(0, 0));
    }

    @Override
    public Location onPolygon(final Polygon p) {
        if (p.getPoints() == null || p.getPoints().isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE, bottom = Integer.MIN_VALUE;

        for (Point pt : p.getPoints()) {
            int x = (int) pt.getX();
            int y = (int) pt.getY();
            left = Math.min(left, x);
            top = Math.min(top, y);
            right = Math.max(right, x);
            bottom = Math.max(bottom, y);
        }

        return new Location(left, top, new Rectangle(right - left, bottom - top));
    }
}