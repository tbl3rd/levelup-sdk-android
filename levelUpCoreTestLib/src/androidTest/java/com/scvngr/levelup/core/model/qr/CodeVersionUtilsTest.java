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
package com.scvngr.levelup.core.model.qr;

import com.scvngr.levelup.core.BuildConfig;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.LogManager;

/**
 * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils}.
 */
public final class CodeVersionUtilsTest extends SupportAndroidTestCase {

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withShortCode() {
        assertFalse(CodeVersionUtils.isValidCode("0", 2, 0, 1, 1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withLongCode() {
        assertFalse(CodeVersionUtils.isValidCode("000", 1, 0, 1, 1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_withUnexpectedVersion() {
        assertFalse(CodeVersionUtils.isValidCode("31", 2, 0, 1, 1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode(String, int, int, int, int)}.
     */
    public void testIsValidCode_valid() {
        assertTrue(CodeVersionUtils.isValidCode("01", 2, 0, 2, 1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfFinalSize() {
        final String str = "111";
        assertEquals("111", CodeVersionUtils.leftPadWithZeros(str, 3));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfLesserSize() {
        final String str = "1";
        assertEquals("0001", CodeVersionUtils.leftPadWithZeros(str, 4));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withStringOfGreaterSize() {
        final String str = "11111";
        assertEquals("11111", CodeVersionUtils.leftPadWithZeros(str, 4));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withNullString() {
        assertEquals("0000", CodeVersionUtils.leftPadWithZeros(null, 4));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#leftPadWithZeros(String, int)}.
     */
    public void testLeftPadWithZeros_withNegativeSize() {

        LogManager.d("Core lib Build config: BuildConfig.DEBUG is " + BuildConfig.DEBUG);
        LogManager.d("Core lib Build config: BuildConfig.BUILD_TYPE is " + BuildConfig.BUILD_TYPE);
        LogManager.d("Core lib Build config: BuildConfig.FLAVOR is " + BuildConfig.FLAVOR);
        LogManager.d("Core lib Build config: BuildConfig.PACKAGE_NAME is " + BuildConfig.PACKAGE_NAME);
        LogManager.d("Core lib Build config: BuildConfig.VERSION_NAME is " + BuildConfig.VERSION_NAME);
        LogManager.d("Core lib Build config: BuildConfig.VERSION_CODE is " + BuildConfig.VERSION_CODE);


        LogManager.d("CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED = "+CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED);
        LogManager.d("CoreLibConstants.IS_CHECKREP_ENABLED = "+CoreLibConstants.IS_CHECKREP_ENABLED);


        try {
            CodeVersionUtils.leftPadWithZeros("0", -1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
