package com.scvngr.levelup.core.model.qr;

import android.content.res.Resources;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.test.ResourcesUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link PaymentTokenV2} color parsing.
 */
public class PaymentTokenV2Test extends SupportAndroidTestCase {

    public static final String V2_CODE = "020123467123456789012345020012LU"; //$NON-NLS-1$
    public static final String V2_CODE_WITHOUT_PREFERENCES = "020123467123456789012345"; //$NON-NLS-1$
    /**
     * Keep in step with {@link #V2_CODE}
     */
    public static final int V2_CODE_CODE_COLOR_INDEX = 2;
    public static final String V3_CODE = "030123467123456789012345010012LU"; //$NON-NLS-1$
    public static final String V2_CODE_INVALID_COLOR = "02012346712345678901234501001ALU"; //$NON-NLS-1$
    public static final String V2_CODE_NEWER_PAYMENT_PREFERENCES_VERSION = "020123467123456789012345030012LU"; //$NON-NLS-1$
    public static final String V2_CODE_INVALID_TOO_LONG = "02012346712345678901234501001200000LU"; //$NON-NLS-1$
    public static final String V2_CODE_INVALID_TOO_SHORT = "020123467123458901234LU"; //$NON-NLS-1$
    public static final String V2_CODE_INVALID_UNRECOGNIZED_VERSION = "000123467123456789012345010012LU"; //$NON-NLS-1$

    @SmallTest
    public void testisValidCode_withValidCode() {
        assertTrue(PaymentTokenV2.isValidCode(V2_CODE));
    }

    @SmallTest
    public void testisValidCode_withShortCode() {
        assertFalse(PaymentTokenV2.isValidCode(V2_CODE_INVALID_TOO_SHORT));
    }

    @SmallTest
    public void testisValidCode_withLongCode() {
        assertFalse(PaymentTokenV2.isValidCode(V2_CODE_INVALID_TOO_LONG));
    }

    @SmallTest
    public void testisValidCode_withV2Code() {
        assertFalse(PaymentTokenV2.isValidCode(V3_CODE));
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
    public void testIsValidToken_withV2Token() {
        assertTrue(PaymentTokenV2.isValidToken(V2_CODE_WITHOUT_PREFERENCES));
    }

    @SmallTest
    public void testEncodePaymentPreferences_basic() {
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_WITHOUT_PREFERENCES);
        assertEquals("020123467123456789012345020011LU", code.encodePaymentPreferences(1, 1)); //$NON-NLS-1$
    }

    @SmallTest
    public void testEncodePaymentPreferences_basic2() {
        final PaymentTokenV2 code = new PaymentTokenV2(V2_CODE_WITHOUT_PREFERENCES);
        assertEquals("0201234671234567890123450200B2LU", code.encodePaymentPreferences(2, 11)); //$NON-NLS-1$
    }
}
