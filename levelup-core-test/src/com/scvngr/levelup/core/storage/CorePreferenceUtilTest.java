/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.storage;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.PreferenceTestUtils;

import junit.framework.TestCase;

/**
 * Tests {@link CorePreferenceUtil}.
 */
public final class CorePreferenceUtilTest extends TestCase {

    /**
     * Test that the prefix of the preference keys begin with {@link CorePreferenceUtil#KEY_PREFIX}
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
