package com.scvngr.levelup.core.util;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Tests {@link IsoDateUtils}.
 */
public final class IsoDateUtilsTest extends AndroidTestCase {

    @NonNull
    private static final TimeZone TIME_ZONE_EST = NullUtils.nonNullContract(TimeZone
            .getTimeZone("GMT-0500")); //$NON-NLS-1$

    @SmallTest
    public void testGetTimeZoneUtc() {
        final TimeZone timeZone = IsoDateUtils.getTimeZoneUtc();
        assertEquals(0, timeZone.getRawOffset());
        assertEquals("UTC", timeZone.getID()); //$NON-NLS-1$
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone); //$NON-NLS-1$
    }

    @SmallTest
    public void testParseIsoDateTime() {
        validateParseIsoDateTime(IsoDateUtils.getTimeZoneUtc());
        validateParseIsoDateTime(TIME_ZONE_EST);
    }

    @SmallTest
    public void testParseIsoDateTime_parseException() {
        validateParseIsoDateTimeParseException(IsoDateUtils.getTimeZoneUtc());
        validateParseIsoDateTimeParseException(TIME_ZONE_EST);
    }

    /**
     * Validate the {@link ParseException} thrown by
     * {@link IsoDateUtils#parseIsoDatetime(String, TimeZone)} when given an invalid datetime.
     *
     * @param timeZone The relative {@link TimeZone}.
     */
    private static void validateParseIsoDateTimeParseException(@NonNull final TimeZone timeZone) {
        try {
            IsoDateUtils.parseIsoDatetime("2011-05-03T12:00:00", timeZone); //$NON-NLS-1$
            fail("ParseException should have been thrown."); //$NON-NLS-1$
        } catch (final ParseException e) {
            // Expected exception
        }

        try {
            IsoDateUtils.parseIsoDatetime("2011-05-0312:00:00-05:00", timeZone); //$NON-NLS-1$
            fail("ParseException should have been thrown."); //$NON-NLS-1$
        } catch (final ParseException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testToIsoDateTime() {
        final Date date = new Date(1370993354000L);
        assertEquals("2013-06-11T23:29:14+0000", //$NON-NLS-1$
                IsoDateUtils.toIsoDatetime(date, IsoDateUtils.getTimeZoneUtc()));
        assertEquals("2013-06-11T18:29:14-0500", IsoDateUtils.toIsoDatetime(date, TIME_ZONE_EST)); //$NON-NLS-1$
    }

    /**
     * Validate the {@link Date}s generated by
     * {@link IsoDateUtils#parseIsoDatetime(String, TimeZone)} when given valid datetimes.
     *
     * @param timeZone The relative {@link TimeZone}.
     */
    private static void validateParseIsoDateTime(@NonNull final TimeZone timeZone) {
        try {
            {
                final Date date =
                        IsoDateUtils.parseIsoDatetime("2012-12-04T18:10:45-05:00", timeZone); //$NON-NLS-1$

                final Calendar cal = Calendar.getInstance(timeZone, Locale.US);
                cal.setTime(date);
                assertEquals(2012, cal.get(Calendar.YEAR));
                assertEquals(4, cal.get(Calendar.DAY_OF_MONTH));
                assertEquals(11, cal.get(Calendar.MONTH));
                assertEquals(23 + getTimeZoneOffsetHours(timeZone), cal.get(Calendar.HOUR_OF_DAY));
                assertEquals(10, cal.get(Calendar.MINUTE));
                assertEquals(45, cal.get(Calendar.SECOND));
                assertEquals(timeZone.getRawOffset(),
                        cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
            }

            {
                final Date date =
                        IsoDateUtils.parseIsoDatetime("2011-05-03T12:00:00-0400", timeZone); //$NON-NLS-1$

                final Calendar cal = Calendar.getInstance(timeZone, Locale.US);
                cal.setTime(date);
                assertEquals(2011, cal.get(Calendar.YEAR));
                assertEquals(3, cal.get(Calendar.DAY_OF_MONTH));
                assertEquals(4, cal.get(Calendar.MONTH));
                assertEquals(16 + getTimeZoneOffsetHours(timeZone), cal.get(Calendar.HOUR_OF_DAY));
                assertEquals(0, cal.get(Calendar.MINUTE));
                assertEquals(0, cal.get(Calendar.SECOND));
                assertEquals(timeZone.getRawOffset(),
                        cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
            }
        } catch (final ParseException e) {
            fail("ParseException should have been thrown."); //$NON-NLS-1$
        }
    }

    /**
     * Get a {@link TimeZone}'s offset hours.
     *
     * @param timeZone The {@link TimeZone}.
     * @return The number of hours offset from GMT.
     */
    private static int getTimeZoneOffsetHours(@NonNull final TimeZone timeZone) {
        return timeZone.getRawOffset() / 3600000;
    }
}
