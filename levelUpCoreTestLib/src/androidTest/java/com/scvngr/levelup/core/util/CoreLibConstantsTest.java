/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.CoreLibConstants}.
 */
public final class CoreLibConstantsTest extends SupportAndroidTestCase {

    @SmallTest
    public static void testSlowAccess() {
        assertFalse("Slow async mode should be disabled", CoreLibConstants.IS_SLOW_ASYNC_ENABLED); //$NON-NLS-1$
    }
}
