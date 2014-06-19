package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.scvngr.levelup.core.model.AppFixture;
import com.scvngr.levelup.core.model.Permission;
import com.scvngr.levelup.core.model.PermissionFixture;
import com.scvngr.levelup.core.model.PermissionsRequest;
import com.scvngr.levelup.core.model.PermissionsRequestFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

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
     * Tests parsing, but with a missing {@link com.scvngr.levelup.core.annotation.NonNull} field.
     */
    @SmallTest
    public void testJsonParse_errorMissingDescription() {
        final PermissionsRequestJsonFactory factory = new PermissionsRequestJsonFactory();

        try {
            final JsonObject model = PermissionsRequestFixture.getJsonObject();
            model.getAsJsonObject(PermissionsRequestJsonFactory.MODEL_ROOT).remove("description"); //$NON-NLS-1$
            factory.createFrom(model);
            fail("Expected exception."); //$NON-NLS-1$
        } catch (final JsonParseException e) {
            // Expected exception.
        }
    }
}
