/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.qr;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

public class PaymentPreferencesTest extends SupportAndroidTestCase {

    @SmallTest
    public void testgetPreferenceVersion_withV1Prefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion(PaymentPreferencesV2Test.V2_PREFS) instanceof PaymentPreferencesV2);
    }

    @SmallTest
    public void testgetPreferenceVersion_invalidPrefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion("") instanceof PaymentPreferencesV2); //$NON-NLS-1$
    }
}
