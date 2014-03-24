/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Utilities for the support test case classes.
 *
 * @see SupportAndroidTestCase
 * @see SupportApplicationTestCase
 */
/* package */final class SupportTestCaseUtils {

    private static final long WAIT_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(4L);

    private static final long WAIT_SLEEP_MILLIS = 20L;

    /**
     * Workaround for a race condition affecting {@link android.test.AndroidTestCase} classes which
     * causes {@link android.content.Context#getApplicationContext} to return null for a short
     * period of time.
     *
     * @param context the target Context.
     * @see <a href="http://goo.gl/V5xbNd">Stack Overflow</a>
     */
    public static void waitForApplicationContext(@NonNull final Context context) {
        final long endTime = SystemClock.elapsedRealtime() + WAIT_TIMEOUT_MILLIS;

        while (null == context.getApplicationContext()) {

            if (SystemClock.elapsedRealtime() >= endTime) {
                AndroidTestCase.fail("Application context is null."); //$NON-NLS-1$
            }

            SystemClock.sleep(WAIT_SLEEP_MILLIS);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private SupportTestCaseUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
