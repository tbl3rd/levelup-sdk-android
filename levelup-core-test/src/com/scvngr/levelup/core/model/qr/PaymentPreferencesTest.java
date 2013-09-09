package com.scvngr.levelup.core.model.qr;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class PaymentPreferencesTest extends AndroidTestCase {

    @SmallTest
    public void testgetPreferenceVersion_withV1Prefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion(PaymentPreferencesV2Test.V2_PREFS) instanceof PaymentPreferencesV2);
    }

    @SmallTest
    public void testgetPreferenceVersion_invalidPrefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion("") instanceof PaymentPreferencesV2); //$NON-NLS-1$
    }
}
