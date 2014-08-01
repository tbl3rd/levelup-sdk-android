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

import com.scvngr.levelup.core.model.WebLink;
import com.scvngr.levelup.core.model.WebLinkFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Tests {@link WebLinkJsonFactoryTest}.
 */
public final class WebLinkJsonFactoryTest extends SupportAndroidTestCase {
    /**
     * Tests json parsing.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink = new WebLinkJsonFactory().from(object);
            assertEquals(1L, webLink.getWebLinkTypeId());
            assertEquals("title", webLink.getTitle());
            assertEquals("http://web_url", webLink.getWebUrl());
        }
    }

    /**
     * Tests json parsing a list of web links.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_objectList() throws JSONException {
        final WebLinkJsonFactory factory = new WebLinkJsonFactory();
        final JSONArray array = new JSONArray();
        array.put(WebLinkFixture.getFullJsonObject(1));
        array.put(WebLinkFixture.getFullJsonObject(2));
        array.put(WebLinkFixture.getFullJsonObject(3));
        final List<WebLink> tokens = factory.fromList(array);

        assertEquals(3, tokens.size());
    }
}
