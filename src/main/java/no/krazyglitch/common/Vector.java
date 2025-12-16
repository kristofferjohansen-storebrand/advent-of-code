package no.krazyglitch.common;

public record Vector(Point a, Point b) {
    public double length() {
        final Point sub = b.sub(a);
        return Math.sqrt(Math.pow(sub.x(), 2) + Math.pow(sub.y(), 2));
    }

    public Vector normalized() {
        final Point origin = new Point(0, 0);
        final Point sub = b.sub(a);

        return new Vector(origin, sub);
    }

    public long minX() {
        return Math.min(a.x(), b.x());
    }

    public long maxX() {
        return Math.max(a.x(), b.x());
    }

    public long minY() {
        return Math.min(a.y(), b.y());
    }

    public long maxY() {
        return Math.max(a.y(), b.y());
    }
}
