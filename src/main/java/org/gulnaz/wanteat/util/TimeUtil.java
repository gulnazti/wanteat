package org.gulnaz.wanteat.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author gulnaz
 */
public class TimeUtil {

    public static final LocalTime RESTRICTION_TIME = LocalTime.of(11, 0);

    private static Clock clock = Clock.systemDefaultZone();

    private TimeUtil() {
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now(clock);
    }

    public static void fixClock(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        clock = Clock.fixed(dateTime.atZone(zoneId).toInstant(), zoneId);
    }
}
