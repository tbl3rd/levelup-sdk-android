/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.util;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper utility class to handle date operations.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.INTERNAL)
public final class IsoDateUtils {

    private static final String ISO_DATETIME_TIME_ZONE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZZ";

    /**
     * Gets a new instance of {@link TimeZone} representing UTC.
     *
     * @return A new instance of {@link TimeZone} representing UTC.
     */
    @NonNull
    public static TimeZone getTimeZoneUtc() {
        return NullUtils.nonNullContract(TimeZone.getTimeZone("UTC"));
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
        setTimeZone(formatter, timeZone);

        return NullUtils.nonNullContract(formatter.parse(datetime));
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
        setTimeZone(formatter, timeZone);

        return NullUtils.nonNullContract(formatter.format(date));
    }

    /**
     * Sets the time zone of a {@link DateFormat}. This method implements a workaround for
     * https://code.google.com/p/android/issues/detail?id=8258 which only affects Android 2.2
     * devices.
     *
     * @param timeZone The relative {@link TimeZone}.
     */
    private static void setTimeZone(@NonNull final DateFormat formatter,
            @NonNull final TimeZone timeZone) {
        if (EnvironmentUtil.isSdk9OrGreater()) {
            formatter.setTimeZone(timeZone);
        } else {
            formatter.setCalendar(Calendar.getInstance(timeZone, Locale.US));
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private IsoDateUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
