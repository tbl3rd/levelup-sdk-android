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

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.StrictModeUtil}.
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

        StrictModeUtil.noteSlowCall("test");
    }
}
