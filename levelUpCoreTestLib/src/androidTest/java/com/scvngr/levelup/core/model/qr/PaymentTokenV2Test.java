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

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.model.tip.PercentageTip;
import com.scvngr.levelup.core.test.ResourcesUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.qr.PaymentTokenV2} color parsing.
 */
public class PaymentTokenV2Test extends SupportAndroidTestCase {

    public static final String V2_CODE = "LU0201234671234567890123030012LU";
    public static final String V2_CODE_WITHOUT_PREFERENCES = "LU0201234671234567890123";
    /**
     * Keep in step with {@link #V2_CODE}
     */
    public static final int V2_CODE_CODE_COLOR_INDEX = 2;
    public static final String V3_CODE = "LU0301234671234567890123030012LU";
    public static final String V2_CODE_INVALID_COLOR = "LU020123467123456789012303001ALU";
    public static final String V2_CODE_NEWER_PAYMENT_PREFERENCES_VERSION = "LU0201234671234567890123040012LU";
    public static final String V2_CODE_INVALID_TOO_LONG = "LU020123467123456789012301001200000LU";
    public static final String V2_CODE_INVALID_TOO_SHORT = "LU0201234671234589012LU";
    public static final String V2_CODE_INVALID_UNRECOGNIZED_VERSION = "LU0001234671234567890123010012LU";

    private static final int CODE_LENGTH = 32;
    private static final int CODE_VERSION = 2;
    private static final int VERSION_INDEX_BEGIN = 2;
    private static final int VERSION_INDEX_END_EXCLUSIVE = 4;

    /**
     * Tests the fixture data with {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode}.
     */
    @SmallTest
    public void testFixtures() {
        assertTrue(isValidCode(V2_CODE));
        assertFalse(isValidCode(V2_CODE_INVALID_TOO_SHORT));
        assertFalse(isValidCode(V2_CODE_INVALID_TOO_LONG));
        assertFalse(isValidCode(V3_CODE));

        assertTrue(CodeVersionUtils.isValidCode(V2_CODE_WITHOUT_PREFERENCES, 24,
                VERSION_INDEX_BEGIN, VERSION_INDEX_END_EXCLUSIVE, CODE_VERSION));
    }

    @SmallTest
    public void testgetColor_validCode() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE);
        assertFalse(code.getColor(res) == -1);
    }

    @SmallTest
    public void testgetColor_invalidColor() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_INVALID_COLOR);
        assertTrue(code.getColor(res) == -1);
    }

    @SmallTest
    public void testgetColor_validCodeWithUnsupportedPaymentPreferenceVersion() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_NEWER_PAYMENT_PREFERENCES_VERSION);
        assertFalse(code.getColor(res) == -1);
    }

    @SmallTest
    public void testgetColor_invalidCodeUnrecognizedVersion() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final int[] array = res.getIntArray(R.array.levelup_dock_colors);
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_INVALID_UNRECOGNIZED_VERSION);
        assertEquals(array[2], code.getColor(res));
    }

    @SmallTest
    public void testEncodePaymentPreferences_basic() {
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_WITHOUT_PREFERENCES);
        assertEquals("LU0201234671234567890123030011LU", code.encodePaymentPreferences(1,
                new PercentageTip(1)));
    }

    @SmallTest
    public void testEncodePaymentPreferences_basic2() {
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_WITHOUT_PREFERENCES);
        assertEquals("LU02012346712345678901230300B2LU", code.encodePaymentPreferences(2,
                new PercentageTip(11)));
    }

    /**
     * Returns whether or not the QR code is a V2 code.
     *
     * @param qrData the data scanned from the QR code.
     * @return true if it is a V2 code, false otherwise.
     */
    private static boolean isValidCode(@NonNull final String qrData) {
        final int idx = qrData.indexOf('-');
        boolean valid = false;

        if (idx == -1) {
            valid =
                    CodeVersionUtils.isValidCode(qrData, CODE_LENGTH, VERSION_INDEX_BEGIN,
                            VERSION_INDEX_END_EXCLUSIVE, CODE_VERSION);
        }

        return valid;
    }
}
