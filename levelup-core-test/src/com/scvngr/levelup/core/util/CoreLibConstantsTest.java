package com.scvngr.levelup.core.util;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests {@link CoreLibConstants}.
 */
public final class CoreLibConstantsTest extends AndroidTestCase {

    @SmallTest
    public static void testSlowAccess() {
        assertFalse("Slow async mode should be disabled", CoreLibConstants.IS_SLOW_ASYNC_ENABLED); //$NON-NLS-1$
    }
}
