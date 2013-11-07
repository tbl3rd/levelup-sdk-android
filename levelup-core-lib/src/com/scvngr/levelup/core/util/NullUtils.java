package com.scvngr.levelup.core.util;

import java.util.Locale;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * Utilities to help use {@link NonNull} and {@link Nullable} annotations.
 */
public final class NullUtils {

    /**
     * Retrieves a non-null result from source or throws an exception if the source is null. This
     * should be used when the wrapped source claims to return non-null, but isn't annotated.
     *
     * @param <T> type of object to return.
     * @param source source object or null.
     * @param what a human-readable identifier for the item.
     * @return the source object.
     * @throws AssertionError if the source is null
     */
    @NonNull
    public static <T> T nonNullContract(@Nullable final T source, @NonNull final String what) {
        if (null == source) {
            throw new AssertionError(String.format(Locale.US, "%s cannot be null", what)); //$NON-NLS-1$
        }

        return source;
    }

    /**
     * Retrieves a non-null result from source or throws an exception if the source is null. This
     * should be used when the wrapped source claims to return non-null, but isn't annotated.
     *
     * @param <T> type of object to return.
     * @param source source object or null.
     * @return the source object.
     * @throws AssertionError if the source is null
     */
    @NonNull
    public static <T> T nonNullContract(@Nullable final T source) {
        if (null == source) {
            throw new AssertionError("the source cannot be null"); //$NON-NLS-1$
        }

        return source;
    }

    /**
     * A wrapper around {@link String#format(Locale, String, Object...)} which returns a non-null
     * result and uses {@link Locale#US}.
     *
     * @param format format string
     * @param args optional arguments
     * @return a non-null formatted string
     */
    @NonNull
    public static String format(@NonNull final String format, @Nullable final Object... args) {
        return nonNullContract(String.format(Locale.US, format, args), "formatted string"); //$NON-NLS-1$
    }

    /**
     * A wrapper around {@link String#format(Locale, String, Object...)} which returns a non-null
     * result and uses {@link Locale#US}.
     *
     * @param locale format locale
     * @param format format string
     * @param args optional arguments
     * @return a non-null formatted string
     */
    @NonNull
    public static String format(@NonNull final Locale locale, @NonNull final String format,
            @Nullable final Object... args) {
        return nonNullContract(String.format(locale, format, args), "formatted string"); //$NON-NLS-1$
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private NullUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
