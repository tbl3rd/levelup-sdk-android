package com.scvngr.levelup.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Helper utility class to handle date operations.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.INTERNAL)
public final class IsoDateUtils {

    private static final String ISO_DATETIME_TIME_ZONE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZZ"; //$NON-NLS-1$

    /**
     * Gets a new instance of {@link TimeZone} representing UTC.
     *
     * @return A new instance of {@link TimeZone} representing UTC.
     */
    @NonNull
    public static TimeZone getTimeZoneUtc() {
        return TimeZone.getTimeZone("UTC"); //$NON-NLS-1$
    }

    /**
     * Parses an ISO 8601 timezone-formatted string into a {@link Date} relative to a
     * {@link TimeZone}.
     *
     * @param datetime An ISO 8601 formatted datetime with timezone.
     * @param timeZone The relative {@link TimeZone}.
     * @return a parsed {@link Date} relative to the given {@link TimeZone}.
     * @throws ParseException if there was a problem parsing the date.
     */
    @NonNull
    public static Date parseIsoDatetime(@NonNull final String datetime,
            @NonNull final TimeZone timeZone) throws ParseException {
        final DateFormat formatter =
                new SimpleDateFormat(ISO_DATETIME_TIME_ZONE_FORMAT_STRING, Locale.US);
        formatter.setTimeZone(timeZone);
        return formatter.parse(datetime);
    }

    /**
     * Converts a {@link Date} to a ISO 8601 timezone-formatted string relative to a
     * {@link TimeZone}.
     *
     * @param date The date to convert.
     * @param timeZone The relative {@link TimeZone}.
     * @return An ISO 8601 formatted datetime relative to the given {@link TimeZone}.
     */
    @NonNull
    public static String toIsoDatetime(@NonNull final Date date, @NonNull final TimeZone timeZone) {
        final DateFormat formatter =
                new SimpleDateFormat(ISO_DATETIME_TIME_ZONE_FORMAT_STRING, Locale.US);
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private IsoDateUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
