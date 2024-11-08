package no.krazyglitch.aoc2015.day11;

import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class CorporatePolicy {
    private static final Pattern CONTAINS_PAIRS_PATTERN = Pattern.compile("((\\w)\\2).*((\\w)\\4)");
    private static final Pattern CONTAINS_STRAIGHTS_PATTERN = Pattern.compile("(?=(abc|bcd|cde|def|efg|fgh|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz))");
    private static final Pattern CONTAINS_NO_CONFUSION_PATTERN = Pattern.compile("^([^ilo])*$");

    private static final Predicate<String> PASSWORD_VALID = password ->
            CONTAINS_PAIRS_PATTERN.asPredicate()
                    .and(CONTAINS_STRAIGHTS_PATTERN.asPredicate())
                    .and(CONTAINS_NO_CONFUSION_PATTERN.asPredicate())
                    .test(password);

    public CorporatePolicy() {
        LocalDateTime start = LocalDateTime.now();

        final String nextPassword = getNextValidPassword("vzbxkghb");
        System.out.printf("The next valid password is %s\n", nextPassword);
        System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

        start = LocalDateTime.now();
        System.out.printf("The next next valid password is %s\n", getNextValidPassword(nextPassword));
        System.out.printf("Part two took %d ms", getMillisSince(start));
    }

    public static String getNextValidPassword(final String oldPassword) {
        String password = oldPassword;
        do {
            password = getNextPasswordIteration(password);
        } while (!checkPassword(password));

        return password;
    }

    private static String getNextPasswordIteration(final String currentIteration) {
        final char[] letters = currentIteration.toCharArray();
        boolean overflow;
        int index = letters.length-1;

        do {
            final char letter = letters[index];
            overflow = false;
            if (letter == 'z') {
                letters[index] = 'a';
                overflow = true;
            } else {
                letters[index] += 1;
            }

            index--;
        } while (overflow && index >= 0);

        return new String(letters);
    }

    private static boolean checkPassword(final String password) {
        return PASSWORD_VALID.test(password);
    }

    public static void main(final String[] args) {
        new CorporatePolicy();
    }
}
