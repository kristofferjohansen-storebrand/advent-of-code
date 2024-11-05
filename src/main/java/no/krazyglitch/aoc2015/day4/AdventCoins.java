package no.krazyglitch.aoc2015.day4;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class AdventCoins {

    private static final MessageDigest MESSAGE_DIGEST;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException exc) {
            System.out.println("Whoops");
            throw new RuntimeException(exc);
        }
    }

    public AdventCoins() {
        final String data = "iwrupvqb";
        LocalDateTime start = LocalDateTime.now();
        System.out.printf("The lowest salt resulting in 5 leading zeros is %d\n", findLowestSalt(data, 5));
        System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

        start = LocalDateTime.now();
        System.out.printf("The lowest salt resulting in 6 leading zeros is %d\n", findLowestSalt(data, 6));
        System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
    }

    public static int findLowestSalt(final String secret, final int leadingZeros) {
        final String leadingPart = StringUtils.leftPad("", leadingZeros, "0");
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            final String hash = getMD5Hash(secret, String.valueOf(i));

            if (hash.startsWith(leadingPart)) {
                return i;
            }
        }

        return 0;
    }

    private static String getMD5Hash(final String secret, final String salt) {
        MESSAGE_DIGEST.update((secret+salt).getBytes(StandardCharsets.UTF_8));
        return bytesToHex(MESSAGE_DIGEST.digest());
    }

    private static String bytesToHex(final byte[] bytes) {
        final BigInteger bigInteger = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bigInteger);
    }

    public static void main(final String[] args) {
        new AdventCoins();
    }
}
