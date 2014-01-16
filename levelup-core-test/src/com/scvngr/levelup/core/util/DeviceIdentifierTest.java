/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.provider.Settings;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link DeviceIdentifier}.
 */
public final class DeviceIdentifierTest extends SupportAndroidTestCase {

    @SmallTest
    public void testGetDeviceId() {
        assertEquals(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID),
                DeviceIdentifier.getDeviceId(NullUtils.nonNullContract(getContext())));
    }
}
