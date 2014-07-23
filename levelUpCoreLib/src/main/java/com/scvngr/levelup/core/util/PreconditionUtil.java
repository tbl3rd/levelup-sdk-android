/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import net.jcip.annotations.ThreadSafe;

import java.util.Locale;

/**
 * This is a utility class to enforce preconditions.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class PreconditionUtil {

    /**
     *
     * @param objectToCheck Reference to check for nullness.
     * @param objectName Human-readable name of the object reference, to be formatted into any
     *            errors generated by this method.
     * @throws AssertionError if {@code objectToCheck} is null and
     *             {@link CoreLibConstants#IS_PARAMETER_CHECKING_ENABLED}.
     */
    public static void assertNotNull(@Nullable final Object objectToCheck,
            @NonNull final String objectName) {
        if (CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED) {
            if (null == objectToCheck) {
                final String message = String.format(Locale.US, "%s cannot be null", objectName);
                throw new AssertionError(message);
            }
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PreconditionUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
