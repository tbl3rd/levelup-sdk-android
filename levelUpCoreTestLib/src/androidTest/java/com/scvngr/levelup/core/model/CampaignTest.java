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

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.CampaignJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.Campaign}.
 */
public final class CampaignTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel() throws JSONException {
        {
            final JSONObject object = CampaignFixture.getFullJsonObject();

            final Campaign campaign = new CampaignJsonFactory().from(object);
            final Parcel parcel = Parcel.obtain();
            campaign.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final Campaign parceled = Campaign.CREATOR.createFromParcel(parcel);
            assertEquals(campaign, parceled);
        }
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(CampaignJsonFactory.JsonKeys.class,
                new CampaignJsonFactory(), CampaignFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }
}
