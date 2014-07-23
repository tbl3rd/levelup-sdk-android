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
        LogManager.v("Test message");
        LogManager.d("Test message");
        LogManager.i("Test message");
        LogManager.w("Test message");
        LogManager.e("Test message");
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with ints.
     */
    @SmallTest
    public void testLog_with_formatters_int() {
        LogManager.v("Test %d", 1);
        LogManager.d("Test %d", 1);
        LogManager.i("Test %d", 1);
        LogManager.w("Test %d", 1);
        LogManager.e("Test %d", 1);
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with Strings.
     */
    @SmallTest
    public void testLog_with_formatters_string() {
        LogManager.v("Test %s", "stringy");
        LogManager.d("Test %s", "stringy");
        LogManager.i("Test %s", "stringy");
        LogManager.w("Test %s", "stringy");
        LogManager.e("Test %s", "stringy");
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with objects.
     */
    @SmallTest
    public void testLog_with_formatters_object() {
        LogManager.v("Test %s", new Object());
        LogManager.d("Test %s", new Object());
        LogManager.i("Test %s", new Object());
        LogManager.w("Test %s", new Object());
        LogManager.e("Test %s", new Object());
    }

    /**
     * Tests that no exceptions are thrown when log messages are formatted with null.
     */
    @SmallTest
    public void testLog_with_formatters_null() {
        LogManager.v("Test %s", (Object[]) null);
        LogManager.d("Test %s", (Object[]) null);
        LogManager.i("Test %s", (Object[]) null);
        LogManager.w("Test %s", (Object[]) null);
        LogManager.e("Test %s", (Object[]) null);
    }

    /**
     * Tests that no exceptions are thrown when messages are logged with exceptions.
     */
    @SmallTest
    public void testLog_with_exceptions() {
        final Exception e = new RuntimeException("foo");

        LogManager.v("Test", e);
        LogManager.d("Test", e);
        LogManager.i("Test", e);
        LogManager.w("Test", e);
        LogManager.e("Test", e);
    }
}
