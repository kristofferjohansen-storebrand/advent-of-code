package no.krazyglitch.aoc2015.day8;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.List;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class LiteralStrings {


    public LiteralStrings() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The length difference when unescaping strings is %d\n", getUnescapedDifference(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The length difference when escaping strings is %d\n", getEscapedDifference(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static int getUnescapedDifference(final List<String> data) {
        return data.stream().mapToInt(LiteralStrings::getUnescapedDifference).sum();
    }

    private static int getUnescapedDifference(final String escapedString) {
        return escapedString.length()-unescapeString(escapedString).length();
    }

    public static int getEscapedDifference(final List<String> data) {
        return data.stream().mapToInt(LiteralStrings::getEscapedDifference).sum();
    }

    private static int getEscapedDifference(final String unescapedString) {
        return escapeString(unescapedString).length() - unescapedString.length();
    }

    private static String unescapeString(final String string) {
        final StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int i = 1; i < string.length()-1; i++) {
            final char nextCharacter = string.charAt(i);
            if (escaping) {
                if (nextCharacter == 'x') {
                    result.append((char) Integer.parseInt(string.substring(i + 1, i + 3), 16));
                    i += 2;
                } else {
                    result.append(nextCharacter);
                }

                escaping = false;
            } else {
                if (nextCharacter == '\\') {
                    escaping = true;
                } else {
                    result.append(nextCharacter);
                }
            }
        }

        return result.toString();
    }

    private static String escapeString(final String string) {
        final String stringWithEscapedBackSlash = string.replaceAll("\\\\", "\\\\\\\\");
        final String stringWithEscapedQuotes = stringWithEscapedBackSlash.replaceAll("\"", "\\\\\"");
        return "\"" + stringWithEscapedQuotes + "\"";
    }

    public static void main(final String[] args) {
        new LiteralStrings();
    }
}
