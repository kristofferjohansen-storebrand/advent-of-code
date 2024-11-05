package no.krazyglitch.aoc2015.day2;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class WrappingPaper {

    public WrappingPaper() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The elves need to order %d square feet of wrapping paper\n", wrapPresents(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The elves need to order %d feet of ribbon\n", tiePresents(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int wrapPresents(final List<String> data) {
        return data.stream()
                .mapToInt(WrappingPaper::wrapPresent)
                .sum();
    }

    private static int wrapPresent(final String present) {
        final Integer[] dimensions = getDimensions(present);

        return 2*dimensions[0]*dimensions[1] + 2*dimensions[1]*dimensions[2] + 2*dimensions[0]*dimensions[2] + dimensions[0]*dimensions[1];
    }

    private static Integer[] getDimensions(final String present) {
        return Arrays.stream(present.split("x"))
                .map(Integer::parseInt)
                .sorted()
                .toList()
                .toArray(new Integer[0]);
    }

    public static int tiePresents(final List<String> data) {
        return data.stream()
                .mapToInt(WrappingPaper::tiePresent)
                .sum();
    }

    private static int tiePresent(final String present) {
        final Integer[] dimensions = getDimensions(present);

        return 2*dimensions[0] + 2*dimensions[1] + dimensions[0]*dimensions[1]*dimensions[2];
    }

    public static void main(final String[] args) {
        new WrappingPaper();
    }
}
