package com.scvngr.levelup.core.net;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests {@link HttpMethod} for expected values.
 */
public final class HttpMethodTest extends AndroidTestCase {

    private static final String[] EXPECTED_HTTP_VERBS = { "GET", "POST", "PUT", "DELETE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * Tests that HttpMethod includes methods with the expected names.
     */
    @SmallTest
    public void testMethodNames() {
        assertEquals(EXPECTED_HTTP_VERBS.length, HttpMethod.values().length);
        for (final String verb : EXPECTED_HTTP_VERBS) {
            assertNotNull(HttpMethod.valueOf(verb));
        }
    }
}
