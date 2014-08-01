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

import com.scvngr.levelup.core.model.AppFixture;
import com.scvngr.levelup.core.model.Permission;
import com.scvngr.levelup.core.model.PermissionFixture;
import com.scvngr.levelup.core.model.PermissionsRequest;
import com.scvngr.levelup.core.model.PermissionsRequestFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.util.Set;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.PermissionsRequestJsonFactory}.
 */
public final class PermissionsRequestJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests parsing a {@link com.scvngr.levelup.core.model.PermissionsRequest}.
     *
     * @throws org.json.JSONException on programming errors.
     */
    @SmallTest
    public void testJsonParse_full() throws JSONException {

        final PermissionsRequestJsonFactory factory = new PermissionsRequestJsonFactory();

        final PermissionsRequest permissionsRequest =
                factory.createFrom(PermissionsRequestFixture.getJsonObject());

        assertNotNull(permissionsRequest);

        assertEquals(PermissionsRequestFixture.ACCEPT_TEXT_FIXTURE_1,
                permissionsRequest.getAcceptText());
        assertEquals(AppFixture.APP_ID_FIXTURE_1, permissionsRequest.getAppId());
        assertEquals(PermissionsRequestFixture.CREATED_AT_FIXTURE_1,
                permissionsRequest.getCreatedAt());

        // A non-equals() comparison
        final Set<Permission> permissions = permissionsRequest.getPermissions();
        assertEquals(2, permissions.size());
        assertTrue(permissions.contains(PermissionFixture.getFullModel1()));
        assertTrue(permissions.contains(PermissionFixture.getFullModel2()));

        assertEquals(PermissionsRequestFixture.DESCRIPTION_FIXTURE_1,
                permissionsRequest.getDescription());

        assertEquals(PermissionsRequestFixture.PERMISSIONS_REQUEST_ID_FIXTURE_1,
                permissionsRequest.getId());
        assertEquals(PermissionsRequestFixture.REJECT_TEXT_FIXTURE_1,
                permissionsRequest.getRejectText());
        assertEquals(PermissionsRequestFixture.STATE_FIXTURE_1, permissionsRequest.getState());

        // Double-check that the equals() method also verifies the same thing.
        assertEquals(PermissionsRequestFixture.getFullModel(), permissionsRequest);
    }

    /**
     * Tests parsing, but with a missing {@link android.support.annotation.NonNull} field.
     */
    @SmallTest
    public void testJsonParse_errorMissingDescription() {
        final PermissionsRequestJsonFactory factory = new PermissionsRequestJsonFactory();

        try {
            final JsonObject model = PermissionsRequestFixture.getJsonObject();
            model.getAsJsonObject(PermissionsRequestJsonFactory.MODEL_ROOT).remove("description");
            factory.createFrom(model);
            fail("Expected exception.");
        } catch (final JsonParseException e) {
            // Expected exception.
        }
    }
}
