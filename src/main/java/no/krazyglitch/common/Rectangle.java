package no.krazyglitch.common;

import java.util.List;

public class Rectangle {
    private final Point a, b, c, d;

    public Rectangle(final Point start, final Point end) {
        this(start, new Point(start.x(), end.y()), end, new Point(end.x(), start.y()));
    }

    public Rectangle (final Point a, final Point b, final Point c, final Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public double getArea(final boolean includeBound) {
        final long constant = includeBound ? 1L : 0L;

        return (new Vector(a, b).length() + constant) * (new Vector(b, c).length() + constant);
    }

    public double getArea() {
        return getArea(true);
    }

    public List<Point> getPoints() {
        return List.of(a, b, c, d);
    }
}
