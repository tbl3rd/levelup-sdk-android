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
package com.scvngr.levelup.core.storage;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.PreferenceTestUtils;

import junit.framework.TestCase;

/**
 * Tests {@link com.scvngr.levelup.core.storage.CorePreferenceUtil}.
 */
public final class CorePreferenceUtilTest extends TestCase {

    /**
     * Test that the prefix of the preference keys begin with {@link com.scvngr.levelup.core.storage.CorePreferenceUtil#KEY_PREFIX}
     * plus an additional dot.
     */
    @SmallTest
    public static void testKeysArePrefixed() {
        PreferenceTestUtils.assertKeysArePrefixed(CorePreferenceUtil.class, CorePreferenceUtil
                .KEY_PREFIX);
    }

    /**
     * Test that the preference keys are unique.
     */
    @SmallTest
    public static void testKeysAreUnique() {
        PreferenceTestUtils.assertKeysAreUnique(CorePreferenceUtil.class);
    }
}
