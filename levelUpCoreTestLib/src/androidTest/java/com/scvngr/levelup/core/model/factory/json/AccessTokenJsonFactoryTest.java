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

import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.AccessTokenFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.AccessTokenJsonFactory}.
 */
public final class AccessTokenJsonFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testJsonParse_singleObject() throws JSONException {
        final AccessTokenJsonFactory factory = new AccessTokenJsonFactory();
        final JSONObject object = AccessTokenFixture.getFullJsonObject();
        final AccessToken token = factory.from(object);
        assertEquals(object.get(AccessTokenJsonFactory.JsonKeys.TOKEN), token.getAccessToken());
        assertEquals(Long.valueOf(object.getLong(AccessTokenJsonFactory.JsonKeys.USER_ID)),
                token.getUserId());
    }

    @SmallTest
    public void testJsonParse_singleObjectMinimalModel() throws JSONException {
        final AccessTokenJsonFactory factory = new AccessTokenJsonFactory();
        final JSONObject object = AccessTokenFixture.getMinimalJsonObject();
        final AccessToken token = factory.from(object);
        assertEquals(object.get(AccessTokenJsonFactory.JsonKeys.TOKEN), token.getAccessToken());
        assertNull(token.getUserId());
    }

    @SmallTest
    public void testJsonParse_objectList() throws JSONException {
        final AccessTokenJsonFactory factory = new AccessTokenJsonFactory();
        final JSONArray array = new JSONArray();
        array.put(AccessTokenFixture.getFullJsonObject());
        array.put(AccessTokenFixture.getFullJsonObject());
        array.put(AccessTokenFixture.getFullJsonObject());
        final List<AccessToken> tokens = factory.fromList(array);

        assertEquals(3, tokens.size());
    }
}
