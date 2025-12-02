package no.krazyglitch.aoc2025.day2;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class IDChecker {

    private static final Pattern DUPLICATE_PATTERN = Pattern.compile("^(\\d+)\\1$");
    private static final Pattern REPEATING_PATTERN = Pattern.compile("^(\\d+)\\1+$");

    public IDChecker() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The total sum of invalid IDs with mirrored patterns is %d%n", getSumOfDuplicateInvalidIDs(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The total sum of invalid IDs with repeating patterns %d%n", getSumOfRepeatingInvalidIDs(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long getSumOfDuplicateInvalidIDs(final String data) {
        return Arrays.stream(data.split(","))
                .flatMapToLong(IDChecker::getIDsInRange)
                .mapToObj(String::valueOf)
                .filter(IDChecker::isDuplicatePattern)
                .mapToLong(Long::parseLong)
                .sum();
    }

    public static long getSumOfRepeatingInvalidIDs(final String data) {
        return Arrays.stream(data.split(","))
                .flatMapToLong(IDChecker::getIDsInRange)
                .mapToObj(String::valueOf)
                .filter(IDChecker::isRepeatingPattern)
                .mapToLong(Long::parseLong)
                .sum();
    }

    private static LongStream getIDsInRange(final String input) {
        final String[] inputArray = input.split("-");
        return getIDsInRange(inputArray[0], inputArray[1]);
    }

    private static LongStream getIDsInRange(final String start, final String end) {
        return LongStream.rangeClosed(Long.parseLong(start), Long.parseLong(end));
    }

    private static boolean isDuplicatePattern(final String data) {
        return isMatchingPattern(data, DUPLICATE_PATTERN);
    }

    private static boolean isRepeatingPattern(final String data) {
        return isMatchingPattern(data, REPEATING_PATTERN);
    }

    private static boolean isMatchingPattern(final String data, final Pattern pattern) {
        return pattern.matcher(data).matches();
    }

    static void main() {
        new IDChecker();
    }
}
