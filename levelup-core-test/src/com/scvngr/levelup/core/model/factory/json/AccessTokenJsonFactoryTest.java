/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
 * Tests {@link AccessTokenJsonFactory}.
 */
public final class AccessTokenJsonFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testJsonParse_singleObject() throws JSONException {
        final AccessTokenJsonFactory factory = new AccessTokenJsonFactory();
        final JSONObject object = AccessTokenFixture.getFullJsonObject();
        final AccessToken token = factory.from(object);
        assertEquals(object.get(AccessTokenJsonFactory.JsonKeys.TOKEN), token.getAccessToken());
        assertEquals(object.getLong(AccessTokenJsonFactory.JsonKeys.USER_ID), token.getUserId());
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
