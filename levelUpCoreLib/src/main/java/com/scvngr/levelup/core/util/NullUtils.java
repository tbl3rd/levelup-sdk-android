/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import java.util.Locale;

/**
 * Utilities to help use {@link NonNull} and {@link Nullable} annotations.
 */
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
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
        // A separate conditional, so the compiler can strip it.
        if (CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED) {
            if (null == source) {
                throw new AssertionError(String.format(Locale.US, "%s cannot be null", what)); //$NON-NLS-1$
            }
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
        // A separate conditional, so the compiler can strip it.
        if (CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED) {
            if (null == source) {
                throw new AssertionError("the source cannot be null"); //$NON-NLS-1$
            }
        }

        return source;
    }

    /**
     * @param clazz the class whose name is being retrieved
     * @return the value of class.getName()
     */
    @NonNull
    public static String getClassName(final Class<?> clazz) {
        return NullUtils.nonNullContract(clazz.getName());
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
