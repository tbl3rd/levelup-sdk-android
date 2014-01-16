/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link StrictModeUtil}.
 */
public final class StrictModeUtilTest extends SupportAndroidTestCase {

    @SmallTest
    public void testSetStrictMode() {
        /*
         * This primarily verifies that calling the methods doesn't cause a crash. It isn't possible
         * to check see if StrictMode is actually enabled within the test framework.
         */

        StrictModeUtil.setStrictMode(true);
        StrictModeUtil.setStrictMode(false);
    }

    @SmallTest
    public void testNoteSlowCall() {
        /*
         * This primarily verifies that calling the methods doesn't cause a crash. It isn't possible
         * to check see if a slow call is noted within the test framework.
         */

        StrictModeUtil.noteSlowCall("test"); //$NON-NLS-1$
    }
}
