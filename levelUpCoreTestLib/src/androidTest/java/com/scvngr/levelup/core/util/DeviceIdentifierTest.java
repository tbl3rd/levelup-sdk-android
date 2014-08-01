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
package com.scvngr.levelup.core.util;

import android.provider.Settings;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.DeviceIdentifier}.
 */
public final class DeviceIdentifierTest extends SupportAndroidTestCase {

    @SmallTest
    public void testGetDeviceId() {
        assertEquals(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID),
                DeviceIdentifier.getDeviceId(NullUtils.nonNullContract(getContext())));
    }
}
