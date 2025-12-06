package no.krazyglitch.aoc2025.day5;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Kitchen {

    private static final Comparator<Series> SERIES_COMPARATOR = Comparator.comparing(Series::start).thenComparing(Series::end);

    public Kitchen() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of fresh ingredients is %d%n", getFreshIngredients(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of all fresh ingredients is %d%n", getAmountOfFreshIngredients(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getFreshIngredients(final List<String> data) {
        final int emptyLinePosition = data.indexOf("");
        final Map<Long, Series> seriesMap = parseSeries(data.subList(0, emptyLinePosition));
        final List<Long> idsToCheck = data.subList(emptyLinePosition + 1, data.size()).stream()
                .mapToLong(Long::parseLong)
                .boxed()
                .toList();

        return (int) idsToCheck.stream()
                .filter(id -> isIngredientFresh(seriesMap, id))
                .count();
    }

    public static long getAmountOfFreshIngredients(final List<String> data) {
        final int emptyLinePosition = data.indexOf("");
        final Map<Long, Series> seriesMap = parseSeries(data.subList(0, emptyLinePosition));
        final List<Series> sortedSeries = seriesMap.values().stream()
                .sorted(SERIES_COMPARATOR)
                .toList();

        final List<Series> mergedSeries = getMergedSeries(sortedSeries);
        return mergedSeries.stream()
                .mapToLong(series -> series.end()-series.start() + 1L)
                .sum();
    }

    private static boolean isIngredientFresh(final Map<Long, Series> seriesMap, final long id) {
        return seriesMap.entrySet().stream()
                .filter(entry -> id >= entry.getKey())
                .anyMatch(entry -> entry.getValue().contains(id));
    }

    private static List<Series> getMergedSeries(final List<Series> originalSeries) {
        final List<Series> mergedSeries = new ArrayList<>();
        mergedSeries.add(originalSeries.getFirst());

        for (final Series current : originalSeries) {
            final Series last = mergedSeries.getLast();

            if (current.start() <= last.end()) {
                final long currEnd = Math.max(last.end(), current.end());
                mergedSeries.removeLast();
                mergedSeries.add(new Series(last.start(), currEnd));
            } else {
                mergedSeries.add(current);
            }
        }

        return mergedSeries;
    }

    private static Map<Long, Series> parseSeries(final List<String> data) {
        return data.stream()
                .map(line -> line.split("-"))
                .map(arr -> new Series(Long.parseLong(arr[0]), Long.parseLong(arr[1])))
                .collect(Collectors.toMap(Series::start, series -> series,
                        (seriesOne, seriesTwo) -> seriesOne.end() >= seriesTwo.end() ? seriesOne : seriesTwo));
    }

    record Series(long start, long end) {
        public boolean contains(final long id) {
            return id >= start && id <= end;
        }
    }

    static void main() {
        new Kitchen();
    }
}
