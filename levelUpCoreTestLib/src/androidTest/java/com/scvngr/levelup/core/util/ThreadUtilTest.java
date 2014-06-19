/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.ThreadUtil}.
 */
public final class ThreadUtilTest extends TestCase {

    @SmallTest
    public static void testGetHandlerThread() {
        HandlerThread thread = null;
        try {
            thread = ThreadUtil.getHandlerThread("test-thread", //$NON-NLS-1$
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);

            assertNotNull(thread);
            assertEquals("test-thread", thread.getName()); //$NON-NLS-1$
            assertTrue(thread.isAlive());
            assertNotNull(thread.getLooper());
            assertNotSame(Looper.myLooper(), thread.getLooper());
            assertNotSame(Looper.getMainLooper(), thread.getLooper());

            SystemClock.sleep(100);

            assertEquals(android.os.Process.THREAD_PRIORITY_BACKGROUND,
                    android.os.Process.getThreadPriority(thread.getThreadId()));
        } finally {
            if (null != thread) {
                thread.getLooper().quit();
            }
        }
    }

    @SmallTest
    public static void testGetHandlerThread_new_instance() {
        HandlerThread thread1 = null;
        HandlerThread thread2 = null;
        try {
            thread1 = ThreadUtil.getHandlerThread("test-thread-1", //$NON-NLS-1$
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thread2 = ThreadUtil.getHandlerThread("test-thread-2", //$NON-NLS-1$
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);

            assertNotSame(thread1, thread2);
        } finally {
            if (null != thread1) {
                thread1.getLooper().quit();
            }

            if (null != thread2) {
                thread2.getLooper().quit();
            }
        }
    }
}
