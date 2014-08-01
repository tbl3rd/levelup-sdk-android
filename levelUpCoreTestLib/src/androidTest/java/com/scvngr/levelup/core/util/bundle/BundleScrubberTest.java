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
package com.scvngr.levelup.core.util.bundle;

import android.content.Intent;
import android.os.Bundle;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

/**
 * Tests the {@link com.scvngr.levelup.core.util.bundle.BundleScrubber}.
 */
public final class BundleScrubberTest extends TestCase {
    /*
     * There is unfortunately no way to test the Private serializable scenario, because Serializable
     * objects created here would be available to this app's class loader.
     */

    /**
     * Verifies that passing null to {@link com.scvngr.levelup.core.util.bundle.BundleScrubber#scrub(android.content.Intent)} returns false.
     */
    @SmallTest
    public void testScrubNullIntent() {
        assertFalse(BundleScrubber.scrub((Intent) null));
    }

    /**
     * Verifies that passing a valid Intent to {@link com.scvngr.levelup.core.util.bundle.BundleScrubber#scrub(android.content.Intent)} returns false
     * and does not mutate the Intent.
     */
    @SmallTest
    public void testScrubValidIntent() {
        {
            /*
             * An empty Intent should be valid and should not be mutated
             */
            final Intent intent = new Intent();

            assertNull(intent.getExtras());
            assertFalse(BundleScrubber.scrub(intent));

            assertNull(intent.getExtras());
        }

        {
            /*
             * A non empty Intent should be valid and should not be mutated
             */
            final Intent intent = new Intent().putExtra("test", "test");
            assertFalse(BundleScrubber.scrub(intent));

            assertEquals(1, intent.getExtras().keySet().size());
            assertEquals("test", intent.getStringExtra("test"));
        }
    }

    /**
     * Verifies that passing null to {@link com.scvngr.levelup.core.util.bundle.BundleScrubber#scrub(android.os.Bundle)} returns false.
     */
    @SmallTest
    public void testScrubNullBundle() {
        assertFalse(BundleScrubber.scrub((Bundle) null));
    }

    /**
     * Verifies that passing a valid Bundle to {@link com.scvngr.levelup.core.util.bundle.BundleScrubber#scrub(android.os.Bundle)} returns false
     * and does not mutate the Bundle.
     */
    @SmallTest
    public void testScrubValidBundle() {
        {
            /*
             * An empty Bundle should be valid and should not be mutated
             */
            final Bundle bundle = new Bundle();

            assertFalse(BundleScrubber.scrub(bundle));

            assertEquals(0, bundle.keySet().size());
        }

        {
            /*
             * A non empty Intent should be valid and should not be mutated
             */
            final Intent intent = new Intent().putExtra("test", "test");
            assertFalse(BundleScrubber.scrub(intent));

            assertEquals(1, intent.getExtras().keySet().size());
            assertEquals("test", intent.getStringExtra("test"));
        }
    }
}
