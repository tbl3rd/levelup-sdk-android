/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.qr;

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.tip.PercentageTip;
import com.scvngr.levelup.core.model.tip.USCentTip;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3}.
 */
public final class PaymentPreferencesV3Test extends SupportAndroidTestCase {

    /**
     * Keep in step with the color value in {@link #V3_PREFS}.
     */
    private static final int V3_PREFS_COLOR = 2;
    private static final String V3_PREFS_TOO_SHORT = "0012LU";
    private static final String V3_PREFS_TOO_LONG = "030AA12LU";
    private static final String V3_PREFS = "030012LU";
    private static final String V2_PREFS = "020012LU";

    /**
     * Tests the fixture data with {@link com.scvngr.levelup.core.model.qr.CodeVersionUtils#isValidCode}.
     */
    @SmallTest
    public void testFixtures() {
        assertTrue(isValidPreference(V3_PREFS));
        assertFalse(isValidPreference(V2_PREFS));
        assertFalse(isValidPreference(V3_PREFS_TOO_SHORT));
        assertFalse(isValidPreference(V3_PREFS_TOO_LONG));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#getColorPreference(String)} with valid preferences.
     */
    @SmallTest
    public void testGetColorPreference_validPrefs() {
        assertEquals(V3_PREFS_COLOR, PaymentPreferences.getColorPreference(V3_PREFS));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#getColorPreference(String)} with valid preferences with
     * high color.
     */
    @SmallTest
    public void testGetColorPreference_validPrefsLargeColor() {
        assertEquals(35, PaymentPreferences.getColorPreference("02001ZLU"));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#getColorPreference(String)} with invalid(short)
     * preferences.
     */
    @SmallTest
    public void testGetColorPreference_invalidPrefs() {
        assertEquals(2, PaymentPreferences.getColorPreference(V3_PREFS_TOO_SHORT));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with valid values.
     */
    public void testEncode_basic() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");
        assertEquals("030011LU", preferences.encode(1, new PercentageTip(1)));
        assertEquals("031011LU", preferences.encode(1, new USCentTip(1)));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with large, but valid color.
     */
    public void testEncode_largeColor() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");
        assertEquals("03001ZLU", preferences.encode(35, new PercentageTip(1)));
        assertEquals("03101ZLU", preferences.encode(35, new USCentTip(1)));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with large invalid color.
     */
    public void testEncode_invalidLargeColor() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");
        assertEquals("030010LU", preferences.encode(36, new PercentageTip(1)));
        assertEquals("031010LU", preferences.encode(36, new USCentTip(1)));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with large, but valid tip value.
     */
    public void testEncode_largeTip() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");
        assertEquals("030ZZ2LU", preferences.encode(2, new PercentageTip(1295)));
        assertEquals("03ZZZ2LU", preferences.encode(2, new USCentTip(45359)));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with large invalid tip.
     */
    public void testEncode_invalidLargeTip() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");

        try {
            preferences.encode(1, new PercentageTip(1296));
            fail();
        } catch (final IllegalArgumentException e) {
            // pass
        }

        try {
            preferences.encode(1, new USCentTip(45360));
            fail();
        } catch (final IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.qr.PaymentPreferencesV3#encode} with negative color.
     */
    public void testEncode_invalidNegativeColor() {
        final PaymentPreferencesV3 preferences = new PaymentPreferencesV3("");
        assertEquals("030000LU", preferences.encode(-1, new PercentageTip(0)));
        assertEquals("031000LU", preferences.encode(-1, new USCentTip(0)));
    }

    /**
     * @param prefs the string preference from the QR code.
     * @return true if these preferences are v3.
     */
    private static boolean isValidPreference(@NonNull final String prefs) {
        return CodeVersionUtils.isValidCode(prefs, PaymentPreferencesV3.TOTAL_ENCODED_LENGTH, 0,
                PaymentPreferencesV3.VERSION_LENGTH,
                PaymentPreferencesV3.VERSION);
    }
}
