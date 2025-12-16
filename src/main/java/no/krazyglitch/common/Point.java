package no.krazyglitch.common;

public record Point(long x, long y) {
    public Point add(final Point other) {
        return new Point(x + other.x(), y + other.y());
    }

    public Point sub(final Point other) {
        return new Point(x - other.x(), y - other.y());
    }
}
