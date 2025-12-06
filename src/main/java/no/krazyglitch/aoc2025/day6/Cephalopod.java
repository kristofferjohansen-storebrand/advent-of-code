package no.krazyglitch.aoc2025.day6;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Cephalopod {

    private static final Map<String, Function<List<Long>, Long>> OPERATOR_MAP = Map.of(
            "*", Cephalopod::multiplyList,
            "+", Cephalopod::addList
    );

    private static final Map<String, Function<List<String>, Long>> RTL_OPERATOR_MAP = Map.of(
            "*", Cephalopod::multiplyRTLList,
            "+", Cephalopod::addRTLList
    );

    public Cephalopod() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of the calculations is %d%n", solveCephalopodMath(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of all calculations reading right to left is %d%n", solveCephalopodMathRightToLeft(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long solveCephalopodMath(final List<String> data) {
        final List<List<Long>> lists = parseHorizontalTrimmedLists(data.subList(0, data.size() - 1));
        final String[] operators = data.getLast().split("\\s+");
        long sum = 0;

        for (int i = 0; i < lists.size(); i++) {
            sum += OPERATOR_MAP.get(operators[i]).apply(lists.get(i));
        }

        return sum;
    }

    public static long solveCephalopodMathRightToLeft(final List<String> data) {
        final List<List<String>> columns = parseStringColumns(data);
        final String[] operators = data.getLast().split("\\s+");
        long sum = 0;
        for (int i = 0; i < columns.size(); i++) {
            sum += RTL_OPERATOR_MAP.get(operators[i]).apply(columns.get(i));
        }

        return sum;
    }

    private static List<List<Long>> parseHorizontalTrimmedLists(final List<String> data) {
        final int columns = data.getFirst().split("\\s+").length;
        final int rows = data.size();
        final List<List<Long>> lists = new ArrayList<>(columns);
        IntStream.range(0, columns).forEach(it -> lists.add(new ArrayList<>(rows)));

        data.stream()
                .map(String::trim)
                .map(line -> line.split("\\s+"))
                .map(Cephalopod::parseLongArray)
                .forEach(longs -> addNumbersToLists(longs, lists));

        return lists;
    }

    private static List<List<String>> parseStringColumns(final List<String> data) {
        final int rightMostPosition = data.subList(0, data.size() - 1).stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();

        final List<Integer> columnPositions = getColumnPositions(data.getLast(), rightMostPosition);
        final int rows = data.size();

        final List<List<String>> columns = new ArrayList<>(rows);
        IntStream.range(0, columnPositions.size() - 1).forEach(it -> columns.add(new ArrayList<>(data.size() - 1)));

        data.subList(0, rows - 1).forEach(line -> addRowToColumns(line, columns, columnPositions));
        return columns;
    }

    private static void addRowToColumns(final String line, final List<List<String>> columns, final List<Integer> columnPositions) {
        final int maxLength = line.length();
        for (int i = 0; i < columnPositions.size() - 1; i++) {
            columns.get(i).add(line.substring(columnPositions.get(i), Math.min(columnPositions.get(i + 1), maxLength)));
        }
    }

    private static List<Integer> getColumnPositions(final String lastLine, final int endPosition) {
        return IntStream.concat(IntStream.of(endPosition), IntStream.range(0, lastLine.length())
                .filter(idx -> lastLine.charAt(idx) != ' '))
                .boxed()
                .sorted()
                .toList();
    }

    private static long[] parseLongArray(final String[] stringArray) {
        return Arrays.stream(stringArray)
                .mapToLong(Long::parseLong)
                .toArray();
    }

    private static void addNumbersToLists(final long[] numbers, final List<List<Long>> lists) {
        for (int i = 0; i < numbers.length; i++) {
            lists.get(i).add(numbers[i]);
        }
    }

    private static Long multiplyList(final List<Long> longs) {
        return longs.stream()
                .reduce(1L, (a, b) -> a * b);
    }

    private static Long addList(final List<Long> longs) {
        return longs.stream()
                .reduce(0L, Long::sum);
    }

    private static Long multiplyRTLList(final List<String> lines) {
        final List<StringBuilder> stringBuilders = getStringBuilders(lines);

        return stringBuilders.stream()
                .map(StringBuilder::toString)
                .filter(line -> !line.isBlank())
                .mapToLong(Long::parseLong)
                .reduce(1L, (a, b) -> a * b);
    }

    private static List<StringBuilder> getStringBuilders(final List<String> lines) {
        final int rightMostPosition = lines.stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow(() -> new IllegalStateException("No longest string found in: " + lines));

        final List<StringBuilder> stringBuilders = IntStream.range(0, rightMostPosition)
                .mapToObj(i -> new StringBuilder())
                .toList();

        lines.forEach(line -> addDigitsToStringBuilders(line, stringBuilders));
        return stringBuilders;
    }

    private static Long addRTLList(final List<String> lines) {
        final List<StringBuilder> stringBuilders = getStringBuilders(lines);

        return stringBuilders.stream()
                .map(StringBuilder::toString)
                .filter(line -> !line.isBlank())
                .mapToLong(Long::parseLong)
                .reduce(0L, Long::sum);
    }

    private static void addDigitsToStringBuilders(final String line, final List<StringBuilder> stringBuilders) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                continue;
            }

            stringBuilders.get(i).append(line.charAt(i));
        }
    }

    static void main() {
        new Cephalopod();
    }
}
