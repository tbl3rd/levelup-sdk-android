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
package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.InterstitialJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.Interstitial}
 */
public final class InterstitialTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel() {
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getMinimalModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getClaimActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getNoActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getShareActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getUrlActionModel());
    }

    @SmallTest
    public void testAction_unrecognized_or_no_action() {
        // Both unrecognized and no_action interstitials return null for getAction()
        assertNull(InterstitialFixture.getMinimalModel().getAction());
        assertNull(InterstitialFixture.getNoActionModel().getAction());
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(InterstitialJsonFactory.JsonKeys.class,
                new InterstitialJsonFactory(), InterstitialFixture.getMinimalJsonObject(),
                new String[] { "MODEL_ROOT", "ACTION" });

        assertNotSame(InterstitialFixture.getClaimActionModel(), InterstitialFixture
                .getMinimalModel());
    }
}
