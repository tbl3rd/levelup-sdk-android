/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.util;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.util.IsoDateUtils;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * Helper class to work with {@link JSONObject} and avoid its bugs.
 * <http://code.google.com/p/android/issues/detail?id=13830>.
 */
@ThreadSafe
public final class JsonUtils {

    /**
     * Helper method that doesn't return some fake version of null if the string is null, it returns
     * null.
     *
     * @param json the {@link JSONObject}.
     * @param key the key in {@code json}.
     * @return {@link String} mapping for {@code key} in {@code json} or {@code null} if no mapping
     *         exists.
     */
    @Nullable
    public static String optString(@NonNull final JSONObject json, @NonNull final String key) {
        String value = null;

        if (!json.isNull(key)) {
            value = json.optString(key, null);
        }

        return value;
    }

    /**
     * Loads a {@link Date}.
     *
     * @param json the object to read from.
     * @param key the key in {@code json}.
     * @return A parsed {@link Date} or {@code null} if either the key is not present or is set to
     *         {@code null}.
     * @throws JSONException if the date could not be parsed.
     */
    @Nullable
    /* package */static Date optDate(@NonNull final JSONObject json, @NonNull final String key)
            throws JSONException {
        Date value = null;

        if (!json.isNull(key)) {
            try {
                value = IsoDateUtils.parseIsoDatetime(json.getString(key), TimeZone.getDefault());
            } catch (final ParseException e) {
                final JSONException jsonException = new JSONException("could not parse datetime"); //$NON-NLS-1$
                jsonException.initCause(e);
                throw jsonException;
            }
        }

        return value;
    }

    /**
     * Loads a {@link Set} of {@link Integer} from a {@link JSONArray} of integers.
     *
     * @param json the object to read from.
     * @param key the key in {@code json}.
     * @return a set of integers loaded from the array of integers at the given {@code key} or
     *         {@code null} if either the key is not present or is set to {@code null}.
     * @throws JSONException if the array contents aren't integers.
     */
    @Nullable
    public static Set<Integer> optIntegerSet(@NonNull final JSONObject json,
            @NonNull final String key) throws JSONException {
        Set<Integer> result = null;

        if (!json.isNull(key)) {
            final JSONArray array = json.optJSONArray(key);

            if (null != array) {
                final int count = array.length();
                result = new HashSet<Integer>(count);

                for (int i = 0; i < count; i++) {
                    result.add(array.getInt(i));
                }
            }
        }

        return result;
    }

    /**
     * Loads a {@link Set} of {@link String}s from a {@link JSONArray} of strings.
     *
     * @param json the object to read from.
     * @param key the key in {@code json}.
     * @return a set of strings loaded from the array of strings at the given {@code key} or
     *         {@code null} if either the key is not present or is set to {@code null}.
     * @throws JSONException if the array contents aren't strings.
     */
    @Nullable
    public static Set<String>
            optStringSet(@NonNull final JSONObject json, @NonNull final String key)
                    throws JSONException {
        Set<String> result = null;

        if (!json.isNull(key)) {
            final JSONArray array = json.optJSONArray(key);

            if (null != array) {
                final int count = array.length();
                result = new HashSet<String>(count);

                for (int i = 0; i < count; i++) {
                    result.add(array.getString(i));
                }
            }
        }

        return result;
    }

    /**
     * Gets an optional {@link MonetaryValue} object from the JSON at the key passed.
     *
     * @param json the {@link JSONObject} to get the {@link MonetaryValue} from.
     * @param key the key in the {@link JSONObject} to get the {@link MonetaryValue} from.
     * @return a {@link MonetaryValue} from the parsed long or null.
     * @throws JSONException if the parsing fails.
     */
    @Nullable
    public static MonetaryValue optMonetaryValue(@NonNull final JSONObject json,
            @NonNull final String key) throws JSONException {
        final MonetaryValue value;
        final Long amount = json.optLong(key, Long.MIN_VALUE);

        if (Long.MIN_VALUE != amount) {
            value = new MonetaryValue(amount);
        } else {
            value = null;
        }

        return value;
    }

    /**
     * Gets a required {@link MonetaryValue} object from the JSON at the key passed or throws a
     * {@link JSONException} if it's missing.
     *
     * @param json the {@link JSONObject} to get the {@link MonetaryValue} from.
     * @param key the key in the {@link JSONObject} to get the {@link MonetaryValue} from.
     * @return a {@link MonetaryValue} from the parsed long.
     * @throws JSONException if the parsing fails.
     */
    @NonNull
    public static MonetaryValue getMonetaryValue(@NonNull final JSONObject json,
            @NonNull final String key) throws JSONException {
        final MonetaryValue value = optMonetaryValue(json, key);

        if (null == value) {
            throw new JSONException(String.format(Locale.US, "key %s is required.", key)); //$NON-NLS-1$
        }

        return value;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private JsonUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
