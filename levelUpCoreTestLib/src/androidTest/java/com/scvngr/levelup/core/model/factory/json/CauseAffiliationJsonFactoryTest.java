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
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.CauseAffiliation;
import com.scvngr.levelup.core.model.CauseAffiliationFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.CauseAffiliationJsonFactory}.
 */
public final class CauseAffiliationJsonFactoryTest extends SupportAndroidTestCase {
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        final CauseAffiliation causeAffiliation =
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject());
        assertEquals(
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject()),
                causeAffiliation);
        assertEquals(CauseAffiliationFixture.getFullModel().getCauseWebServiceId(),
                causeAffiliation.getCauseWebServiceId());
        assertEquals(CauseAffiliationFixture.getFullModel().getPercentDonation(),
                causeAffiliation.getPercentDonation());
    }

    @SmallTest
    public void testJsonParse_nullId() throws JSONException {
        final CauseAffiliation causeAffiliation =
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject(null));
        assertEquals(
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject(null)),
                causeAffiliation);
        assertEquals(CauseAffiliationFixture.getFullModel(null).getCauseWebServiceId(),
                causeAffiliation.getCauseWebServiceId());
        assertEquals(CauseAffiliationFixture.getFullModel(null).getPercentDonation(),
                causeAffiliation.getPercentDonation());
    }
}
