package com.scvngr.levelup.core.model.qr;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests {@link PaymentPreferencesV2}.
 */
public final class PaymentPreferencesV2Test extends AndroidTestCase {
    protected static final String V2_PREFS = "020013LU"; //$NON-NLS-1$

    /**
     * Keep in step with the color value in {@link #V2_PREFS}.
     */
    private static final int V2_PREFS_COLOR = 3;
    private static final String V2_PREFS_TOO_SHORT = "0012LU"; //$NON-NLS-1$
    private static final String V2_PREFS_TOO_LONG = "020AA12LU"; //$NON-NLS-1$
    private static final String V3_PREFS = "030012LU"; //$NON-NLS-1$

    /**
     * Tests {@link PaymentPreferencesV2#isValidPreference(String)} with v2 preferences.
     */
    @SmallTest
    public void testisValidPreference_withValidPrefs() {
        assertTrue(PaymentPreferencesV2.isValidPreference(V2_PREFS));
    }

    /**
     * Tests {@link PaymentPreferencesV2#isValidPreference(String)} with v3 preferences.
     */
    @SmallTest
    public void testisValidPreference_withV2Prefs() {
        assertFalse(PaymentPreferencesV2.isValidPreference(V3_PREFS));
    }

    /**
     * Tests {@link PaymentPreferencesV2#isValidPreference(String)} with v2 preferences that is too
     * short.
     */
    @SmallTest
    public void testisValidPreference_tooShort() {
        assertFalse(PaymentPreferencesV2.isValidPreference(V2_PREFS_TOO_SHORT));
    }

    /**
     * Tests {@link PaymentPreferencesV2#isValidPreference(String)} with v2 preferences that is too
     * long.
     */
    @SmallTest
    public void testisValidPreference_tooLong() {
        assertFalse(PaymentPreferencesV2.isValidPreference(V2_PREFS_TOO_LONG));
    }

    /**
     * Tests {@link PaymentPreferencesV2#getColorPreference(String)} with valid preferences.
     */
    @SmallTest
    public void testGetColorPreference_validPrefs() {
        assertEquals(V2_PREFS_COLOR, PaymentPreferences.getColorPreference(V2_PREFS));
    }

    /**
     * Tests {@link PaymentPreferencesV2#getColorPreference(String)} with valid preferences with
     * high color.
     */
    @SmallTest
    public void testGetColorPreference_validPrefsLargeColor() {
        assertEquals(35, PaymentPreferences.getColorPreference("02001ZLU")); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#getColorPreference(String)} with invalid(short)
     * preferences.
     */
    @SmallTest
    public void testGetColorPreference_invalidPrefs() {
        assertEquals(2, PaymentPreferences.getColorPreference(V2_PREFS_TOO_SHORT));
    }

    /**
     * Tests {@link PaymentPreferencesV2#getColorPreference(String)} with invalid(v3) preferences.
     */
    @SmallTest
    public void testGetColorPreference_invalidPrefsV2() {
        assertEquals(2, PaymentPreferences.getColorPreference(V3_PREFS));
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with valid values.
     */
    public void testEncode_basic() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        assertEquals("020011LU", v1.encode(1, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with large, but valid color.
     */
    public void testEncode_largeColor() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        assertEquals("02001ZLU", v1.encode(35, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with large invalid color.
     */
    public void testEncode_invalidLargeColor() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        assertEquals("020010LU", v1.encode(36, 1)); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with large, but valid tip value.
     */
    public void testEncode_largeTip() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        assertEquals("020ZZ2LU", v1.encode(2, 1295)); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with large invalid tip.
     */
    public void testEncode_invalidLargeTip() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        boolean threw = false;

        try {
            v1.encode(1, 1296);
        } catch (final IllegalArgumentException e) {
            threw = true;
        }

        assertTrue("Encode threw IllegalArgumentException", threw); //$NON-NLS-1$
    }

    /**
     * Tests {@link PaymentPreferencesV2#encode(int, int)} with negative color.
     */
    public void testEncode_invalidNegativeColor() {
        final PaymentPreferencesV2 v1 = new PaymentPreferencesV2(null);
        assertEquals("020000LU", v1.encode(-1, 0)); //$NON-NLS-1$
    }
}
