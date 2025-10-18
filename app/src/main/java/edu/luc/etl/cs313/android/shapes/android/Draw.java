package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import edu.luc.etl.cs313.android.shapes.model.*;
import java.util.List;

// Visitor implementation
// each shape accepts this visitor so Draw can call the right method

public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());

        // Critical: if immediate child is StrokeColor, must call onStrokeColor now to set paint color
        if (l.getShape() instanceof StrokeColor) {
            ((StrokeColor) l.getShape()).accept(this);
        } else if (l.getShape() != null) {
            l.getShape().accept(this);
        }

        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        if (g.getShapes() != null) {
            for (Shape s : g.getShapes()) {
                if (s != null) {
                    s.accept(this);
                }
            }
        }
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        if (f.getShape() != null) {
            f.getShape().accept(this);
        }
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int oldColor = paint.getColor();
        paint.setColor(c.getColor());
        if (c.getShape() != null) {
            c.getShape().accept(this);
        }
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        if (o.getShape() != null) {
            o.getShape().accept(this);
        }
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon p) {
        List<? extends Point> pts = p.getPoints();
        if (pts == null || pts.size() < 2) return null;

        float[] lines = new float[pts.size() * 4]; // Each line: startX, startY, endX, endY
        for (int i = 0; i < pts.size(); i++) {
            Point start = pts.get(i);
            Point end = pts.get((i + 1) % pts.size()); // Wrap around for closing
            lines[i * 4] = start.getX();
            lines[i * 4 + 1] = start.getY();
            lines[i * 4 + 2] = end.getX();
            lines[i * 4 + 3] = end.getY();
        }
        canvas.drawLines(lines, paint);
        return null;
    }

}