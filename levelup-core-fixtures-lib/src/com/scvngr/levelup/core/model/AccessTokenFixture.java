/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.AccessTokenJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link AccessToken}.
 */
public final class AccessTokenFixture {
    /**
     * An arbitrary access token string.
     */
    public static final String ACCESS_TOKEN_FIXTURE_1 = "sx82GslFAv8ZlHOhpftd"; //$NON-NLS-1$

    /**
     * A random user ID.
     *
     * @see <a href="http://xkcd.com/221/">xkcd #221 - Random Number</a>
     */
    public static final long USER_ID_FIXTURE_1 = 4;

    /**
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel() {
        return new AccessToken(ACCESS_TOKEN_FIXTURE_1, USER_ID_FIXTURE_1);
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel(@NonNull final String accessToken, final long userId) {
        return new AccessToken(accessToken, userId);
    }

    /**
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(ACCESS_TOKEN_FIXTURE_1, USER_ID_FIXTURE_1);
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject
            getFullJsonObject(@NonNull final String accessToken, final long userId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(AccessTokenJsonFactory.JsonKeys.TOKEN, accessToken);
            object.put(AccessTokenJsonFactory.JsonKeys.USER_ID, userId);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }
}
