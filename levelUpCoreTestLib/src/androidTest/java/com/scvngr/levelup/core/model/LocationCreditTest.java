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

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.LocationCreditJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.LocationCredit}
 */
public final class LocationCreditTest extends AndroidTestCase {
    @SmallTest
    public void testParcel() throws JSONException {
        final LocationCredit locationCredit = LocationCreditFixture.getFullModel(5);
        ParcelTestUtils.assertParcelableRoundtrips(locationCredit);
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(LocationCreditJsonFactory.JsonKeys.class,
                new LocationCreditJsonFactory(), LocationCreditFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }
}
