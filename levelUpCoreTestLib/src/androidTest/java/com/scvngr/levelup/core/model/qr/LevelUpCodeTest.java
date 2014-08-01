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
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.model.tip.PercentageTip;
import com.scvngr.levelup.core.test.ResourcesUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

public class LevelUpCodeTest extends SupportAndroidTestCase {

    public static final String UNRECOGNIZED_CODE = "LU0301234671234567890123";
    public static final String UNRECOGNIZED_CODE_WITH_PARAMS = "LU0301234671234567890123020012LU";

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////// DecodeColor///////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    @SmallTest
    public void testDecodeColor_validColor() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final int[] array = res.getIntArray(R.array.levelup_dock_colors);
        for (int i = 0; i < array.length; i++) {
            final int resourceId = LevelUpCode.decodeColor(i, res);
            assertFalse(String.format(Locale.US, "Position: %d", i), resourceId == -1);
            assertEquals(resourceId, array[i]);
        }
    }

    @SmallTest
    public void testDecodeColor_invalidPosition() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        assertEquals(-1, LevelUpCode.decodeColor(-1, res));
    }

    @SmallTest
    public void testParseColor_withV2Code() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final int[] array = res.getIntArray(R.array.levelup_dock_colors);
        assertEquals(array[PaymentTokenV2Test.V2_CODE_CODE_COLOR_INDEX], LevelUpCode.parseColor(
                res, PaymentTokenV2Test.V2_CODE));
    }

    @SmallTest
    public void testParseColor_withUnrecognizedCodeWithParseableColor() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final int[] array = res.getIntArray(R.array.levelup_dock_colors);
        assertEquals(array[2], LevelUpCode.parseColor(res, UNRECOGNIZED_CODE_WITH_PARAMS));
    }

    @SmallTest
    public void testGetFullPaymentTokenVersion_withV2Code() {
        final LevelUpCode code = LevelUpCode.getFullPaymentTokenVersion(PaymentTokenV2Test.V2_CODE);
        assertTrue(code instanceof PaymentTokenV2);
    }

    @SmallTest
    public void testGetPaymentTokenVersion_withValidV2Token() {
        final LevelUpCode code =
                LevelUpCode.getPaymentTokenVersion(PaymentTokenV2Test.V2_CODE_WITHOUT_PREFERENCES);
        assertNotNull(code);
        assertTrue(code instanceof PaymentTokenV2);
    }

    @SmallTest
    public void testGetPaymentTokenVersion_withUnrecognizedToken() {
        final LevelUpCode code = LevelUpCode.getPaymentTokenVersion(UNRECOGNIZED_CODE);
        assertNotNull(code);
        assertTrue(code instanceof PaymentTokenV2);
    }

    @SmallTest
    public void testEncodeLevelUpCode_withV2Code() {
        final String base = PaymentTokenV2Test.V2_CODE_WITHOUT_PREFERENCES;
        assertEquals(base + "030027LU", LevelUpCode.encodeLevelUpCode(base, 7,
                new PercentageTip(2)));
    }

    @SmallTest
    public void testEncodeLevelUpCode_withV2CodeAndNullColor() {
        final String base = PaymentTokenV2Test.V2_CODE_WITHOUT_PREFERENCES;
        assertEquals(base + "030030LU", LevelUpCode.encodeLevelUpCode(base, -1,
                new PercentageTip(3)));
    }
}
