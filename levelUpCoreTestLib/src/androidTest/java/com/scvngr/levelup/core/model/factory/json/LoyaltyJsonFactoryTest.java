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

import com.scvngr.levelup.core.model.Loyalty;
import com.scvngr.levelup.core.model.LoyaltyFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.LoyaltyJsonFactory}.
 */
public final class LoyaltyJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests json parsing.
     *
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty = new LoyaltyJsonFactory().from(object);
            assertTrue(loyalty.isLoyaltyEnabled());
            assertEquals(1, loyalty.getMerchantWebServiceId().longValue());
            assertEquals(2, loyalty.getOrdersCount());
            assertEquals(1000, loyalty.getPotentialCredit().getAmount());
            assertEquals(50, loyalty.getProgressPercentage());
            assertEquals(1000, loyalty.getSavings().getAmount());
            assertEquals(1000, loyalty.getShouldSpend().getAmount());
            assertEquals(1000, loyalty.getSpendRemaining().getAmount());
            assertEquals(1000, loyalty.getWillEarn().getAmount());
        }
    }

    /**
     * Tests json parsing a list of credit cards.
     *
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testJsonParse_objectList() throws JSONException {
        final LoyaltyJsonFactory factory = new LoyaltyJsonFactory();
        final JSONArray array = new JSONArray();
        array.put(LoyaltyFixture.getFullJsonObject(1));
        array.put(LoyaltyFixture.getFullJsonObject(2));
        array.put(LoyaltyFixture.getFullJsonObject(3));
        final List<Loyalty> tokens = factory.fromList(array);

        assertEquals(3, tokens.size());
    }
}
