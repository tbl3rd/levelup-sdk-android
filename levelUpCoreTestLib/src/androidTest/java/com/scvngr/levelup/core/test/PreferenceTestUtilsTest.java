/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;

import junit.framework.AssertionFailedError;

/**
 * Tests {@link PreferenceTestUtils}.
 */
public final class PreferenceTestUtilsTest extends SupportAndroidTestCase {

    @NonNull
    private static final String KEY_PREFIX_ONE =
            "com.scvngr.levelup.core.test.storage.preference"; //$NON-NLS-1$

    @NonNull
    private static final String KEY_PREFIX_TWO = "com.example.storage.preference"; //$NON-NLS-1$

    /**
     * Tests {@link PreferenceTestUtils#assertKeysArePrefixed}.
     */
    @SmallTest
    public void testAssertKeysArePrefixed() {
        PreferenceTestUtils.assertKeysArePrefixed(ValidPreferenceClass.class, KEY_PREFIX_ONE);

        try {
            PreferenceTestUtils.assertKeysArePrefixed(ValidPreferenceClass.class, KEY_PREFIX_TWO);
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }

        try {
            PreferenceTestUtils.assertKeysArePrefixed(ValidPreferenceClass.class, "com.example"); //$NON-NLS-1$
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }

        try {
            PreferenceTestUtils.assertKeysArePrefixed(ValidPreferenceClass.class,
                    "com.example.storage.preferences"); //$NON-NLS-1$
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }

        PreferenceTestUtils.assertKeysArePrefixed(DuplicatePreferenceClass.class, KEY_PREFIX_ONE);

        try {
            PreferenceTestUtils.assertKeysArePrefixed(DuplicatePreferenceClass.class,
                    KEY_PREFIX_TWO);
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }

        try {
            PreferenceTestUtils.assertKeysArePrefixed(InconsistentPrefixPreferenceClass.class,
                    KEY_PREFIX_ONE);
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }

        try {
            PreferenceTestUtils.assertKeysArePrefixed(InconsistentPrefixPreferenceClass.class,
                    KEY_PREFIX_TWO);
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }
    }

    /**
     * Tests {@link PreferenceTestUtils#assertKeysAreUnique}.
     */
    @SmallTest
    public void testAssertKeysAreUnique() {
        PreferenceTestUtils.assertKeysAreUnique(ValidPreferenceClass.class);
        PreferenceTestUtils.assertKeysAreUnique(InconsistentPrefixPreferenceClass.class);

        try {
            PreferenceTestUtils.assertKeysAreUnique(DuplicatePreferenceClass.class);
            fail();
        } catch (final AssertionFailedError e) {
            // Expected
        }
    }

    /**
     * Class that defines duplicate preference keys.
     */
    @SuppressWarnings("unused")
    private static final class DuplicatePreferenceClass {

        @NonNull
        public static final String KEY_STRING_TO_DUPE = KEY_PREFIX_ONE + ".string_to_dupe"; //$NON-NLS-1$

        @NonNull
        public static final String KEY_STRING_OR_NOT_TO_DUPE = KEY_PREFIX_ONE + ".string_to_dupe"; //$NON-NLS-1$
    }

    /**
     * Class that defines a preference keys with different prefixes.
     */
    @SuppressWarnings("unused")
    private static final class InconsistentPrefixPreferenceClass {

        @NonNull
        public static final String KEY_STRING_NOT_BAD = KEY_PREFIX_ONE + ".string_not_bad"; //$NON-NLS-1$

        @NonNull
        public static final String KEY_STRING_WRONG_PREFIX = KEY_PREFIX_TWO
                + ".string_wrong_prefix"; //$NON-NLS-1$
    }

    /**
     * Class that defines valid preference keys.
     */
    @SuppressWarnings("unused")
    private static final class ValidPreferenceClass {

        @NonNull
        public static final String KEY_STRING_FAVORITE_DINOSAUR = KEY_PREFIX_ONE
                + ".string_favorite_dinosaur"; //$NON-NLS-1$

        @NonNull
        public static final String KEY_BOOLEAN_IS_EXTINCT = KEY_PREFIX_ONE + ".boolean_is_extinct"; //$NON-NLS-1$
    }
}
