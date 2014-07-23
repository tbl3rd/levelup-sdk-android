/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
/*
 * The implementation of scrubClass(AndroidTestCase) is based on AOSP source code:
 *
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.util.LogManager;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
     * Preserves static fields and avoids scrubby Android bugs. This implementation originates from
     * AOSP source code.
     *
     * @param testCase the test case to scrub
     * @throws IllegalAccessException from {@link Field#get}
     * @see <a href="http://goo.gl/YVFAM0">Update scrubClass() to match CoreTestRunner.</a>
     */
    @SuppressWarnings("unused")
    public static void scrubClass(@NonNull final TestCase testCase) throws
            IllegalAccessException {
        final Field[] fields = testCase.getClass().getDeclaredFields();

        for (final Field field : fields) {
            if (!field.getType().isPrimitive() && !Modifier.isStatic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    field.set(testCase, null);
                } catch (final Exception e) {
                    LogManager.d("Could not nullify field!");
                }

                if (null != field.get(testCase)) {
                    LogManager.d("Could not nullify field!");
                }
            }
        }
    }

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
                AndroidTestCase.fail("Application context is null.");
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
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
