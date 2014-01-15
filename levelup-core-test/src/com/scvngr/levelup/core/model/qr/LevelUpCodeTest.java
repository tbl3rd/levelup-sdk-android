/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.qr;

import android.content.res.Resources;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.test.ResourcesUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

public class LevelUpCodeTest extends SupportAndroidTestCase {

    public static final String UNRECOGNIZED_CODE = "LU0301234671234567890123"; //$NON-NLS-1$
    public static final String UNRECOGNIZED_CODE_WITH_PARAMS = "LU0301234671234567890123020012LU"; //$NON-NLS-1$

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////// DecodeColor///////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    @SmallTest
    public void testDecodeColor_validColor() {
        final Resources res = ResourcesUtil.getAppResources(getContext());
        final int[] array = res.getIntArray(R.array.levelup_dock_colors);
        for (int i = 0; i < array.length; i++) {
            final int resourceId = LevelUpCode.decodeColor(i, res);
            assertFalse(String.format(Locale.US, "Position: %d", i), resourceId == -1); //$NON-NLS-1$
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
    public void testGetPaymentTokenVersion_withValidV2Code() {
        final LevelUpCode code = LevelUpCode.getPaymentTokenVersion(PaymentTokenV2Test.V3_CODE);
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
    public void testEncodeLevelUpCode_witV2Token() {
        final String base = PaymentTokenV2Test.V2_CODE_WITHOUT_PREFERENCES;
        assertEquals(base + "020027LU", LevelUpCode.encodeLevelUpCode(base, 7, 2)); //$NON-NLS-1$
    }

    @SmallTest
    public void testEncodeLevelUpCode_withV2CodeAndNullColor() {
        final String base = PaymentTokenV2Test.V2_CODE_WITHOUT_PREFERENCES;
        assertEquals(base + "020030LU", LevelUpCode.encodeLevelUpCode(base, -1, 3)); //$NON-NLS-1$
    }
}
