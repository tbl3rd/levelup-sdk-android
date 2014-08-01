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

import com.scvngr.levelup.core.model.Claim;
import com.scvngr.levelup.core.model.ClaimFixture;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.MonetaryValueFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.ClaimJsonFactory}.
 */
public final class ClaimJsonFactoryTest extends SupportAndroidTestCase {
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        final Claim claim = new ClaimJsonFactory().from(ClaimFixture.getFullJsonObject(1));
        assertEquals("code", claim.getCode());
        assertEquals(1, claim.getId());
        final MonetaryValue money = MonetaryValueFixture.getFullModel();
        assertEquals(money.getAmount(), claim.getValue().getAmount());
        assertEquals(money.getAmount(), claim.getValueRemaining().getAmount());
    }

    @SmallTest
    public void testJsonParse_missing_all_keys() throws JSONException {
        try {
            new ClaimJsonFactory().from(new JSONObject());
            fail("should throw JSONException");
        } catch (final JSONException e) {
            // Expected Exception
        }
    }

    @SmallTest
    public void testJsonParse_missing_id() throws JSONException {

        try {
            final JSONObject json = ClaimFixture.getMinimalJsonObject();
            json.remove(ClaimJsonFactory.JsonKeys.ID);
            new ClaimJsonFactory().from(json);
            fail("should throw JSONException");
        } catch (final JSONException e) {
            // Expected Exception
        }
    }

    @SmallTest
    public void testJsonParse_missing_value() throws JSONException {

        try {
            final JSONObject json = ClaimFixture.getMinimalJsonObject();
            json.remove(ClaimJsonFactory.JsonKeys.VALUE);
            new ClaimJsonFactory().from(json);
            fail("should throw JSONException");
        } catch (final JSONException e) {
            // Expected Exception
        }
    }

    @SmallTest
    public void testJsonParse_missing_value_remaining() throws JSONException {

        try {
            final JSONObject json = ClaimFixture.getMinimalJsonObject();
            json.remove(ClaimJsonFactory.JsonKeys.VALUE_REMAINING);
            new ClaimJsonFactory().from(json);
            fail("should throw JSONException");
        } catch (final JSONException e) {
            // Expected Exception
        }
    }

    @SmallTest
    public void testJsonParse_minimal_object() throws JSONException {
        // Make sure the minimum base set of attrs doesn't throw an exception.
        new ClaimJsonFactory().from(ClaimFixture.getMinimalJsonObject());
    }
}
