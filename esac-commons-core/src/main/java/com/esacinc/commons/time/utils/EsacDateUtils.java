package com.esacinc.commons.time.utils;

import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;

public final class EsacDateUtils {
    public final static long HOURS_IN_DAY = 24L;

    public final static long MIN_IN_HOUR = 60L;
    public final static long MIN_IN_DAY = MIN_IN_HOUR * HOURS_IN_DAY;

    public final static long SEC_IN_MIN = 60L;
    public final static long SEC_IN_HOUR = SEC_IN_MIN * MIN_IN_HOUR;
    public final static long SEC_IN_DAY = SEC_IN_MIN * MIN_IN_DAY;
    public final static long SEC_IN_YEAR = 31556952L;

    public final static long MS_IN_SEC = 1000L;
    public final static long MS_IN_MIN = MS_IN_SEC * SEC_IN_MIN;
    public final static long MS_IN_HOUR = MS_IN_SEC * SEC_IN_HOUR;
    public final static long MS_IN_DAY = MS_IN_SEC * SEC_IN_DAY;
    public final static long MS_IN_YEAR = MS_IN_SEC * SEC_IN_YEAR;

    public final static long US_IN_MS = 1000L;
    public final static long US_IN_SEC = US_IN_MS * MS_IN_SEC;
    public final static long US_IN_MIN = US_IN_MS * MS_IN_MIN;
    public final static long US_IN_HOUR = US_IN_MS * MS_IN_HOUR;
    public final static long US_IN_DAY = US_IN_MS * MS_IN_DAY;
    public final static long US_IN_YEAR = US_IN_MS * MS_IN_YEAR;

    public final static long NS_IN_US = 1000L;
    public final static long NS_IN_MS = NS_IN_US * US_IN_MS;
    public final static long NS_IN_SEC = NS_IN_US * US_IN_SEC;
    public final static long NS_IN_MIN = NS_IN_US * US_IN_MIN;
    public final static long NS_IN_HOUR = NS_IN_US * US_IN_HOUR;
    public final static long NS_IN_DAY = NS_IN_US * US_IN_DAY;
    public final static long NS_IN_YEAR = NS_IN_US * US_IN_YEAR;

    public final static String UTC_TZ_ID = "UTC";
    public final static TimeZone UTC_TZ = TimeZone.getTimeZone(UTC_TZ_ID);

    public final static FastDateFormat UTC_TZ_DISPLAY_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss Z", UTC_TZ, Locale.ROOT);

    private EsacDateUtils() {
    }
}
