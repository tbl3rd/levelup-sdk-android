/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.PermissionsRequest.State;
import com.scvngr.levelup.core.model.factory.json.PermissionsRequestJsonFactory;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.jcip.annotations.ThreadSafe;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A fixture for {@link PermissionsRequest}s.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class PermissionsRequestFixture {
    /**
     * A date.
     */
    @NonNull
    public static final String DATE_FIXTURE_1 = "2014-01-01T00:04:00-10:00"; //$NON-NLS-1$

    /**
     * A random permissions request ID.
     *
     * @see <a href="http://xkcd.com/221/">xkcd #221 - Random Number</a>
     */
    public static final long PERMISSIONS_REQUEST_ID_FIXTURE_1 = 5;

    /**
     * Permissions request web service ID #2
     */
    public static final long PERMISSIONS_REQUEST_ID_FIXTURE_2 = 7;

    /**
     * {@link PermissionsRequest#getAcceptText} fixture.
     */
    @NonNull
    public static final String ACCEPT_TEXT_FIXTURE_1 = "Sounds Great"; //$NON-NLS-1$

    /**
     * {@link PermissionsRequest#getCreatedAt} fixture.
     */
    @NonNull
    public static final String CREATED_AT_FIXTURE_1 = DATE_FIXTURE_1;

    /**
     * {@link PermissionsRequest#getDescription} fixture.
     */
    @NonNull
    public static final String DESCRIPTION_FIXTURE_1 =
            "Ariburgers has requested to be added to your LevelUp account and would like the following permissions"; //$NON-NLS-1$

    /**
     * {@link PermissionsRequest#getPermissions} fixture.
     */
    @NonNull
    public static final Set<Permission> PERMISSIONS_FIXTURE_1 = NullUtils
            .nonNullContract(Collections.unmodifiableSet(new HashSet<Permission>(Arrays
                    .asList(new Permission[] { PermissionFixture.getFullModel1(),
                            PermissionFixture.getFullModel2() }))));

    /**
     * {@link PermissionsRequest#getRejectText} fixture.
     */
    @NonNull
    public static final String REJECT_TEXT_FIXTURE_1 = "No Thanks"; //$NON-NLS-1$

    /**
     * {@link PermissionsRequest#getState} fixture.
     */
    @NonNull
    public static final PermissionsRequest.State STATE_FIXTURE_1 = State.PENDING;

    /**
     * {@link PermissionsRequest#getState} fixture's string representation.
     */
    @NonNull
    public static final String STATE_FIXTURE_STRING_1 = "pending"; //$NON-NLS-1$

    /**
     * {@link PermissionsRequest#getToken} fixture.
     */
    @NonNull
    public  static final String ACCESS_TOKEN_FIXTURE_1 = "my_excellent_token";

    /**
     * @param permissionsRequestId the web service ID of the {@link PermissionsRequest}.
     * @return a fully-populated {@link PermissionsRequest}.
     */
    @NonNull
    public static PermissionsRequest getFullModel(final long permissionsRequestId) {
        return new PermissionsRequest(ACCEPT_TEXT_FIXTURE_1, AppFixture.APP_ID_FIXTURE_1,
                CREATED_AT_FIXTURE_1, DESCRIPTION_FIXTURE_1, permissionsRequestId,
                PERMISSIONS_FIXTURE_1, REJECT_TEXT_FIXTURE_1, STATE_FIXTURE_1,
                ACCESS_TOKEN_FIXTURE_1);
    }

    /**
     * @return a fully-populated {@link PermissionsRequest} with ID
     *         {@link #PERMISSIONS_REQUEST_ID_FIXTURE_1}.
     */
    @NonNull
    public static PermissionsRequest getFullModel() {
        return getFullModel(PERMISSIONS_REQUEST_ID_FIXTURE_1);
    }

    /**
     * @return a fully-populated {@link JsonObject} representing a {@link PermissionsRequest}.
     */
    @NonNull
    public static JsonObject getJsonObject() {
        return getJsonObject(PermissionsRequestFixture.PERMISSIONS_REQUEST_ID_FIXTURE_1);
    }

    /**
     * @param id the web service ID of the permissions request
     * @return a fully-populated {@link JsonObject} representing a {@link PermissionsRequest}.
     */
    @NonNull
    public static JsonObject getJsonObject(final long id) {
        final JsonObject o = new JsonObject();

        o.addProperty("accept_text", PermissionsRequestFixture.ACCEPT_TEXT_FIXTURE_1); //$NON-NLS-1$
        o.addProperty("app_id", AppFixture.APP_ID_FIXTURE_1); //$NON-NLS-1$
        o.addProperty("created_at", PermissionsRequestFixture.CREATED_AT_FIXTURE_1); //$NON-NLS-1$
        o.addProperty("description", PermissionsRequestFixture.DESCRIPTION_FIXTURE_1); //$NON-NLS-1$
        o.addProperty("id", id); //$NON-NLS-1$

        final JsonArray permissions = new JsonArray();
        permissions.add(PermissionFixture.getJsonObject1());
        permissions.add(PermissionFixture.getJsonObject2());

        o.add("permissions", permissions); //$NON-NLS-1$
        o.addProperty("reject_text", PermissionsRequestFixture.REJECT_TEXT_FIXTURE_1); //$NON-NLS-1$
        o.addProperty("state", PermissionsRequestFixture.STATE_FIXTURE_STRING_1); //$NON-NLS-1$
        o.addProperty("token", PermissionsRequestFixture.ACCESS_TOKEN_FIXTURE_1);

        final JsonObject container = new JsonObject();
        container.add(PermissionsRequestJsonFactory.MODEL_ROOT, o);

        return container;
    }

    private PermissionsRequestFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
