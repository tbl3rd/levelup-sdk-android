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

import com.scvngr.levelup.core.model.PaymentToken;
import com.scvngr.levelup.core.model.PaymentTokenFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.PaymentTokenJsonFactory}.
 */
public final class PaymentTokenJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests basic json parsing.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        // Make sure required fields are parsed if set
        PaymentToken token = new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        assertEquals("0123456789", token.getData());
        assertEquals(1, token.getId());

        // Test nested parsing
        token = new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        assertEquals("0123456789", token.getData());
        assertEquals(1, token.getId());
    }

    /**
     * Tests basic json parsing with required key missing.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKey() throws JSONException {
        try {
            final JSONObject object = PaymentTokenFixture.getFullJsonObject();
            object.remove(PaymentTokenJsonFactory.JsonKeys.DATA);
            new PaymentTokenJsonFactory().from(object);
            fail("no data field should throw error");
        } catch (final JSONException e) {
            // Expected exception
        }

        try {
            final JSONObject object = PaymentTokenFixture.getFullJsonObject();
            object.remove(PaymentTokenJsonFactory.JsonKeys.ID);
            new PaymentTokenJsonFactory().from(object);
            fail("no id field should throw error");
        } catch (final JSONException e) {
            // Expected exception
        }
    }
}
