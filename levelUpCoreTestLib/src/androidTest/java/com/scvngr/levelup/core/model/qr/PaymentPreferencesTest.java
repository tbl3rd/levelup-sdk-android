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

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

public class PaymentPreferencesTest extends SupportAndroidTestCase {

    @SmallTest
    public void testgetPreferenceVersion_withV3Prefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion("030012LU")
                instanceof PaymentPreferencesV3);
    }

    @SmallTest
    public void testgetPreferenceVersion_invalidPrefs() {
        assertTrue(PaymentPreferences.getPreferenceVersion("") instanceof PaymentPreferencesV3);
    }
}
