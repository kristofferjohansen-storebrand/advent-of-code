package no.krazyglitch.aoc2025.day9;

import no.krazyglitch.common.Point;
import no.krazyglitch.common.Rectangle;
import no.krazyglitch.util.ArrayUtil;
import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Theatre {

    private static final Comparator<Rectangle> RECTANGLE_COMPARATOR = Comparator.comparing(Rectangle::getArea);

    public Theatre() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The largest possible rectangle is %d%n", getLargestRectangle(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            //System.out.printf("The product of the two last X coordinates is %d%n", frazzled(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long getLargestRectangle(final List<String> data) {
        final List<Point> points = parsePoints(data);
        final Set<Pair> pointPairs = new HashSet<>();
        final SortedSet<Rectangle> rectangles = new TreeSet<>(RECTANGLE_COMPARATOR);
        for (final Point outer : points) {
            for (final Point inner : points) {
                if (inner.equals(outer) || pointPairs.contains(new Pair(outer, inner))) {
                    continue;
                }

                pointPairs.add(new Pair(outer, inner));
                rectangles.add(new Rectangle(outer, inner));
            }
        }

        return (long) rectangles.last().getArea();
    }

    private static List<Point> parsePoints(final List<String> data) {
        return data.stream()
                .map(line -> line.split(","))
                .map(ArrayUtil::toIntArray)
                .map(a -> new Point(a[0], a[1]))
                .toList();
    }

    static void main() {
        new Theatre();
    }

    private record Pair(Point a, Point b) {
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Pair(final Point a1, final Point b1))) return false;
            return (Objects.equals(a, a1) && Objects.equals(b, b1)) || (Objects.equals(a, b1) && Objects.equals(b, a1));
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}
