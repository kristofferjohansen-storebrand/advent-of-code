package no.krazyglitch.aoc2015.day10;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class LookSay {

    private static final Pattern LOOK_SAY_PATTERN = Pattern.compile("((\\d)(\\2*))");

    public LookSay() {
        LocalDateTime start = LocalDateTime.now();
        System.out.printf("The length of the the result is %d\n", getLookSayLength("3113322113", 40));
        System.out.printf("Part one took %s ms\n\n", getMillisSince(start));

        start = LocalDateTime.now();
        System.out.printf("The length of the the result is %d\n", getLookSayLength("3113322113", 50));
        System.out.printf("Part two took %s ms", getMillisSince(start));
    }

    public static int getLookSayLength(final String input, final int runs) {
        return getLookSay(input, runs).length();
    }

    private static String getLookSay(final String input, final int runs) {
        return getLookSay(input, runs, 0);
    }

    private static String getLookSay(final String input, final int runs, final int index) {
        if (runs == index) {
            return input;
        }

        final Matcher matcher = LOOK_SAY_PATTERN.matcher(input);
        final List<String> lookSayGroups = new ArrayList<>();
        while (matcher.find()) {
            lookSayGroups.add(matcher.group(1));
        }

        final String lookSayString = lookSayGroups.stream()
                .map(string -> "" + string.length() + string.charAt(0))
                .collect(Collectors.joining());

        return getLookSay(lookSayString, runs, index+1);
    }

    public static void main(final String[] args) {
        new LookSay();
    }
}
