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
            thread = ThreadUtil.getHandlerThread("test-thread",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);

            assertNotNull(thread);
            assertEquals("test-thread", thread.getName());
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
            thread1 = ThreadUtil.getHandlerThread("test-thread-1",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thread2 = ThreadUtil.getHandlerThread("test-thread-2",
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
