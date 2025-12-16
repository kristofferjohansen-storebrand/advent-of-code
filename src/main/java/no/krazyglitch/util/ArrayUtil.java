package no.krazyglitch.util;

import java.util.Arrays;

public class ArrayUtil {

    public static int[] toIntArray(final String[] input) {
        return Arrays.stream(input)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public static long[] toLongArray(final String[] input) {
        return Arrays.stream(input)
                .mapToLong(Long::parseLong)
                .toArray();
    }
}
