package no.krazyglitch.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static long getMillisSince(final LocalDateTime start) {
        return ChronoUnit.MILLIS.between(start, LocalDateTime.now());
    }
}
