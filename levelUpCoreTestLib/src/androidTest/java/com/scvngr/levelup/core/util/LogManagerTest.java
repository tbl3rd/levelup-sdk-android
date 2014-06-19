/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.LogManager}.
 */
public final class LogManagerTest extends SupportAndroidTestCase {

    /*
     * NOTE: This class does not check for correct output to logcat. It is designed to simply verify
     * that the LogManager doesn't throw any exceptions under normal circumstances.
     */

    /**
     * Tests that no exceptions are thrown when log messages contain no formatters.
     */
    @SmallTest
    public void testLog_without_formatters() {
        LogManager.v("Test message"); //$NON-NLS-1$
        LogManager.d("Test message"); //$NON-NLS-1$
        LogManager.i("Test message"); //$NON-NLS-1$
        LogManager.w("Test message"); //$NON-NLS-1$
        LogManager.e("Test message"); //$NON-NLS-1$
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with ints.
     */
    @SmallTest
    public void testLog_with_formatters_int() {
        LogManager.v("Test %d", 1); //$NON-NLS-1$
        LogManager.d("Test %d", 1); //$NON-NLS-1$
        LogManager.i("Test %d", 1); //$NON-NLS-1$
        LogManager.w("Test %d", 1); //$NON-NLS-1$
        LogManager.e("Test %d", 1); //$NON-NLS-1$
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with Strings.
     */
    @SmallTest
    public void testLog_with_formatters_string() {
        LogManager.v("Test %s", "stringy"); //$NON-NLS-1$ //$NON-NLS-2$
        LogManager.d("Test %s", "stringy"); //$NON-NLS-1$ //$NON-NLS-2$
        LogManager.i("Test %s", "stringy"); //$NON-NLS-1$ //$NON-NLS-2$
        LogManager.w("Test %s", "stringy"); //$NON-NLS-1$ //$NON-NLS-2$
        LogManager.e("Test %s", "stringy"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with objects.
     */
    @SmallTest
    public void testLog_with_formatters_object() {
        LogManager.v("Test %s", new Object()); //$NON-NLS-1$
        LogManager.d("Test %s", new Object()); //$NON-NLS-1$
        LogManager.i("Test %s", new Object()); //$NON-NLS-1$
        LogManager.w("Test %s", new Object()); //$NON-NLS-1$
        LogManager.e("Test %s", new Object()); //$NON-NLS-1$
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with null.
     */
    @SmallTest
    public void testLog_with_formatters_null() {
        LogManager.v("Test %s", (Object[]) null); //$NON-NLS-1$
        LogManager.d("Test %s", (Object[]) null); //$NON-NLS-1$
        LogManager.i("Test %s", (Object[]) null); //$NON-NLS-1$
        LogManager.w("Test %s", (Object[]) null); //$NON-NLS-1$
        LogManager.e("Test %s", (Object[]) null); //$NON-NLS-1$
    }

    /**
     * Tests that no exceptions are thrown when messages are logged with exceptions.
     */
    @SmallTest
    public void testLog_with_exceptions() {
        final Exception e = new RuntimeException("foo"); //$NON-NLS-1$

        LogManager.v("Test", e); //$NON-NLS-1$
        LogManager.d("Test", e); //$NON-NLS-1$
        LogManager.i("Test", e); //$NON-NLS-1$
        LogManager.w("Test", e); //$NON-NLS-1$
        LogManager.e("Test", e); //$NON-NLS-1$
    }
}
