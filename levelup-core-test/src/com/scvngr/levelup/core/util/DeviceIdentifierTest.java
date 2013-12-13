package com.scvngr.levelup.core.util;

import android.provider.Settings;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests {@link DeviceIdentifier}.
 */
public final class DeviceIdentifierTest extends AndroidTestCase {

    @SmallTest
    public void testGetDeviceId() {
        assertEquals(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID),
                DeviceIdentifier.getDeviceId(NullUtils.nonNullContract(getContext())));
    }
}
