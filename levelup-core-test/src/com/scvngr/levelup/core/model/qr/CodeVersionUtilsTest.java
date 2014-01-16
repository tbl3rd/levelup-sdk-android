/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.qr;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link CodeVersionUtils}.
 */
public final class CodeVersionUtilsTest extends SupportAndroidTestCase {

    /**
     * Tests {@link CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withShortCode() {
        assertFalse(CodeVersionUtils.isValidCode("0", 2, 0, 1, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withLongCode() {
        assertFalse(CodeVersionUtils.isValidCode("000", 1, 0, 1, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withUnexpectedVersion() {
        assertFalse(CodeVersionUtils.isValidCode("31", 2, 0, 1, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_valid() {
        assertTrue(CodeVersionUtils.isValidCode("01", 2, 0, 2, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfFinalSize() {
        final String str = "111"; //$NON-NLS-1$
        assertEquals("111", CodeVersionUtils.leftPadWithZeros(str, 3)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfLesserSize() {
        final String str = "1"; //$NON-NLS-1$
        assertEquals("0001", CodeVersionUtils.leftPadWithZeros(str, 4)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfGreaterSize() {
        final String str = "11111"; //$NON-NLS-1$
        assertEquals("11111", CodeVersionUtils.leftPadWithZeros(str, 4)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withNullString() {
        assertEquals("0000", CodeVersionUtils.leftPadWithZeros(null, 4)); //$NON-NLS-1$
    }

    /**
     * Tests {@link CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withNegativeSize() {

        try {
            CodeVersionUtils.leftPadWithZeros("0", -1); //$NON-NLS-1$
            fail("should have thrown an exception"); //$NON-NLS-1$
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
