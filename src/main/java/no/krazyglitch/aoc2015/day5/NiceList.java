package no.krazyglitch.aoc2015.day5;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class NiceList {

    private static final Predicate<String> isNice = s ->
            s.matches(".*[aeiou].*[aeiou].*[aeiou].*") &&
            s.matches(".*(\\w)\\1+.*") &&
            !s.matches(".*(ab|cd|pq|xy).*");

    private static final Predicate<String> isSilly = s ->
            s.matches(".*(\\w\\w).*(\\1+).*") &&
            s.matches(".*(\\w).(\\1+).*");

    public NiceList() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("Santa's list contains %d nice words\n", findNiceWords(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("Santa's list contains %d silly words\n", findSillyWords(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int findNiceWords(final List<String> data) {
        return (int) data.stream()
                .filter(isNice)
                .count();
    }

    public static int findSillyWords(final List<String> data) {
        return (int) data.stream()
                .filter(isSilly)
                .count();
    }

    public static void main(final String[] args) {
        new NiceList();
    }
}
