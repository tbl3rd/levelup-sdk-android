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

import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.UserJsonFactory}.
 */
public final class UserJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests full User model json parsing.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_full() throws JSONException {
        final UserJsonFactory factory = new UserJsonFactory();
        final JSONObject object = UserFixture.getFullJsonObject();
        final User user = factory.from(object);
        assertEquals(UserFixture.getFullModel(), user);
    }

    /**
     * Tests minimal User model json parsing.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_minimal() throws JSONException {
        final UserJsonFactory factory = new UserJsonFactory();
        final JSONObject object = UserFixture.getMinimalJsonObject();
        final User user = factory.from(object);
        assertEquals(UserFixture.getMinimalModel(), user);
    }

    /**
     * Tests basic json parsing with required key missing.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKey() throws JSONException {
        try {
            final JSONObject object = UserFixture.getMinimalJsonObject();
            object.getJSONObject(UserJsonFactory.JsonKeys.MODEL_ROOT).remove(
                    UserJsonFactory.JsonKeys.ID);
            new UserJsonFactory().from(object);
            fail("no ID field should throw error");
        } catch (final JSONException e) {
            // Expected exception
        }
    }
}
